package es.ucm.fdi.agents.behaviours;

import java.util.ArrayList;

import com.sun.j3d.utils.applet.MainFrame;
import es.ucm.fdi.agents.yellowPages.YellowPages;
import es.ucm.fdi.collisionDetection.InfoAgent;
import es.ucm.fdi.collisionDetection.InfoCollision;
import es.ucm.fdi.collisionDetection.Java3d;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class CollisionDetectionBehaviour extends OneShotBehaviour{

	//the branch that we check for collisions
	//private BranchGroup pickRoot = null;

	//how often we check for a collision
	//private static final int ELAPSED_FRAME_COUNT = 1;

	//protected VirtualUniverse m_Universe = null;

	private ArrayList<InfoAgent> listaAgentes;	
	private YellowPages paginasAmarillas;
	private AID[] listaAgentesGeneradores;
	private Java3d j3d;
	
	private static int m_kWidth = 400;	
	private static int m_kHeight = 400;
	
	private boolean generaCoordenadasPrimeraVez;


	public CollisionDetectionBehaviour(Agent arg0) {
		super(arg0);				
		this.listaAgentes = new ArrayList<InfoAgent>();		
		paginasAmarillas= new YellowPages();
		j3d= null;
		generaCoordenadasPrimeraVez= true;
	}

	public void action() {			

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
		
		//Recibir el mensaje de activación o morir:
		ACLMessage mensajeEntrante = myAgent.receive();
		if(mensajeEntrante != null){
			if(mensajeEntrante.getPerformative()==ACLMessage.INFORM){ //Aqui tratamos los mensajes string de "activacion" y "morir" enviados por el servidor
				String contenidoMensaje = mensajeEntrante.getContent();
				if(contenidoMensaje.contains("morir")){ //Matamos a los agentes
					myAgent.doDelete();
				}				
			}
			else if(mensajeEntrante.getPerformative()==ACLMessage.REQUEST){  //Aqui tratamos los mensajes objeto InfoAgent enviados por gato y ratón
				try {
					InfoAgent info = (InfoAgent) mensajeEntrante.getContentObject();
					if(this.listaAgentes.size()< numAgentes)
						this.listaAgentes.add(info);

					if(this.listaAgentes.size()== numAgentes){
						j3d= new Java3d(listaAgentes);
						//j3d.createSceneBranchGroup();
						j3d.initJava3d();
						
						MainFrame v= new MainFrame(j3d, m_kWidth, m_kHeight);

						ArrayList<InfoCollision> ic= j3d.infoColisiones;//((CollisionDetectionAgent) myAgent).getIc();
						//J3dCollisionDetectionBehaviour comportamiento= new J3dCollisionDetectionBehaviour(pickRoot, circulito, new Vector3d(x, y, z));
						//detectaColisiones();
						//((CollisionDetectionAgent) myAgent).setIc(((CollisionDetectionAgent) myAgent).getJ3d().infoColisiones);

						for(int i= 0; i< ic.size(); i++){
							System.out.println("SE HA PRODUCIDO UNA COLISIÓN");
							System.out.println("nombre del agente que ve: "+ic.get(i).getAgenteQueVe());
							System.out.println("nombre del agente que es visto: "+ic.get(i).getAgenteQueEsVisto());
							System.out.println("Claridad de percepción: "+ic.get(i).getClaridadPercepcion());
						}
						
						//Volvemos a decir a los agentes gato y ratón que generen coordenadas:
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

	public Java3d getJ3d() {
		return j3d;
	}

	/*private void detectaColisiones(){
		System.out.println("Dentro del método onTick");
		Enumeration agentes= this.pickRoot.getAllChildren();
		while(agentes.hasMoreElements()){
			Object agente= agentes.nextElement();			 
			if((agente.getClass().getName().contains("gato"))||(agente.getClass().getName().contains("raton"))){
				Appearance app = new Appearance();
				//J3dCollisionDetectionBehaviour comportamiento= new J3dCollisionDetectionBehaviour(pickRoot, (TransformGroup) agente, app);
				//comportamiento.setSchedulingBounds(j3d.getApplicationBounds());// Pone la región de planificación del comportamiento a los límites especificados.
			}
		}

	}	
	 */

//	public CollisionDetectionAgent(ArrayList<Agente> agentes){
	//save references to the objects
	//this.pickRoot = new BranchGroup();	
//	}	



}
