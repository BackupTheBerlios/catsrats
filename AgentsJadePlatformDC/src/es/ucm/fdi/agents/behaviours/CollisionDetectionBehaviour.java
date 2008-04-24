package es.ucm.fdi.agents.behaviours;

import java.util.ArrayList;

import com.sun.j3d.utils.applet.MainFrame;

import es.ucm.fdi.agents.CollisionDetectionAgent;
import es.ucm.fdi.agents.yellowPages.YellowPages;
import es.ucm.fdi.collisionDetection.InfoAgent;
import es.ucm.fdi.collisionDetection.InfoCollision;
import es.ucm.fdi.collisionDetection.Java3d;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class CollisionDetectionBehaviour extends TickerBehaviour{

	//the branch that we check for collisions
	//private BranchGroup pickRoot = null;

	//how often we check for a collision
	//private static final int ELAPSED_FRAME_COUNT = 1;

	//protected VirtualUniverse m_Universe = null;

	private ArrayList<InfoAgent> listaAgentes;	
	private YellowPages paginasAmarillas;
	private AID[] listaAgentesGeneradores;
	
	
	private boolean generaCoordenadasPrimeraVez;
	private boolean rellenaArbol;


	public CollisionDetectionBehaviour(Agent arg0, long tiempo) {
		super(arg0, tiempo);				
		this.listaAgentes = new ArrayList<InfoAgent>();		
		paginasAmarillas= new YellowPages();
		generaCoordenadasPrimeraVez= true;
		rellenaArbol= true;
	}
	
	public void onTick() {	
		
		listaAgentesGeneradores= paginasAmarillas.buscarServicio("generacion-coordenadas", myAgent);
		int numAgentes= listaAgentesGeneradores.length;
		System.out.println("Numero de agentes generadores: "+ numAgentes);
		
		if(generaCoordenadasPrimeraVez){
			System.out.println("Mandamos mensaje 'genera'por primera vez");
			ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
			//agrega contenido: generar coordenadas para la primera vez
			String contenido = "genera";
			mensaje.setContent(contenido);
			for(int i = 0; i< numAgentes; i++){
				String localName = listaAgentesGeneradores[i].getLocalName();
				mensaje.addReceiver(new AID(localName,AID.ISLOCALNAME));
				myAgent.send(mensaje);					
			}	
			generaCoordenadasPrimeraVez= false;
		}
		
		//Recibir el mensaje de activaci�n o morir:
		ACLMessage mensajeEntrante = myAgent.receive();
		if(mensajeEntrante != null){
			if(mensajeEntrante.getPerformative()==ACLMessage.INFORM){ //Aqui tratamos los mensajes string de "activacion" y "morir" enviados por el servidor
				String contenidoMensaje = mensajeEntrante.getContent();
				if(contenidoMensaje.contains("morir")){ //Matamos a los agentes
					myAgent.doDelete();
				}				
			}
			else if(mensajeEntrante.getPerformative()==ACLMessage.REQUEST){  //Aqui tratamos los mensajes objeto InfoAgent enviados por gato y rat�n
				try {
					InfoAgent info = (InfoAgent) mensajeEntrante.getContentObject();
					if(this.listaAgentes.size()< numAgentes)
						this.listaAgentes.add(info);

					if(this.listaAgentes.size()== numAgentes){
						if(rellenaArbol){//Es necesario crear la estructura del arbol al menos una vez:
							((CollisionDetectionAgent)myAgent).getJ3d().rellenaArbol(listaAgentes);
							rellenaArbol= false;
						}							
						else
							((CollisionDetectionAgent)myAgent).getJ3d().updateArbol(listaAgentes);

						
						ArrayList<InfoCollision> ic= ((CollisionDetectionAgent)myAgent).getJ3d().getInfoColisiones();//((CollisionDetectionAgent) myAgent).getIc();
						//J3dCollisionDetectionBehaviour comportamiento= new J3dCollisionDetectionBehaviour(pickRoot, circulito, new Vector3d(x, y, z));
						//detectaColisiones();
						//((CollisionDetectionAgent) myAgent).setIc(((CollisionDetectionAgent) myAgent).getJ3d().infoColisiones);
						
						if(ic!= null){
							for(int i= 0; i< ic.size(); i++){
								System.out.println("SE HA PRODUCIDO UNA COLISI�N");
								System.out.println("nombre del agente que ve: "+ic.get(i).getAgenteQueVe());
								System.out.println("nombre del agente que es visto: "+ic.get(i).getAgenteQueEsVisto());
								System.out.println("Claridad de percepci�n: "+ic.get(i).getClaridadPercepcion());
							}
						}
						
						//Volvemos a decir a los agentes gato y rat�n que generen coordenadas:
						System.out.println("Mandamos mensaje 'genera'de nuevo...");
						ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
						//agrega contenido: generar coordenadas para la primera vez
						String contenido = "genera";
						mensaje.setContent(contenido);
						for(int i = 0; i< numAgentes; i++){
							String localName = listaAgentesGeneradores[i].getLocalName();
							mensaje.addReceiver(new AID(localName,AID.ISLOCALNAME));
							myAgent.send(mensaje);					
						}
						
						//Limpiamos la lista de infoAgent para tratar el nuevo caso:												
						while(this.listaAgentes.size()>0)
								this.listaAgentes.remove(0);
						
					}

				} catch (UnreadableException e) {
					e.printStackTrace();
				}				
			}				
		}		
		else {  //Permanecemos a la espera de que llegue el mensaje de activacion
			block();
		}
	}

	/*private void detectaColisiones(){
		System.out.println("Dentro del m�todo onTick");
		Enumeration agentes= this.pickRoot.getAllChildren();
		while(agentes.hasMoreElements()){
			Object agente= agentes.nextElement();			 
			if((agente.getClass().getName().contains("gato"))||(agente.getClass().getName().contains("raton"))){
				Appearance app = new Appearance();
				//J3dCollisionDetectionBehaviour comportamiento= new J3dCollisionDetectionBehaviour(pickRoot, (TransformGroup) agente, app);
				//comportamiento.setSchedulingBounds(j3d.getApplicationBounds());// Pone la regi�n de planificaci�n del comportamiento a los l�mites especificados.
			}
		}

	}	
	 */

//	public CollisionDetectionAgent(ArrayList<Agente> agentes){
	//save references to the objects
	//this.pickRoot = new BranchGroup();	
//	}	



}
