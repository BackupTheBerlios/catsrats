package es.ucm.fdi.collisionDetection;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
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
		//BranchGroup objRoot = new BranchGroup();
		
		Bounds lightBounds = getApplicationBounds();
		
		AmbientLight ambLight = new AmbientLight(true, new Color3f(1.0f, 1.0f,
				1.0f));
		ambLight.setInfluencingBounds(lightBounds);
		objRoot.addChild(ambLight);
		
		DirectionalLight headLight = new DirectionalLight();
		headLight.setInfluencingBounds(lightBounds);
		objRoot.addChild(headLight);
		
		//this.objRoot= objRoot;
		
		//rellenaArbol(objRoot,agentes);
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
				tgAgente.setName(agentes.get(i).getNombreAgente()+" x "+agentes.get(i).getX()+" y "+agentes.get(i).getY()+" z "+agentes.get(i).getZ());
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
			//System.out.println(hijo.getClass());
			//System.out.println(BranchGroup.class);
			if(hijo.getClass().equals(BranchGroup.class)){
				Enumeration hijosBG= ((BranchGroup)hijo).getAllChildren();
				while(hijosBG.hasMoreElements()){
					Object hijoTG= hijosBG.nextElement();
					//System.out.println(hijoTG.getClass());
					//System.out.println(TransformGroup.class);					 				
					if(hijoTG.getClass().equals(TransformGroup.class)){
						for(int i= 0; i< listaAgentes.size(); i++){
							InfoAgent agenteI= listaAgentes.get(i);
							if(((TransformGroup)hijoTG).getName().contains(agenteI.getNombreAgente())){
								((TransformGroup)hijoTG).setName(agenteI.getNombreAgente()+" x "+agenteI.getX()+" y "+agenteI.getY()+" z "+agenteI.getZ());
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
	private void orientaCono(Orientation orientacion, Transform3D t3d){
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
		
		sphereTg.addChild(new Sphere(5,app));//TODO Radio de la esfera es 5
		tgAgente.addChild(sphereTg);		
		recursiveSetUserData(sphereTg, sphereTg.getName());
	}
	
	private void updateEsfera(double x, double y, double z, TransformGroup sphereTg){
		//Appearance app = new Appearance();
		//TransformGroup sphereTg = new TransformGroup();
		//sphereTg.setName(tgAgente.getName()+" esfera");
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(x, y, z));
		sphereTg.setTransform(t3d);
		
		//sphereTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//sphereTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		//sphereTg.addChild(new Sphere(5,app));//TODO Radio de la esfera es 5
		//tgAgente.addChild(sphereTg);		
		//recursiveSetUserData(sphereTg, sphereTg.getName());
	}
	
	private void añadeCirculoPequeñoDelCono(double x, double y, double z, TransformGroup tgAgente, Orientation orientacion){
		Appearance app = new Appearance();
		TransformGroup sphereTg = new TransformGroup();
		sphereTg.setName(tgAgente.getName()+" circulito");
		Transform3D t3d = new Transform3D();
		
		//t3d.set(new Vector3d(x,y,z));
		
		//¡¡PRIMERO HACER LA ROTACIÓN Y DESPUÉS LA TRASLACIÓN, SINO NO HACE LA TRASLACIÓN!!
		orientaCono(orientacion,t3d);//FUNCIÓN QUE SE ENCARGA DE ORIENTAR EL CONO.
		t3d.setTranslation(new Vector3d(x, y, z));
		//t3d.rotZ(0); //Parámetro en radianes.		
		//t3d.setRotation(new AxisAngle4d(0.0,0.0,1.0,Math.PI/2));
		//System.out.println("Coordenadas de la punta del cono (circulito): x " + x + " ,y " + y + " ,z "+ z);
		//Vector3d coordenadas= new Vector3d(x, y, z);
		//t3d.get(coordenadas);
		//System.out.println("Coordenadas de la punta del cono (circulito).......: x " + coordenadas.x + " ,y " + coordenadas.y + " ,z "+ coordenadas.z);
		
		sphereTg.setTransform(t3d);
		
		sphereTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphereTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		sphereTg.addChild(new Sphere(1,app));//he cambiado el 1 por 0 y ya no pinta ni detecta el circulo pequeño-->OK!		
		recursiveSetUserData(sphereTg, sphereTg.getName());		
		//this.addCone(bg, sphereTg, x, y-2.5, z, incVector, "CONO", 2, 5);//5 es la altura del cono, y 2 el radio.		
		
		añadeCono(x,y,z,sphereTg);//AÑADIMOS EL CONO AL CIRCULO PEQUEÑO
		tgAgente.addChild(sphereTg);//AÑADIMOS EL CONO AL AGENTE 
	}
	
	private void updateCirculoPequeñoDelCono(double x, double y, double z, TransformGroup circulitoTg, Orientation orientacion){
		Transform3D t3d = new Transform3D();
		
		//¡¡PRIMERO HACER LA ROTACIÓN Y DESPUÉS LA TRASLACIÓN, SINO NO HACE LA TRASLACIÓN!!
		orientaCono(orientacion,t3d);//FUNCIÓN QUE SE ENCARGA DE ORIENTAR EL CONO.
		t3d.setTranslation(new Vector3d(x, y, z));
		
		circulitoTg.setTransform(t3d);		
		
		updateCono(x,y,z,circulitoTg);//MODIFICAMOS EL CONO AL CIRCULO PEQUEÑO
		//tgAgente.addChild(sphereTg);//AÑADIMOS EL CONO AL AGENTE 
	}	

	private void añadeCono(double x, double y, double z, TransformGroup circulito){
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
		J3dCollisionDetectionBehaviour comportamiento= new J3dCollisionDetectionBehaviour(this, objRoot, circulito, app, new Vector3d(x, y, z));
		//CollisionBehavior collisionBehavior = new CollisionBehavior(bg, sphereTg, app, new Vector3d(x, y, z), incVector);
		comportamiento.setSchedulingBounds(getApplicationBounds());// Pone la región de planificación del comportamiento a los límites especificados.
		
		coneTg.addChild(comportamiento);	
	}	
	
	private void updateCono(double x, double y, double z, TransformGroup circulitoTg) {
		Enumeration hijosCirculitoTg= ((TransformGroup)circulitoTg).getAllChildren();
		while(hijosCirculitoTg.hasMoreElements()){
			Object coneTg= hijosCirculitoTg.nextElement();
			//System.out.println(coneTg.getClass());
			//System.out.println(TransformGroup.class);
			if(coneTg.getClass().equals(TransformGroup.class)){
				((TransformGroup)coneTg).setName(circulitoTg.getName()+" cono");
				Enumeration hijosTg= ((TransformGroup)coneTg).getAllChildren();
				while(hijosTg.hasMoreElements()){
					Object hijoTg= hijosTg.nextElement();
					//System.out.println(hijoTg.getClass());
					//System.out.println(J3dCollisionDetectionBehaviour.class);					 				
					if(hijoTg.getClass().equals(J3dCollisionDetectionBehaviour.class)){						
						J3dCollisionDetectionBehaviour comportamiento= (J3dCollisionDetectionBehaviour)hijoTg;
						comportamiento.setPositionObject(new Vector3d(x,y,z));
						// create the WakeupCriterion for the behavior
						//WakeupCriterion criterionArray[] = new WakeupCriterion[3];
						//criterionArray[0] = new WakeupOnElapsedFrames(J3dCollisionDetectionBehaviour.ELAPSED_FRAME_COUNT);
						//criterionArray[0] = new WakeupOnCollisionMovement(circulitoTg);
						//criterionArray[1] = new WakeupOnCollisionEntry(circulitoTg);
						//criterionArray[2] = new WakeupOnCollisionExit(circulitoTg);
						
						//comportamiento.getObjectAppearance().setCapability(Appearance.ALLOW_MATERIAL_WRITE);
						
						//circulitoTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
						//circulitoTg.setCapability(Node.ALLOW_BOUNDS_READ);
						
						//comportamiento.setM_WakeupCondition(new WakeupOr(criterionArray));
						
						comportamiento.setSchedulingBounds(getApplicationBounds());
						
						/*J3dCollisionDetectionBehaviour comportamiento= (J3dCollisionDetectionBehaviour)hijoTg;
						//((TransformGroup)coneTg).removeChild(1);
						
						J3dCollisionDetectionBehaviour comportamientoNuevo= new J3dCollisionDetectionBehaviour(this, objRoot, circulitoTg, new Vector3d(x, y, z));
						comportamientoNuevo.setSchedulingBounds(getApplicationBounds());						
						((TransformGroup)coneTg).setChild(comportamientoNuevo,1);*/
						//Appearance app= new Appearance();
						//Java3d j3d= comportamiento.getJ3d();
						//J3dCollisionDetectionBehaviour comportamiento2= new J3dCollisionDetectionBehaviour(j3d, objRoot, conoTg, app, new Vector3d(x,y,z));
						//comportamiento2.setSchedulingBounds(getApplicationBounds());// Pone la región de planificación del comportamiento a los límites especificados.
						//comportamiento= comportamiento2;
						
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
