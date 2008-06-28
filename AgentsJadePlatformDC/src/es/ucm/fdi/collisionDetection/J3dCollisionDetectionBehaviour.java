package es.ucm.fdi.collisionDetection;

import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PickBounds;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOnCollisionMovement;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import es.ucm.fdi.collisionDetection.mathCollision.MathCollision;
import es.ucm.fdi.mcdm.OrientacionAgenteVisto;


//¡¡ESTE COMPORTAMIENTO NO ES UN COMPORTAMIENTO JADE!!
//------------------------------------------------------

public class J3dCollisionDetectionBehaviour extends Behavior {

	//the branch that we check for collisions
	private BranchGroup pickRoot = null;
	private WakeupCondition m_WakeupCondition= null;
	//how often we check for a collision
	public static final int ELAPSED_FRAME_COUNT = 1;		
	//the collision object that we are controlling
	private TransformGroup collisionObject = null;
	private String nombreAgenteClase= "";//NOMBRE DEL AGENTE AL QUE CORRESPONDE ESTE OBJETO.
	//the appearance object that we are controlling
	private Appearance objectAppearance = null;
	//cached Material objects that define the collided and missed colors
	//private Material collideMaterialNegro = null;
	private Material collideMaterialRojo = null;	
	private Material missMaterialAzul = null;
	
	private PickBounds pickBounds = null;
	private PickTool pickTool;

	//the current position of the object
	private Vector3d positionObject = null;	
	private Orientation orientacion;
	private Java3d j3d;
	
	
	public J3dCollisionDetectionBehaviour(Java3d java3d, BranchGroup pickRoot, TransformGroup collisionObject, Appearance app, Vector3d positionObject, Orientation orientacion){
		//save references to the objects
		this.pickRoot = pickRoot;	
		this.collisionObject= collisionObject;
		this.objectAppearance = app;	
		this.j3d= java3d;

		String[] nombreObjetoClase= this.collisionObject.getName().split(" ");
		//Por como he puesto el nombre de cada agente, las posiciones 2, 4 y 6 corresponden a las coordenadas.
		
		this.nombreAgenteClase= nombreObjetoClase[0];
		this.positionObject= positionObject; 
		this.orientacion= orientacion;
		// create the WakeupCriterion for the behavior
		WakeupCriterion criterionArray[] = new WakeupCriterion[1];
		criterionArray[0] = new WakeupOnCollisionMovement(collisionObject);

		objectAppearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

		this.collisionObject.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.collisionObject.setCapability(Node.ALLOW_BOUNDS_READ);

		// save the WakeupCriterion for the behavior
		m_WakeupCondition = new WakeupOr(criterionArray);		
	}	

	public void initialize() {

		Color3f objColor = new Color3f(0.0f, 0.0f, 0.0f);//new Color3f(1.0f, 0.1f, 0.2f);
		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);//color negro para los conos
		//collideMaterialNegro = new Material(objColor, black, objColor, black, 80.0f);
		
		Color3f red = new Color3f(255f, 0.0f, 0.0f);//color negro para los conos
		collideMaterialRojo = new Material(objColor, red, objColor, red, 80.0f);

		objColor = new Color3f(0.0f, 0.1f, 0.8f);//color azul para los conos
		missMaterialAzul = new Material(objColor, black, objColor, black, 80.0f);
		
		objectAppearance.setMaterial(missMaterialAzul);

		//apply the initial WakeupCriterion
		wakeupOn(m_WakeupCondition);
	}

	/*
	 * ESTA FUNCIÓN ES INVOCADA CADA VEZ QUE SE PRODUCE UNA COLISIÓN (ES INVOCADA
	 * TANTAS VECES COMO COLISIONES SE HAYAN PRODUCIDO EN EL ESCENARIO.
	 */
	public void processStimulus(Enumeration criteria) {	

		while (criteria.hasMoreElements()) {
			WakeupCriterion wakeUp = (WakeupCriterion) criteria.nextElement();

			pickTool = new PickTool(pickRoot);
			
			// create a PickBounds	
			pickBounds= new PickBounds(new BoundingBox(new Point3d(positionObject.x-5, positionObject.y-5, positionObject.z-5), 
					new Point3d(positionObject.x+5, positionObject.y+5, positionObject.z+5)));			
			
			pickTool.setShape(pickBounds, new Point3d(positionObject.x, positionObject.y, positionObject.z));			

			PickResult[] resultArray = pickTool.pickAll();// Selects all the nodes that intersect the PickShape.										

			InfoCollision infoAgente= isCollision(resultArray);
			if(infoAgente != null){
				//infoAgente.setParteConoColisionada("derecha");
				j3d.getInfoColisiones().add(infoAgente);				
				System.out.println("---->>>EL AGENTE "+this.nombreAgenteClase+" HA COLISIONADO CON ALGO");
			}
			else{
				onMiss();//No ha habido colisión.
				System.out.println("NO HA HABIDO COLISIÓN");
			}

		}//fin del while

		// assign the next WakeUpCondition, so we are notified again
		wakeupOn(m_WakeupCondition);//QUITANDO ESTA LINEA, ESTA FUNCIÓN SOLO LE EJECUTA UNA VEZ
	}

	//FUNCIÓN QUE DETECTA SI HA HABIDO UNA COLISIÓN:
	//Esta función originalmente devolvia true si habia habido colisión y false e.c.c.
	//Ahora devuelve el objeto con el que ha colisionado o null si no ha colisionado con nadie.
	public InfoCollision isCollision(PickResult[] resultArray) {

		InfoCollision ic= null;

		if (resultArray != null && resultArray.length != 0){

			// we use the user data on the nodes to ignore the
			// case of the collisionObject having collided with itself!
			// the user data also gives us a good mechanism for reporting the
			// collisions.
			for (int n = 0; n < resultArray.length; n++) {
				
				
				
				Object userData = resultArray[n].getObject().getUserData();				
				String[] nombreObjetoConElQueColisiona= userData.toString().split(" ");
				String nombreAgenteConElQueColisiona= nombreObjetoConElQueColisiona[0];

				if (userData != null && userData instanceof String) {	    	 
					// check that we are not colliding with ourselves...				
					if((((String)userData).contains("esfera"))&&
							(!this.nombreAgenteClase.equals(nombreAgenteConElQueColisiona))) { //Ignora la colisión con él mismo.		

						System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");
						Object ob = resultArray[n].getObject();
						if(ob.getClass().equals(Shape3D.class)){
							Shape3D sp= (Shape3D)ob; 
							Transform3D coord= new Transform3D();
							sp.getLocalToVworld(coord);
							Vector3d v3d= new Vector3d();
							coord.get(v3d);							
							//System.out.println(v3d.x+", "+v3d.y+", "+v3d.z);	
							
							
							//Por como he puesto el nombre de cada agente, las posiciones 2, 4 y 6 corresponden a las coordenadas.
							//double cX2= Double.parseDouble(nombreObjetoConElQueColisiona[2]);
							//double cY2= Double.parseDouble(nombreObjetoConElQueColisiona[4]);
							//double cZ2= Double.parseDouble(nombreObjetoConElQueColisiona[6]);		
							String orientacionObjetoVisto=  nombreObjetoConElQueColisiona[8];
							Vector3d cono= new Vector3d(this.positionObject.x,this.positionObject.y,this.positionObject.z);
							//Vector3d esfera= new Vector3d(cX2,cY2,cZ2);
							Vector3d esfera= new Vector3d(v3d.x,v3d.y,v3d.z);

							MathCollision mc= new MathCollision();
							String res= mc.detectaColision(orientacion, cono, esfera);
							System.out.println("........................................");
							System.out.println(userData.toString());
							System.out.println(orientacion);
							System.out.println(cono.x+", "+cono.y+", "+cono.z);
							System.out.println(esfera.x+", "+esfera.y+", "+esfera.z);
							System.out.println("........................................");
							if(res != null){
								if(res.equals("centro")||res.equals("izquierda")||res.equals("derecha")){
									objectAppearance.setMaterial(collideMaterialRojo);//SE CAMBIA EL COLOR CUANDO HAY COLISION
									double cp= claridadDePercepcion(cono, esfera, 1, 5);
									//System.out.println("Claridad de percepción: "+cp);
									String tipoAgente= "";
									if(nombreAgenteConElQueColisiona.contains("gato"))
										tipoAgente= "gato";
									else if(nombreAgenteConElQueColisiona.contains("raton"))
										tipoAgente= "raton";

									double distancia= calculaDistancia(cono, esfera);

									OrientacionAgenteVisto orientacion= calculaOrientacion(this.orientacion, orientacionObjetoVisto);
									InfoCollision infoAgente= new InfoCollision(this.nombreAgenteClase, nombreAgenteConElQueColisiona, res, tipoAgente, orientacion, cp, distancia);

									ic= infoAgente;
									//return infoAgente;
								}
							}
						}										
						System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");						
					}				       
				}			
			}
		}

		return ic;
		//return null;
	}

	private OrientacionAgenteVisto calculaOrientacion(Orientation orientacion, String ov) {
		OrientacionAgenteVisto res= null;
		switch(orientacion){
		case N: {if(ov.equals("N")) res= OrientacionAgenteVisto.ESPALDAS; 
		else if(ov.equals("NE")) res= OrientacionAgenteVisto.ESPALDAS_DERECHA; 
		else if(ov.equals("E")) res= OrientacionAgenteVisto.LADO_DERECHA;
		else if(ov.equals("SE")) res= OrientacionAgenteVisto.FRENTE_DERECHA;
		else if(ov.equals("S")) res= OrientacionAgenteVisto.FRENTE;
		else if(ov.equals("SO")) res= OrientacionAgenteVisto.FRENTE_IZQUIERDA;
		else if(ov.equals("O")) res= OrientacionAgenteVisto.LADO_IZQUIERDA;
		else if(ov.equals("NO")) res= OrientacionAgenteVisto.ESPALDAS_IZQUIERDA;
		break;}
		case NE: {if(ov.equals("NE")) res= OrientacionAgenteVisto.ESPALDAS; 
		else if(ov.equals("E")) res= OrientacionAgenteVisto.ESPALDAS_DERECHA; 
		else if(ov.equals("SE")) res= OrientacionAgenteVisto.LADO_DERECHA;
		else if(ov.equals("S")) res= OrientacionAgenteVisto.FRENTE_DERECHA;
		else if(ov.equals("SO")) res= OrientacionAgenteVisto.FRENTE;
		else if(ov.equals("O")) res= OrientacionAgenteVisto.FRENTE_IZQUIERDA;
		else if(ov.equals("NO")) res= OrientacionAgenteVisto.LADO_IZQUIERDA;
		else if(ov.equals("N")) res= OrientacionAgenteVisto.ESPALDAS_IZQUIERDA;
		break;} 
		case E: {if(ov.equals("E")) res= OrientacionAgenteVisto.ESPALDAS; 
		else if(ov.equals("SE")) res= OrientacionAgenteVisto.ESPALDAS_DERECHA; 
		else if(ov.equals("S")) res= OrientacionAgenteVisto.LADO_DERECHA;
		else if(ov.equals("SO")) res= OrientacionAgenteVisto.FRENTE_DERECHA;
		else if(ov.equals("O")) res= OrientacionAgenteVisto.FRENTE;
		else if(ov.equals("NO")) res= OrientacionAgenteVisto.FRENTE_IZQUIERDA;
		else if(ov.equals("N")) res= OrientacionAgenteVisto.LADO_IZQUIERDA;
		else if(ov.equals("NE")) res= OrientacionAgenteVisto.ESPALDAS_IZQUIERDA;
		break;} 
		case SE: {if(ov.equals("SE")) res= OrientacionAgenteVisto.ESPALDAS; 
		else if(ov.equals("S")) res= OrientacionAgenteVisto.ESPALDAS_DERECHA; 
		else if(ov.equals("SO")) res= OrientacionAgenteVisto.LADO_DERECHA;
		else if(ov.equals("O")) res= OrientacionAgenteVisto.FRENTE_DERECHA;
		else if(ov.equals("NO")) res= OrientacionAgenteVisto.FRENTE;
		else if(ov.equals("N")) res= OrientacionAgenteVisto.FRENTE_IZQUIERDA;
		else if(ov.equals("NE")) res= OrientacionAgenteVisto.LADO_IZQUIERDA;
		else if(ov.equals("E")) res= OrientacionAgenteVisto.ESPALDAS_IZQUIERDA;
		break;} 
		case S: {if(ov.equals("S")) res= OrientacionAgenteVisto.ESPALDAS; 
		else if(ov.equals("SO")) res= OrientacionAgenteVisto.ESPALDAS_DERECHA; 
		else if(ov.equals("O")) res= OrientacionAgenteVisto.LADO_DERECHA;
		else if(ov.equals("NO")) res= OrientacionAgenteVisto.FRENTE_DERECHA;
		else if(ov.equals("N")) res= OrientacionAgenteVisto.FRENTE;
		else if(ov.equals("NE")) res= OrientacionAgenteVisto.FRENTE_IZQUIERDA;
		else if(ov.equals("E")) res= OrientacionAgenteVisto.LADO_IZQUIERDA;
		else if(ov.equals("SE")) res= OrientacionAgenteVisto.ESPALDAS_IZQUIERDA;
		break;} 
		case SO: {if(ov.equals("SO")) res= OrientacionAgenteVisto.ESPALDAS; 
		else if(ov.equals("O")) res= OrientacionAgenteVisto.ESPALDAS_DERECHA; 
		else if(ov.equals("NO")) res= OrientacionAgenteVisto.LADO_DERECHA;
		else if(ov.equals("N")) res= OrientacionAgenteVisto.FRENTE_DERECHA;
		else if(ov.equals("NE")) res= OrientacionAgenteVisto.FRENTE;
		else if(ov.equals("E")) res= OrientacionAgenteVisto.FRENTE_IZQUIERDA;
		else if(ov.equals("SE")) res= OrientacionAgenteVisto.LADO_IZQUIERDA;
		else if(ov.equals("S")) res= OrientacionAgenteVisto.ESPALDAS_IZQUIERDA;
		break;} 
		case O: {if(ov.equals("O")) res= OrientacionAgenteVisto.ESPALDAS; 
		else if(ov.equals("NO")) res= OrientacionAgenteVisto.ESPALDAS_DERECHA; 
		else if(ov.equals("N")) res= OrientacionAgenteVisto.LADO_DERECHA;
		else if(ov.equals("NE")) res= OrientacionAgenteVisto.FRENTE_DERECHA;
		else if(ov.equals("E")) res= OrientacionAgenteVisto.FRENTE;
		else if(ov.equals("SE")) res= OrientacionAgenteVisto.FRENTE_IZQUIERDA;
		else if(ov.equals("S")) res= OrientacionAgenteVisto.LADO_IZQUIERDA;
		else if(ov.equals("SO")) res= OrientacionAgenteVisto.ESPALDAS_IZQUIERDA;
		break;} 
		default: {if(ov.equals("NO")) res= OrientacionAgenteVisto.ESPALDAS; 
		else if(ov.equals("N")) res= OrientacionAgenteVisto.ESPALDAS_DERECHA; 
		else if(ov.equals("NE")) res= OrientacionAgenteVisto.LADO_DERECHA;
		else if(ov.equals("E")) res= OrientacionAgenteVisto.FRENTE_DERECHA;
		else if(ov.equals("SE")) res= OrientacionAgenteVisto.FRENTE;
		else if(ov.equals("S")) res= OrientacionAgenteVisto.FRENTE_IZQUIERDA;
		else if(ov.equals("SO")) res= OrientacionAgenteVisto.LADO_IZQUIERDA;
		else if(ov.equals("O")) res= OrientacionAgenteVisto.ESPALDAS_IZQUIERDA;
		break;} //NO
		}

		return res;
	}	

	protected void onMiss() {
		objectAppearance.setMaterial(missMaterialAzul);
	}

	/*
	 * FUNCIÓN QUE CALCULA LA CLARIDAD DE PERCEPCIÓN.
	 * 
	 * Devuelve un valor entre 0 y 1 siendo 1 máxima claridad y 0 nula.
	 * d1: distancia mínima entre el objeto y el ojo para que dicho objeto pueda ser percibido.
	 * d2: distancia máxima entre el objeto y el ojo para que dicho objeto pueda ser percibido con un elevado nivel de detalles.
	 * 
	 * Las decisiones que hemos tomado han sido las siguientes:
	 * - Para que un agente perciba con una claridad del 100% a otro, el nimbus del 2º debe encontrarse dentro del focus del 1º al menos un 50%.
	 * - A medida que se va alejado la claridad va disminuyendo poco a poco hasta ser próximo a cero cuando ya no hay intersección entre focus y nimbus.
	 * - Si el nimbus de un agente se encuentra muy próximo al focus de otro (distancia<=1), la claridad de percepción es igual a esta distancia.
	 * 
	 */

	private static double claridadDePercepcion(Vector3d v1, Vector3d v2, double d1, double d2){
		double lambda= 1;		
		double sigma= 0.4;
		double resultado= 0.0;
		double d=  calculaDistancia(v1,v2);
		//Math.sqrt(Math.pow(v2.x - v1.x,2) + Math.pow(v2.y - v1.y,2) + Math.pow(v2.z - v1.z,2));
		if((0.0 <= d)&&(d <= d1))
			resultado= lambda * d;
		else if((d1 <= d)&&(d <= d2))
			resultado= 1;
		else if(d >= d2)			
			resultado= (1/(sigma * Math.sqrt(2 * Math.PI)))* Math.exp(-(Math.pow(d-d2,2)/(2*Math.pow(sigma,2))));
		return resultado;//devolvemos un valor entre 0 y 1
	}

	/*
	 * Funcion que se encarga de calcular la distancia euclídea entre dos vectores:
	 */
	private static double calculaDistancia(Vector3d v1, Vector3d v2){
		double d= Math.sqrt(Math.pow(v2.x - v1.x,2) + Math.pow(v2.y - v1.y,2) + Math.pow(v2.z - v1.z,2));
		return d;
	}

	
	//MÉTODOS GETTER Y SETTER:
	
	public void setPositionObject(Vector3d positionObject) {
		this.positionObject = positionObject;
	}

	public void setM_WakeupCondition(WakeupCondition wakeupCondition) {
		m_WakeupCondition = wakeupCondition;
	}

	public Appearance getObjectAppearance() {
		return objectAppearance;
	}

	public void setOrientacion(Orientation orientacion) {
		this.orientacion = orientacion;
	}
	
}
