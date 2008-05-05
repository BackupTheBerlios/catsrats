package es.ucm.fdi.agents.behaviours;

import java.io.IOException;
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
	
	private boolean activado;//TODO Esta variable inicializar a FALSE para hacer pruebas con el entorno gráfico.


	public CollisionDetectionBehaviour(Agent arg0, long tiempo) {
		super(arg0, tiempo);				
		this.listaAgentes = new ArrayList<InfoAgent>();		
		paginasAmarillas= new YellowPages();
		generaCoordenadasPrimeraVez= true;
		rellenaArbol= true;
		activado= true;
	}
	
	public void onTick() {	
		
		listaAgentesGeneradores= paginasAmarillas.buscarServicio("generacion-coordenadas", myAgent);
		int numAgentes= listaAgentesGeneradores.length;
		System.out.println("Numero de agentes generadores: "+ numAgentes);
		
		if(generaCoordenadasPrimeraVez && activado){
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
				else if(contenidoMensaje.contains("comunicacion-lista")){ //Activamos al agente
					activado= true;
				}
			}
			else if(mensajeEntrante.getPerformative()==ACLMessage.REQUEST){  //Aqui tratamos los mensajes objeto InfoAgent enviados por gato y ratón
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
							System.out.println("...............................................................................");
							System.out.println("TAMAÑO DEL ARRAYLIST: "+ic.size());
							for(int i= 0; i< ic.size(); i++){
								System.out.println("SE HA PRODUCIDO UNA COLISIÓN "+i);
								System.out.println("nombre del agente que ve: "+ic.get(i).getAgenteQueVe());
								System.out.println("nombre del agente que es visto: "+ic.get(i).getAgenteQueEsVisto());
								System.out.println("Tipo de agente: "+ic.get(i).getTipoAgente());
								System.out.println("Orientación con la que lo ve: "+ic.get(i).getOrientacion());
								System.out.println("Claridad de percepción: "+ic.get(i).getClaridadPercepcion());
								System.out.println("Distancia: "+ic.get(i).getDistancia());
								
								//Mandamos el mensaje a cada agente que ha detectado una colision:
								ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
								
								InfoCollision contenido = ic.get(i);
								try {
									mensaje.setContentObject(contenido);
								} catch (IOException e) {									
									e.printStackTrace();
								}
								//agrega la dirección del destinatario:
								mensaje.addReceiver(new AID(contenido.getAgenteQueVe(),AID.ISLOCALNAME));
								//envía el mensaje:
								myAgent.send(mensaje);
																
							}							
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
			
		/*ArrayList<InfoCollision> ic2= new ArrayList<InfoCollision>();
		for(int i= 0; i< ic.size(); i++)
			ic2.add(ic.get(i));		
		for(int i= 0; i< ic2.size(); i++){
			int j= ic.size();
			while(j> i){
				String agenteI= ic2.get(i).getAgenteQueVe();
				String agenteJ= ic.get(j).getAgenteQueVe();
				if(agenteI.equals(agenteJ))
					ic.remove(j);
				j--;
			}
		}*/	
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
