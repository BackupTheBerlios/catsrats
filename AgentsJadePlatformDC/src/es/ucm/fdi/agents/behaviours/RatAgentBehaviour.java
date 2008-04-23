package es.ucm.fdi.agents.behaviours;

import java.io.IOException;
import java.io.Serializable;

import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.agents.yellowPages.YellowPages;
import es.ucm.fdi.collisionDetection.InfoAgent;
import es.ucm.fdi.collisionDetection.Orientation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class RatAgentBehaviour extends TickerBehaviour{
	
	private Point punto;
	private String nombre;
	private AID[] listaAgentesComunicacion;
	private AID[] listaAgentesDeteccionColisiones;
	private YellowPages paginasAmarillas;
	private Orientation orientacion;
	private double trayectoria;
	private double anguloTrayectoriaCircular;
	private double radioCircunferencia;
	private boolean activado;
	
	public RatAgentBehaviour(Agent agente, long tiempo) {
		super(agente, tiempo);	
		punto= new Point(Math.random()*10, Math.random()*10, 0.0);
		this.nombre= myAgent.getLocalName();
		this.orientacion = Orientation.E;
		paginasAmarillas= new YellowPages();
		this.trayectoria = Math.round((Math.random()*10)%2);
		this.radioCircunferencia = Math.random()*10;
		this.anguloTrayectoriaCircular = 0.0;
		this.activado= false;
	}

	protected String generaCoordenadas() {
		
		if(trayectoria==0)
			trayectoriaCircular();  //Movimiento en circulo
		else
			trayectoriaZigZag();  //Movimiento en zigzag??
		
		String mensaje= nombre+","+punto.getX()+","+punto.getY()+","+punto.getZ();
		
		return mensaje;	
	}

	private void trayectoriaZigZag() {
		double direccion = Math.round((Math.random()*10)%4);
		
		if(direccion == 0){
			punto.setX(punto.getX()+ 0.5);
			punto.setY(punto.getY()+ 0.5);
		}
		else if(direccion == 1){
			punto.setX(punto.getX()+ 0.5);
			punto.setY(punto.getY()- 0.5);
		}else if(direccion == 2){
			punto.setX(punto.getX()- 0.5);
			punto.setY(punto.getY()+ 0.5);
		}else if(direccion == 3){
			punto.setX(punto.getX()- 0.5);
			punto.setY(punto.getY()- 0.5);
		}
		
	}

	private void trayectoriaCircular() {
		
		double incrementoAngulo = 5*Math.PI/180; //incrementamos el angulo en 5 grados

		punto.setX(radioCircunferencia*Math.cos(anguloTrayectoriaCircular));
		punto.setY(radioCircunferencia*Math.sin(anguloTrayectoriaCircular));
		anguloTrayectoriaCircular += incrementoAngulo;
	}

	protected void onTick() {
		
		listaAgentesComunicacion = paginasAmarillas.buscarServicio("envio-coordenadas", myAgent);
		listaAgentesDeteccionColisiones = paginasAmarillas.buscarServicio("deteccion-colisiones", myAgent);
		
		//Estamos a la escucha para recibir algun mansaje procedente del Agente Servidor
		ACLMessage mensajeEntrante = myAgent.receive();
		if(mensajeEntrante != null){
			System.out.println("Mensaje entrante del raton: "+mensajeEntrante.getContent());
			String contenidoMensaje = mensajeEntrante.getContent();
			if(contenidoMensaje.contains("morir")){ //Matamos a los agentes
				myAgent.doDelete();
			}	
			if(contenidoMensaje.contains("comunicacion-lista")){	//Permitimos que empiecen a generar coordenadas				
				activado= true;
			}
			if(contenidoMensaje.contains("genera")){	//Permitimos que sigan generando coordenadas
				for(int i = 0; i<listaAgentesComunicacion.length; i++){
					if(activado) nuevoMensaje(listaAgentesComunicacion[i].getLocalName());
					mensajeInfoAgente(listaAgentesDeteccionColisiones[i].getLocalName());
				}
			}
		}		
		else {  //Permanecemos a la espera de que llegue el mensaje de activacion
			block();
		}		
	}
	
	private void mensajeInfoAgente(String destinatario) {
		InfoAgent info = new InfoAgent(nombre,punto.getX(),punto.getY(),punto.getZ(),orientacion);
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		//agrega contenido
		try {
			msg.setContentObject(info);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//agrega la direccion del destinatario
		msg.addReceiver( new AID(destinatario, AID.ISLOCALNAME) );
		//envia mensaje
		myAgent.send(msg);
		
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
