package es.ucm.fdi.agents.behaviours;

import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.agents.yellowPages.YellowPages;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class CatAgentBehaviour extends TickerBehaviour{
	
	private Point punto;
	private String nombre;
	private boolean activado;
	private AID[] listaAgentesComunicacion;
	private YellowPages paginasAmarillas;
	private enum Orientacion{N,S,E,O,NE,NO,SE,SO};
	private Orientacion orientacion;

	public CatAgentBehaviour(Agent agente, long tiempo) {
		super(agente, tiempo);
		punto= new Point(Math.random()*20, Math.random()*20, 0.0);
		this.nombre= myAgent.getLocalName();
		this.orientacion = Orientacion.E;
		this.activado = false;
		paginasAmarillas= new YellowPages();
	
	}

	public String generaCoordenadas(){
		
		punto.setX(punto.getX()- Math.random()/10);
		punto.setY(punto.getY() - Math.random()/10);
		
	    String mensaje= nombre+","+punto.getX()+","+punto.getY()+","+punto.getZ();
	    
	    return mensaje;			
	}

	protected void onTick() {
		
		listaAgentesComunicacion = paginasAmarillas.buscarServicio("envio-coordenadas", myAgent);
		
		//Estamos a la escucha para recibir algun mansaje procedente del Agente Servidor
		ACLMessage mensajeEntrante = myAgent.receive();
		if(mensajeEntrante != null){
			String contenidoMensaje = mensajeEntrante.getContent();
			if(contenidoMensaje.contains("morir")){ //Matamos a los agentes
				myAgent.doDelete();
			}
			else if(contenidoMensaje.contains("activacion")){	//Activamos a los agentes			
				activado= true;
				for(int i = 0; i<listaAgentesComunicacion.length; i++){
					nuevoMensaje(listaAgentesComunicacion[i].getLocalName());
				}
				
			}
		}
		else if(activado){  //Si no recibimos mensaje pero los agentes estan activados
							//continuamos con la ejecucion
			for(int i = 0; i<listaAgentesComunicacion.length; i++){
				nuevoMensaje(listaAgentesComunicacion[i].getLocalName());
			}
		}
		else {  //Permanecemos a la espera de que llegue el mensaje de activacion
			block();
		}
		
	}
	
	protected void nuevoMensaje(String destinatario){
		//Realizamos las acciones necesarias cada cierto tiempo
		//Construye mensaje de tipo INFORM
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		//agrega contenido
		String contenido = generaCoordenadas();
		msg.setContent(contenido);
		//agrega la direccion del destinatario
		msg.addReceiver( new AID(destinatario, AID.ISLOCALNAME) );
		//envia mensaje
		myAgent.send(msg);
	}

	
}
