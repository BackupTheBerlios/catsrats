package es.ucm.fdi.collisionDetection;

import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PickBounds;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionMovement;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

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
	private Material collideMaterialNegro = null;	
	private Material missMaterialAzul = null;
	private Material missMaterialBlanco = null;
	private Material collideMaterialRojo = null; //NUEVO!!
	//PARA LAS 5 CAJITAS QUE ESTARÁN DENTRO DEL CONO Y QUE DETECTARÁN LAS COLISIONES:
	private PickBounds pickBounds1 = null;
	private PickBounds pickBounds2 = null;
	private PickBounds pickBounds3 = null;
	private PickBounds pickBounds4 = null;
	private PickBounds pickBounds5 = null;
	//the current position of the object
	private Vector3d positionObject = null;	
	private Orientation orientacion;
	private Java3d j3d;
	//En esta estructura guardaremos las colisiones que se han producido,
	//entre qué agentes y cual ha sido la claridad de percepción:
	
	//public InfoColision infoColision= null;

	public J3dCollisionDetectionBehaviour(Java3d java3d, BranchGroup pickRoot, TransformGroup collisionObject, Appearance app, Vector3d positionObject, Orientation orientacion){
		//save references to the objects
		this.pickRoot = pickRoot;	
		this.collisionObject= collisionObject;
		this.objectAppearance = app;	
		this.j3d= java3d;
		
		String[] nombreObjetoClase= this.collisionObject.getName().split(" ");
		//Por como he puesto el nombre de cada agente, las posiciones 2, 4 y 6 corresponden a las coordenadas.
		/*double cX= Double.parseDouble(nombreObjetoClase[2]);
		double cY= Double.parseDouble(nombreObjetoClase[4]);
		double cZ= Double.parseDouble(nombreObjetoClase[6]);		
		this.positionObject= new Vector3d(cX,cY,cZ);
		*/
		this.nombreAgenteClase= nombreObjetoClase[0];
		this.positionObject= positionObject; 
		this.orientacion= orientacion;
		// create the WakeupCriterion for the behavior
		//WakeupCriterion criterionArray[] = new WakeupCriterion[1];
		//criterionArray[0] = new WakeupOnElapsedFrames(ELAPSED_FRAME_COUNT);
		//criterionArray[1] = new WakeupOnCollisionEntry(collisionObject);
		//criterionArray[2] = new WakeupOnCollisionMovement(collisionObject);
		
		WakeupCriterion criterionArray[] = new WakeupCriterion[1];
		criterionArray[0] = new WakeupOnCollisionMovement(collisionObject);
		//criterionArray[0] = new WakeupOnCollisionEntry(collisionObject);
		//criterionArray[1] = new WakeupOnCollisionExit(collisionObject);
		
		
		
		objectAppearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		
		this.collisionObject.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.collisionObject.setCapability(Node.ALLOW_BOUNDS_READ);
		
		// save the WakeupCriterion for the behavior
		m_WakeupCondition = new WakeupOr(criterionArray);
		
		
		
		
	}	
	
	public void initialize() {
		//apply the initial WakeupCriterion
			
		
		Color3f objColor = new Color3f(0.0f, 0.0f, 0.0f);//new Color3f(1.0f, 0.1f, 0.2f);
		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);//color negro para los conos
		collideMaterialNegro = new Material(objColor, black, objColor, black, 80.0f);
		
		objColor = new Color3f(0.0f, 0.1f, 0.8f);//color azul para los conos
		missMaterialAzul = new Material(objColor, black, objColor, black, 80.0f);
		
		objColor = new Color3f(1f, 0f, 0f);//color rojo para las esferas
		collideMaterialRojo = new Material(objColor, black, objColor, black, 80.0f);
		
		objColor = new Color3f(1f, 1f, 1f);//color blanco para las esferas
		missMaterialBlanco = new Material(objColor, black, objColor, black, 80.0f);
		
		objectAppearance.setMaterial(missMaterialAzul);
		
		wakeupOn(m_WakeupCondition);//TODO Cambio dicho por Gonzalo	
	}

	/*
	 * ESTA FUNCIÓN ES INVOCADA CADA VEZ QUE SE PRODUCE UNA COLISIÓN (ES INVOCADA
	 * TANTAS VECES COMO COLISIONES SE HAYAN PRODUCIDO EN EL ESCENARIO.
	 */
	public void processStimulus(Enumeration criteria) {
		boolean exito= false;
		//System.out.println("Posición del cono: "+ this.positionObject.x +", "+ (this.positionObject.y + 5) +", "+ this.positionObject.z);
		while (criteria.hasMoreElements()) {
			WakeupCriterion wakeUp = (WakeupCriterion) criteria.nextElement();
			
			// every N frames, check for a collision
			//if (wakeUp instanceof WakeupOnElapsedFrames) {
				// create a PickBounds
				PickTool pickTool = new PickTool(pickRoot);
				pickTool.setMode(PickTool.BOUNDS);
				PickTool pickTool2 = new PickTool(pickRoot);
				pickTool2.setMode(PickTool.BOUNDS);
				PickTool pickTool3 = new PickTool(pickRoot);
				pickTool3.setMode(PickTool.BOUNDS);
				PickTool pickTool4 = new PickTool(pickRoot);
				pickTool4.setMode(PickTool.BOUNDS);
				PickTool pickTool5 = new PickTool(pickRoot);
				pickTool5.setMode(PickTool.BOUNDS);
				
				//Caja de abajo (la mas grande):
				pickBounds1 = new PickBounds(new BoundingBox(new Point3d(positionObject.x-4, positionObject.y-5, positionObject.z-4),
						new Point3d(positionObject.x+4, positionObject.y-3, positionObject.z+4)));
				//Segunda caja
				pickBounds2 = new PickBounds(new BoundingBox(new Point3d(positionObject.x-3, positionObject.y-3, positionObject.z-3),
						new Point3d(positionObject.x+3, positionObject.y-1, positionObject.z+3)));
				//Tercera caja
				pickBounds3 = new PickBounds(new BoundingBox(new Point3d(positionObject.x-2, positionObject.y-1, positionObject.z-2),
						new Point3d(positionObject.x+2, positionObject.y+1, positionObject.z+2)));				
				//Cuarta caja
				pickBounds4 = new PickBounds(new BoundingBox(new Point3d(positionObject.x-1, positionObject.y+1, positionObject.z-1),
						new Point3d(positionObject.x+1, positionObject.y+3, positionObject.z+1)));				
				//Quinta caja
				pickBounds5 = new PickBounds(new BoundingBox(new Point3d(positionObject.x-0.5, positionObject.y+3, positionObject.z-0.5),
						new Point3d(positionObject.x+0.5, positionObject.y+4, positionObject.z+0.5)));				
				
				pickTool.setShape(pickBounds1, new Point3d(positionObject.x, positionObject.y, positionObject.z));
				pickTool2.setShape(pickBounds2, new Point3d(positionObject.x, positionObject.y, positionObject.z));
				pickTool3.setShape(pickBounds3, new Point3d(positionObject.x, positionObject.y, positionObject.z));			
				pickTool4.setShape(pickBounds4, new Point3d(positionObject.x, positionObject.y, positionObject.z));
				pickTool5.setShape(pickBounds5, new Point3d(positionObject.x, positionObject.y, positionObject.z));
				
				PickResult[] resultArray = pickTool.pickAll();// Selects all the nodes that intersect the PickShape.
				PickResult[] resultArray2 = pickTool2.pickAll();
				PickResult[] resultArray3 = pickTool3.pickAll();				
				PickResult[] resultArray4 = pickTool4.pickAll();
				PickResult[] resultArray5 = pickTool5.pickAll();
				
				//TODO No consigo que me guarde lo que tenía el arrayList infoColisiones la proxima vez que entra aquí
				InfoCollision infoAgente= isCollision(resultArray);
				if(infoAgente != null){ j3d.getInfoColisiones().add(infoAgente); exito= true;}
				else{
					infoAgente= isCollision(resultArray2);
					if(infoAgente != null){ j3d.getInfoColisiones().add(infoAgente); exito= true;}
					else{
						infoAgente= isCollision(resultArray3);
						if(infoAgente != null){ j3d.getInfoColisiones().add(infoAgente); exito= true;}
						else{
							infoAgente= isCollision(resultArray4);
							if(infoAgente != null){ j3d.getInfoColisiones().add(infoAgente); exito= true;}
							else{
								infoAgente= isCollision(resultArray5);
								if(infoAgente != null){ j3d.getInfoColisiones().add(infoAgente); exito= true;}
								else {
									onMiss();//No ha habido colisión.
									System.out.println("NO HA HABIDO COLISIÓN");
								}
							}
						}						
					}
				}
				inicializaColoresEsferas();
				if(exito) 
					cambiaColorObjetoColisionado(infoAgente.getAgenteQueEsVisto());
				/*if ((isCollision(resultArray)!= null)||(isCollision(resultArray2)!= null)||(isCollision(resultArray3)!= null)){//YA NO MIRAMOS SI ES TRUE, VEMOS QUE SEA !=NULL
					if((isCollision(resultArray)!= null)){
						Object objColision=  isCollision(resultArray);						
						//onCollide(objColision);	
					}else if((isCollision(resultArray2)!= null)){
						Object objColision2=  isCollision(resultArray2);						
						//onCollide(objColision2);	
					}else if((isCollision(resultArray3)!= null)){
						Object objColision3=  isCollision(resultArray3);
						//onCollide(objColision3);	
					}								
				}
				else{onMiss();}*/				
				
		}//fin del while
		
		// assign the next WakeUpCondition, so we are notified again
		wakeupOn(m_WakeupCondition);//TODO QUITANDO ESTA LINEA, ESTA FUNCIÓN SOLO LE EJECUTA UNA VEZ QUE ES LO QUE QUIERO!!
	}
	
	//FUNCIÓN QUE DETECTA SI HA HABIDO UNA COLISIÓN:
	//Esta función originalmente devolvia true si habia habido colisión y false e.c.c.
	//Ahora devuelve el objeto con el que ha colisionado o null si no ha colisionado con nadie.
	public InfoCollision isCollision(PickResult[] resultArray) {
	
		if (resultArray == null || resultArray.length == 0)
			return null;
		
		// we use the user data on the nodes to ignore the
		// case of the collisionObject having collided with itself!
		// the user data also gives us a good mechanism for reporting the
		// collisions.
		for (int n = 0; n < resultArray.length; n++) {
			//Object objeto= resultArray[n].getObject();
			Object userData = resultArray[n].getObject().getUserData();
			String[] nombreObjetoConElQueColisiona= userData.toString().split(" ");
			String nombreAgenteConElQueColisiona= nombreObjetoConElQueColisiona[0];
			
			if (userData != null && userData instanceof String) {	    	 
				// check that we are not colliding with ourselves...				
				//if ((!((String) userData).equals((String) collisionObject.getUserData()))&&
					if((((String)userData).contains("esfera"))&&
					   (!this.nombreAgenteClase.equals(nombreAgenteConElQueColisiona))) { //Ignora la colisión con él mismo.		
						
						//System.out.println("He chocado con: "+userData.toString());		
						//System.out.println("SE HA PRODUCIDO UNA COLISIÓN");
						//System.out.println("nombreAgenteClase------------->"+this.nombreAgenteClase);
						//System.out.println("nombreAgenteConElQueColisiona-------->"+nombreAgenteConElQueColisiona);									
						//return userData;//MODIFICAMOS LA FUNCIÓN PARA EN VEZ DE DEVOLVER TRUE DEVUELVA EL OBJETO CON EL QUE HA COLISIONADO.
						
						//TODO Las siguientes diez lineas (hasta el return) corresponden a lo que se hacía dentro de la función "onCollide":
						objectAppearance.setMaterial(collideMaterialNegro);//SE CAMBIA EL COLOR CUANDO HAY COLISION
						String[] nombreObjetoColisionado= userData.toString().split(" ");
						//Por como he puesto el nombre de cada agente, las posiciones 2, 4 y 6 corresponden a las coordenadas.
						double cX2= Double.parseDouble(nombreObjetoColisionado[2]);
						double cY2= Double.parseDouble(nombreObjetoColisionado[4]);
						double cZ2= Double.parseDouble(nombreObjetoColisionado[6]);		
						String orientacionObjetoVisto=  nombreObjetoColisionado[8];
						Vector3d v1= new Vector3d(this.positionObject.x,this.positionObject.y,this.positionObject.z);
						Vector3d v2= new Vector3d(cX2,cY2,cZ2);								
						double cp= claridadDePercepcion(v1,v2,1,5);
						//System.out.println("Claridad de percepción: "+cp);						
						
						String tipoAgente= "";
						if(nombreAgenteConElQueColisiona.contains("gato"))
							tipoAgente= "gato";
						else if(nombreAgenteConElQueColisiona.contains("raton"))
							tipoAgente= "raton";
						
						double distancia= calculaDistancia(v1, v2);
						
						OrientacionAgenteVisto orientacion= calculaOrientacion(this.orientacion, orientacionObjetoVisto);
						InfoCollision infoAgente= new InfoCollision(this.nombreAgenteClase, nombreAgenteConElQueColisiona, tipoAgente, orientacion, cp, distancia);
						
						return infoAgente;
					}
					//else System.out.println("NO HA HABIDO COLISIÓN");       
			}
			//else System.out.println("NO HA HABIDO COLISIÓN");
		}
		
		return null;
	}
	
	/**
	 * Método que se encarga de colorear las esferas con las que se colisiona.
	 * @param nombreAgenteQueEsVisto
	 */
	private void cambiaColorObjetoColisionado(String nombreAgenteQueEsVisto) {
		//String[] nombreObjetoConElQueColisiona= userData.toString().split(" ");
		//String nombreAgenteConElQueColisiona= nombreObjetoConElQueColisiona[0];

		Enumeration hijosArbol= pickRoot.getAllChildren();
		while(hijosArbol.hasMoreElements()){
			Object hijo= hijosArbol.nextElement();
			if(hijo.getClass().equals(BranchGroup.class)){
				Enumeration hijosBG= ((BranchGroup)hijo).getAllChildren();
				while(hijosBG.hasMoreElements()){
					Object hijoTG= hijosBG.nextElement();
					if(hijoTG.getClass().equals(TransformGroup.class)){
						if(((TransformGroup)hijoTG).getName().contains(nombreAgenteQueEsVisto)){							
							Enumeration hijosTG= ((TransformGroup)hijoTG).getAllChildren();
							while(hijosTG.hasMoreElements()){
								Object hijoTG2= hijosTG.nextElement();									
								if(((TransformGroup)hijoTG2).getName().contains("esfera")){
									//Es una esfera:
									TransformGroup esfera= (TransformGroup)hijoTG2;
									Enumeration hijosEsferaTG= esfera.getAllChildren();
									while(hijosEsferaTG.hasMoreElements()){
										Object hijoEsferaTG= hijosEsferaTG.nextElement();
										if(hijoEsferaTG.getClass().equals(Sphere.class)){
											Sphere esferitaDeMisAmores= (Sphere) hijoEsferaTG;
											//Appearance app= new Appearance();
											//app.setMaterial(collideMaterial);
											//esferitaDeMisAmores.setAppearance(app);
											esferitaDeMisAmores.getAppearance().setMaterial(collideMaterialRojo);
										}
									}
									
								}
							}
						}
						/*else{//Pintamos de blanco resto de esferas:
							Enumeration hijosTGb= ((TransformGroup)hijoTG).getAllChildren();
							while(hijosTGb.hasMoreElements()){
								Object hijoTG2= hijosTGb.nextElement();									
								if(((TransformGroup)hijoTG2).getName().contains("esfera")){
									//Es una esfera:
									TransformGroup esferab= (TransformGroup)hijoTG2;
									Enumeration hijosEsferaTGb= esferab.getAllChildren();
									while(hijosEsferaTGb.hasMoreElements()){
										Object hijoEsferaTGb= hijosEsferaTGb.nextElement();
										if(hijoEsferaTGb.getClass().equals(Sphere.class)){
											Sphere esferitaDeMisAmoresb= (Sphere) hijoEsferaTGb;
											//Appearance app= new Appearance();
											//app.setMaterial(collideMaterial);
											//esferitaDeMisAmores.setAppearance(app);
											esferitaDeMisAmoresb.getAppearance().setMaterial(missMaterialBlanco);
										}
									}
									
								}
							}
						}*/
					}
				}
			}
		}		

	}
	
	
	/**
	 * Método que se encarga de inicializar el color de las esferas.
	 */
	private void inicializaColoresEsferas() {
		Enumeration hijosArbol= pickRoot.getAllChildren();
		while(hijosArbol.hasMoreElements()){
			Object hijo= hijosArbol.nextElement();
			if(hijo.getClass().equals(BranchGroup.class)){
				Enumeration hijosBG= ((BranchGroup)hijo).getAllChildren();
				while(hijosBG.hasMoreElements()){
					Object hijoTG= hijosBG.nextElement();
					if(hijoTG.getClass().equals(TransformGroup.class)){
						//if(nombreAgenteQueEsVisto!= null){
							//if(!((TransformGroup)hijoTG).getName().contains(nombreAgenteQueEsVisto)){
								//Pintamos de blanco resto de esferas:
								Enumeration hijosTGb= ((TransformGroup)hijoTG).getAllChildren();
								while(hijosTGb.hasMoreElements()){
									Object hijoTG2= hijosTGb.nextElement();									
									if(((TransformGroup)hijoTG2).getName().contains("esfera") &&
										!((TransformGroup)hijoTG2).getName().contains(nombreAgenteClase)){
										//Es una esfera:
										TransformGroup esferab= (TransformGroup)hijoTG2;
										Enumeration hijosEsferaTGb= esferab.getAllChildren();
										while(hijosEsferaTGb.hasMoreElements()){
											Object hijoEsferaTGb= hijosEsferaTGb.nextElement();
											if(hijoEsferaTGb.getClass().equals(Sphere.class)){
												Sphere esferitaDeMisAmoresb= (Sphere) hijoEsferaTGb;
												esferitaDeMisAmoresb.getAppearance().setMaterial(missMaterialBlanco);
											}
										}
									}
								}
							//}
						//}
					}
				}
			}
		}		

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

	/*
	//FUNCIÓN A LA QUE SE LLAMA CUANDO SE HA PRODUCIDO UNA COLISIÓN:	
	protected void onCollide(Object objColision) {		
		objectAppearance.setMaterial(collideMaterial);//SE CAMBIA EL COLOR CUANDO HAY COLISION
		//Miramos que haya colisionado con una esfera y que esta no sea la suya:		
		//if((objColision.toString().contains("esfera"))&&(!objColision.toString().contains(this.collisionObject.getName()))){				
				
				String[] nombreObjetoColisionado= objColision.toString().split(" ");
				//Por como he puesto el nombre de cada agente, las posiciones 2, 4 y 6 corresponden a las coordenadas.
				double cX2= Double.parseDouble(nombreObjetoColisionado[2]);
				double cY2= Double.parseDouble(nombreObjetoColisionado[4]);
				double cZ2= Double.parseDouble(nombreObjetoColisionado[6]);
				
				Vector3d v1= new Vector3d(this.positionObject.x,this.positionObject.y,this.positionObject.z);
				Vector3d v2= new Vector3d(cX2,cY2,cZ2);
				
				//v1.setY(this.positionVector.getY()+2.5);
				//Vector3d v2= new Vector3d(-5,0,0);//esfera
				double resultado= claridadDePercepcion(v1,v2,1,5);
				System.out.println("Claridad de percepción (entre 0 y 100): "+resultado);
					
		//}
	//	else{}		
	}*/
	
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
