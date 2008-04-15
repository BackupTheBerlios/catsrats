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
	private double radioCircunferencia;
	
	public RatAgentBehaviour(Agent agente, long tiempo) {
		super(agente, tiempo);	
		this.coordX= Math.random()*20;
		this.coordY= Math.random()*20;
		this.coordZ= 0.0;
		this.nombre= myAgent.getLocalName();
		this.orientacion = Orientacion.E;
		this.activado = false;
		paginasAmarillas= new YellowPages();
		this.trayectoria = Math.round((Math.random()*10)%2);
		this.radioCircunferencia = Math.random()*10;
		this.anguloTrayectoriaCircular = 0.0;
		
	}

	protected String generaCoordenadas() {
		
		if(trayectoria==0)
			trayectoriaCircular();  //Movimiento en circulo
		else
			trayectoriaZigZag();  //Movimiento en zigzag??
		
		String mensaje= nombre+","+coordX+","+coordY+","+coordZ;
		
		return mensaje;	
	}

	private void trayectoriaZigZag() {
		double direccion = Math.round((Math.random()*10)%4);
		
		if(direccion == 0){
			coordX += 0.5;
			coordY += 0.5;
		}
		else if(direccion == 1){
			coordX += 0.5;
			coordY -= 0.5;
		}else if(direccion == 2){
			coordX -= 0.5;
			coordY += 0.5;
		}else if(direccion == 3){
			coordX -= 0.5;
			coordY -= 0.5;
		}
		
	}

	private void trayectoriaCircular() {
		
		double incrementoAngulo = 5*Math.PI/180; //incrementamos el angulo en 5 grados

		coordX = radioCircunferencia*Math.cos(anguloTrayectoriaCircular);
		coordY = radioCircunferencia*Math.sin(anguloTrayectoriaCircular);
		anguloTrayectoriaCircular += incrementoAngulo;
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
		else if(activado){ //Si no recibimos mensaje pero los agentes estan activados
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
