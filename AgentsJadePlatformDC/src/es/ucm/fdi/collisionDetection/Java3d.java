package es.ucm.fdi.collisionDetection;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOnCollisionMovement;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Sphere;


public class Java3d extends Java3dApplet{
	private ArrayList<InfoAgent> agentes;
	private ArrayList<InfoCollision> infoColisiones;
	
	BranchGroup objRoot;

	public Java3d(ArrayList<InfoAgent> agentes) {
		super();		
		this.agentes= agentes;
		infoColisiones= new ArrayList<InfoCollision>(); 
		this.objRoot= new BranchGroup();
		objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		objRoot.setCapability(Group.ALLOW_CHILDREN_READ);
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
	}
	
	protected BranchGroup createSceneBranchGroup() {
			
		Bounds lightBounds = getApplicationBounds();
		
		AmbientLight ambLight = new AmbientLight(true, new Color3f(1.0f, 1.0f,
				1.0f));
		ambLight.setInfluencingBounds(lightBounds);
		objRoot.addChild(ambLight);
		
		DirectionalLight headLight = new DirectionalLight();
		headLight.setInfluencingBounds(lightBounds);
		objRoot.addChild(headLight);
		
		rellenaArbol(agentes);
		
		return objRoot;
	}
	
	
	/*
	 * El nombre del agente está compuesto por la siguiente información:
	 * "nombre del agente" x "coordenada x" y "coordenada y" z "coordenada z"
	 */
	public void rellenaArbol(ArrayList<InfoAgent> agentes){
		if (agentes != null){
			for(int i= 0; i< agentes.size(); i++){
				TransformGroup tgAgente= new TransformGroup();
				tgAgente.setCollidable(true);
				tgAgente.setPickable(true);
				tgAgente.setName(agentes.get(i).getNombreAgente()+" x "+agentes.get(i).getX()+" y "+agentes.get(i).getY()+" z "+agentes.get(i).getZ()+" orientacion "+agentes.get(i).getOrientacion());
				System.out.println(tgAgente.getName()+" ; Orientación(null si no es gato ni ratón): "+agentes.get(i).getOrientacion());//Imprimimos el nombre del agente.
				if(agentes.get(i).getNombreAgente().contains("gato")||
						agentes.get(i).getNombreAgente().contains("raton")){
					//añadimos el cono:				
					Vector3d nuevasCoord= cambiaCoordendas(agentes.get(i).getOrientacion(),agentes.get(i).getX(),agentes.get(i).getY(),agentes.get(i).getZ());							
					añadeCirculoPequeñoDelCono(nuevasCoord.x, nuevasCoord.y, nuevasCoord.z, tgAgente, agentes.get(i).getOrientacion());
					System.out.println("Añadido CIRCULITO del agente: "+agentes.get(i).getNombreAgente());
				}
				//añadimos la esfera:
				añadeEsfera(agentes.get(i).getX(), agentes.get(i).getY(), agentes.get(i).getZ(), tgAgente);
				System.out.println("Añadida ESFERA del agente: "+agentes.get(i).getNombreAgente());
				BranchGroup bg = new BranchGroup();
				bg.addChild(tgAgente);
				
				objRoot.addChild(bg);
				
			}		
		}
	}
	/*
	 * Esta función se encarga de actualizar el arbol:
	 */
	public void updateArbol(ArrayList<InfoAgent> listaAgentes) {		
		Enumeration hijosArbol= objRoot.getAllChildren();
		while(hijosArbol.hasMoreElements()){
			Object hijo= hijosArbol.nextElement();
			if(hijo.getClass().equals(BranchGroup.class)){
				Enumeration hijosBG= ((BranchGroup)hijo).getAllChildren();
				while(hijosBG.hasMoreElements()){
					Object hijoTG= hijosBG.nextElement();
					if(hijoTG.getClass().equals(TransformGroup.class)){
						for(int i= 0; i< listaAgentes.size(); i++){
							InfoAgent agenteI= listaAgentes.get(i);
							if(((TransformGroup)hijoTG).getName().contains(agenteI.getNombreAgente())){
								((TransformGroup)hijoTG).setName(agenteI.getNombreAgente()+" x "+agenteI.getX()+" y "+agenteI.getY()+" z "+agenteI.getZ()+" orientacion "+agenteI.getOrientacion());
								Enumeration hijosTG= ((TransformGroup)hijoTG).getAllChildren();
								while(hijosTG.hasMoreElements()){
									Object hijoTG2= hijosTG.nextElement();
									if(agenteI.getNombreAgente().contains("gato")||
									   agenteI.getNombreAgente().contains("raton")){								
										if(((TransformGroup)hijoTG2).getName().contains("circulito")){
										//Es un circulito:											
											TransformGroup circulito= (TransformGroup)hijoTG2;
											circulito.setName(((TransformGroup)hijoTG).getName()+" circulito");
											Vector3d nuevasCoord= cambiaCoordendas(agenteI.getOrientacion(),agenteI.getX(), agenteI.getY(), agenteI.getZ());
											this.updateCirculoPequeñoDelCono(nuevasCoord.x, nuevasCoord.y, nuevasCoord.z, circulito, agenteI.getOrientacion());
											System.out.println("Realizado update del CIRCULITO del agente: "+agenteI.getNombreAgente());
										}
									}
									if(((TransformGroup)hijoTG2).getName().contains("esfera")){
									//Es una esfera:
										TransformGroup esfera= (TransformGroup)hijoTG2;
										esfera.setName(((TransformGroup)hijoTG).getName()+" esfera");
										this.updateEsfera(agenteI.getX(), agenteI.getY(), agenteI.getZ(), esfera);
										System.out.println("Realizado update de la ESFERA del agente: "+agenteI.getNombreAgente());
									}
								}
							}
						}
					}
				}
			}
		}		
	}
	
	/*
	 * Esta función se encarga de cambiar las coordenadas con respecto a la orientación del cono, ya que el circulito se crea 
	 * en el centro del cono y dependiendo de la orientación, para que las coodenadas sigan con respecto a la punta del cono, 
	 * hay que modificar el valor de las coordenadas.
	 * 
	 * Cuando la orientación del cono es diagonal, queremos que las nuevas coordenas estén a una distancia de 5. 
	 * Esto equivale a calcular el valor de los catetos para que la hipotenusa sea igual a 5.
	 * h^2= cat^2 + cat^2 => 5^2= 2* cat^2 => 25/2 = cat^2 => 12.5 = cat^2 => cat= raiz cuadrada de 12.5
	 */
	private Vector3d cambiaCoordendas(Orientation orientacion, double cx, double cy, double cz){
		Vector3d res= new Vector3d(cx,cy,cz);
		switch(orientacion){
			case N: res.y= res.y+5; break;//Parámetro en radianes.
			case NE: res.x= res.x+Math.sqrt(12.5); res.y= res.y+Math.sqrt(12.5); break;
			case E: res.x= res.x+5; break;
			case SE: res.x= res.x+Math.sqrt(12.5); res.y= res.y-Math.sqrt(12.5); break;
			case S: res.y= res.y-5; break;
			case SO: res.x= res.x-Math.sqrt(12.5); res.y= res.y-Math.sqrt(12.5); break;
			case O: res.x= res.x-5; break;
			default: res.x= res.x-Math.sqrt(12.5); res.y= res.y+Math.sqrt(12.5); break;//NO
		}
		return res;
	}
	
	/*
	 * Esta función se encarga de dada la orientación que debe tener el cono, situarlo en esa orientación.
	 */
	public void orientaCono(Orientation orientacion, Transform3D t3d){
		switch(orientacion){
			case N: t3d.rotZ(Math.PI); break;//Parámetro en radianes.
			case NE: t3d.rotZ(3*Math.PI/4); break;
			case E: t3d.rotZ(Math.PI/2); break;
			case SE: t3d.rotZ(Math.PI/4); break;
			case S: t3d.rotZ(0); break;
			case SO: t3d.rotZ(7*Math.PI/4); break;
			case O: t3d.rotZ(3*Math.PI/2); break;
			default: t3d.rotZ(5*Math.PI/4); break;//NO
		}
		
	}
	
	private void añadeEsfera(double x, double y, double z, TransformGroup tgAgente){
		Appearance app = new Appearance();
		TransformGroup sphereTg = new TransformGroup();
		sphereTg.setName(tgAgente.getName()+" esfera");
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(x, y, z));
		sphereTg.setTransform(t3d);
		
		sphereTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphereTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
						
		sphereTg.addChild(new Sphere(5,app));//Radio de la esfera es 5
		tgAgente.addChild(sphereTg);		
		recursiveSetUserData(sphereTg, sphereTg.getName());
	}
	
	private void updateEsfera(double x, double y, double z, TransformGroup sphereTg){
		
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(x, y, z));
		sphereTg.setTransform(t3d);		
	}
	
	private void añadeCirculoPequeñoDelCono(double x, double y, double z, TransformGroup tgAgente, Orientation orientacion){
		Appearance app = new Appearance();
		TransformGroup sphereTg = new TransformGroup();
		sphereTg.setCollidable(true);
		sphereTg.setPickable(true);
		sphereTg.setName(tgAgente.getName()+" circulito");
		Transform3D t3d = new Transform3D();
		
		
		//¡¡PRIMERO HACER LA ROTACIÓN Y DESPUÉS LA TRASLACIÓN, SINO NO HACE LA TRASLACIÓN!!
		orientaCono(orientacion,t3d);//FUNCIÓN QUE SE ENCARGA DE ORIENTAR EL CONO.
		t3d.setTranslation(new Vector3d(x, y, z));
		
		sphereTg.setTransform(t3d);
		
		sphereTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphereTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		sphereTg.addChild(new Sphere(1,app));//he cambiado el 1 por 0 y ya no pinta ni detecta el circulo pequeño-->OK!		
		recursiveSetUserData(sphereTg, sphereTg.getName());		
				
		añadeCono(x,y,z,orientacion,sphereTg);//AÑADIMOS EL CONO AL CIRCULO PEQUEÑO
		tgAgente.addChild(sphereTg);//AÑADIMOS EL CONO AL AGENTE 
	}
	
	private void updateCirculoPequeñoDelCono(double x, double y, double z, TransformGroup circulitoTg, Orientation orientacion){
		Transform3D t3d = new Transform3D();
		
		//¡¡PRIMERO HACER LA ROTACIÓN Y DESPUÉS LA TRASLACIÓN, SINO NO HACE LA TRASLACIÓN!!
		orientaCono(orientacion,t3d);//FUNCIÓN QUE SE ENCARGA DE ORIENTAR EL CONO.
		t3d.setTranslation(new Vector3d(x, y, z));
		
		circulitoTg.setTransform(t3d);		
		
		updateCono(x,y,z,orientacion,circulitoTg);//MODIFICAMOS EL CONO AL CIRCULO PEQUEÑO		 
	}	

	private void añadeCono(double x, double y, double z, Orientation orientacion, TransformGroup circulito){
		float radio= 5;
		float altura= 10;
		Appearance app = new Appearance();
		Cone cone= new Cone(radio, altura, app);
				
		TransformGroup coneTg = new TransformGroup();
		coneTg.setName(circulito.getName()+" cono");
				
		coneTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);//Specifies that the node allows writing its object's transform information.		 
		coneTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);//Specifies that the node allows access to its object's transform information.
	
		coneTg.addChild(cone);
		circulito.addChild(coneTg);
		recursiveSetUserData(coneTg, coneTg.getName());
		
		// create the collision behaviour:
		J3dCollisionDetectionBehaviour comportamiento= new J3dCollisionDetectionBehaviour(this, objRoot, circulito, app, new Vector3d(x, y, z), orientacion);
				
		comportamiento.setSchedulingBounds(getApplicationBounds());// Pone la región de planificación del comportamiento a los límites especificados.		
		coneTg.addChild(comportamiento);
		
	}	
	
	private void updateCono(double x, double y, double z, Orientation orientacion, TransformGroup circulitoTg) {
		J3dCollisionDetectionBehaviour comportamiento= null;
		Enumeration hijosCirculitoTg= ((TransformGroup)circulitoTg).getAllChildren();
		while(hijosCirculitoTg.hasMoreElements()){
			Object objetoTg= hijosCirculitoTg.nextElement();
			if(objetoTg.getClass().equals(TransformGroup.class)){
				if(((TransformGroup)objetoTg).getName().contains("cono")){
					((TransformGroup)objetoTg).setName(circulitoTg.getName()+" cono");
					Enumeration hijosTg= ((TransformGroup)objetoTg).getAllChildren();
					while(hijosTg.hasMoreElements()){
						Object hijoTg= hijosTg.nextElement();
						if(hijoTg.getClass().equals(J3dCollisionDetectionBehaviour.class)){						
							comportamiento= (J3dCollisionDetectionBehaviour)hijoTg;
							comportamiento.setPositionObject(new Vector3d(x,y,z));
							comportamiento.setOrientacion(orientacion);
							
							comportamiento.setSchedulingBounds(getApplicationBounds());
						}						
					}
				}			
			}			
		}				
	}
	
	//method to recursively set the user data for objects in the scenegraph
	// tree
	// we also set the capabilites on Shape3D and Morph objects required by the
	// PickTool
	void recursiveSetUserData(SceneGraphObject root, Object value) {
		root.setUserData(value);
		
		// recursively process group
		if (root instanceof Group) {
			Group g = (Group) root;
			
			// recurse on child nodes
			java.util.Enumeration enumKids = g.getAllChildren();
			
			while (enumKids.hasMoreElements() != false)
				recursiveSetUserData((SceneGraphObject) enumKids.nextElement(), value);
		}
	}

	public ArrayList<InfoCollision> getInfoColisiones() {
		return infoColisiones;
	}
	
}
