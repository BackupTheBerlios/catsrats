package agents.behaviours;

import agents.yellowPages.YellowPagesPerception;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class RatAgentBehaviour extends TickerBehaviour{
	
	private double coordX;
	private double coordY;
	private double coordZ;
	private String nombre;
	private boolean activado;
	private AID[] listaAgentesComunicacion;
	private YellowPagesPerception paginasAmarillas;
	
	public RatAgentBehaviour(Agent agente, long period) {
		super(agente, period);	
		this.coordX= Math.random();
		this.coordY= Math.random();
		this.coordZ= Math.random();
		this.nombre= myAgent.getLocalName();
		this.activado = false;
		paginasAmarillas= new YellowPagesPerception();
	}

	protected String generaCoordenadas() {
		coordX = coordX - 0.010;
		coordY = coordY + 0.010;
		coordZ = coordZ + 0.010;
		String mensaje= nombre+","+coordX+","+coordY+","+coordZ;
		
		return mensaje;	
	}

	protected void onTick() {
		
		listaAgentesComunicacion = paginasAmarillas.buscarServicio("envio-coordenadas", myAgent);
		
		//Estamos a la escucha para recibir algun mansaje procedente del Agente Servidor
		ACLMessage mensajeEntrante = myAgent.receive();
		if(mensajeEntrante != null){
			String contenidoMensaje = mensajeEntrante.getContent();
			if(contenidoMensaje.contains("morir")){
				myAgent.doDelete();
			}
			else if(contenidoMensaje.contains("activacion")){				
				activado= true;
				for(int i = 0; i<listaAgentesComunicacion.length; i++){
					nuevoMensaje(listaAgentesComunicacion[i].getLocalName());
				}
			}
		}
		else if(activado){
			for(int i = 0; i<listaAgentesComunicacion.length; i++){
				nuevoMensaje(listaAgentesComunicacion[i].getLocalName());
			}
		}
		else {
			block();
		}
		
	}

	protected void nuevoMensaje(String destinatario){
		//Realizamos las acciones necesarias cada cierto tiempo
		//Construye mensaje de tipo INFORM
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		//agrega contenido
		String contenido = generaCoordenadas();
		System.out.println(contenido);
		msg.setContent(contenido);
		//agrega la direccion del destinatario
		msg.addReceiver( new AID(destinatario, AID.ISLOCALNAME) );
		//envia mensaje
		myAgent.send(msg);
	}

}
