package es.ucm.fdi.agents.behaviours;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import es.ucm.fdi.agents.CollisionDetectionAgent;
import es.ucm.fdi.agents.yellowPages.YellowPages;
import es.ucm.fdi.collisionDetection.InfoAgent;
import es.ucm.fdi.collisionDetection.InfoCollision;

import jade.core.AID;
import jade.core.Agent;
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
	
	private boolean activado;//TODO Esta variable inicializar a FALSE para hacer pruebas con el entorno gr�fico.

	static Logger logger = Logger.getLogger(CollisionDetectionBehaviour.class);

	public CollisionDetectionBehaviour(Agent arg0, long tiempo) {
		super(arg0, tiempo);				
		this.listaAgentes = new ArrayList<InfoAgent>();		
		paginasAmarillas= new YellowPages();
		generaCoordenadasPrimeraVez= true;
		rellenaArbol= true;
		activado= false;
	}
	
	public void onTick() {	
		
		listaAgentesGeneradores= paginasAmarillas.buscarServicio("generacion-coordenadas", myAgent);
		int numAgentes= listaAgentesGeneradores.length;
		logger.info("Numero de agentes generadores: "+ numAgentes);
		
		if(generaCoordenadasPrimeraVez && activado){
			logger.info("Mandamos mensaje 'genera'por primera vez");
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
				else if(contenidoMensaje.contains("comunicacion-lista")){ //Activamos al agente
					activado= true;
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
							logger.info("...............................................................................");
							logger.info("TAMA�O DEL ARRAYLIST: "+ic.size());
							for(int i= 0; i< ic.size(); i++){
								logger.info("SE HA PRODUCIDO UNA COLISI�N "+i);
								logger.info("nombre del agente que ve: "+ic.get(i).getAgenteQueVe());
								logger.info("nombre del agente que es visto: "+ic.get(i).getAgenteQueEsVisto());
								logger.info("Tipo de agente: "+ic.get(i).getTipoAgente());
								logger.info("Orientaci�n con la que lo ve: "+ic.get(i).getOrientacion());
								logger.info("Claridad de percepci�n: "+ic.get(i).getClaridadPercepcion());
								logger.info("Distancia: "+ic.get(i).getDistancia());
								
								//Mandamos el mensaje a cada agente que ha detectado una colision:
								ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
								
								InfoCollision contenido = ic.get(i);
								try {
									mensaje.setContentObject(contenido);
								} catch (IOException e) {									
									e.printStackTrace();
								}
								//agrega la direcci�n del destinatario:
								mensaje.addReceiver(new AID(contenido.getAgenteQueVe(),AID.ISLOCALNAME));
								//env�a el mensaje:
								myAgent.send(mensaje);
																
							}							
						}
						
						
						
						
						//Volvemos a decir a los agentes gato y rat�n que generen coordenadas:
						logger.info("Mandamos mensaje 'genera'de nuevo...");
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
						
						while(ic.size()>0)
							ic.remove(0);
						
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

	private void quitaRepetidos(ArrayList<InfoCollision> ic) {
		
		int i= 0;
		int tam= ic.size();
		int j= ic.size();
		while(i< tam){
			while(i<j){
				String agenteI= ic.get(i).getAgenteQueVe();
				String agenteJ= ic.get(j).getAgenteQueVe();
				if(agenteI.equals(agenteJ))
					ic.remove(j);
				else j--;
			}
			i++;
		}
		
	}
}
