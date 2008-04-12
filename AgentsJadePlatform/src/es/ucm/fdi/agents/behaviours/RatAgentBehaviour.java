package es.ucm.fdi.agents.behaviours;

import es.ucm.fdi.agents.yellowPages.YellowPages;
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
	private YellowPages paginasAmarillas;
	private enum Orientacion{N,S,E,O,NE,NO,SE,SO};
	private Orientacion orientacion;
	private double trayectoria;
	private double anguloTrayectoriaCircular;
	
	public RatAgentBehaviour(Agent agente, long period) {
		super(agente, period);	
		this.coordX= Math.random();
		this.coordY= Math.random();
		this.coordZ= 0.0;
		this.nombre= myAgent.getLocalName();
		this.orientacion = Orientacion.E;
		this.activado = false;
		paginasAmarillas= new YellowPages();
		this.trayectoria = (Math.random()*10)%2;
		this.anguloTrayectoriaCircular = 0.0;
	}

	protected String generaCoordenadas() {
		
		//Movimiento en circulo
		//if(trayectoria==0)
			trayectoriaCircular();
		//else
			//trayectoriaZigZag();
		
		String mensaje= nombre+","+coordX+","+coordY+","+coordZ;
		
		return mensaje;	
	}

	private void trayectoriaZigZag() {
		//TODO
		
	}

	private void trayectoriaCircular() {
		double radio = 3;
		double incremento = 5*Math.PI/180; //incrementamos el angulo en 5 grados
		coordX = radio*Math.cos(anguloTrayectoriaCircular);
		coordY = radio*Math.sin(anguloTrayectoriaCircular);
		anguloTrayectoriaCircular += incremento;
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
		msg.setContent(contenido);
		//agrega la direccion del destinatario
		msg.addReceiver( new AID(destinatario, AID.ISLOCALNAME) );
		//envia mensaje
		myAgent.send(msg);
	}

}
