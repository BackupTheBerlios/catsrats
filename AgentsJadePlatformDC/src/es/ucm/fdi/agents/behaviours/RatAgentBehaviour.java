package es.ucm.fdi.agents.behaviours;

import java.io.IOException;

import es.ucm.fdi.agents.behaviours.paths.PathsBehaviour;
import es.ucm.fdi.agents.coordinates.Path;
import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.agents.yellowPages.YellowPages;
import es.ucm.fdi.collisionDetection.InfoAgent;
import es.ucm.fdi.collisionDetection.InfoCollision;
import es.ucm.fdi.collisionDetection.Orientation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class RatAgentBehaviour extends TickerBehaviour{
		
	public static final double DISTANCIA = 30;//2000.0;
	
	private Point punto;
	private String nombre;
	private AID[] listaAgentesComunicacion;
	private AID[] listaAgentesDeteccionColisiones;
	private YellowPages paginasAmarillas;
	private boolean activado;
	private Path camino;
	private PathsBehaviour comportamientoTrayectorias;
	private int tipoTrayectoria;
	
	public RatAgentBehaviour(Agent agente, long tiempo) {
		super(agente, tiempo);
		this.punto= new Point(Math.random()*200, Math.random()*200, 0.0);//new Point(20,0,0);//
		this.nombre= myAgent.getLocalName();
		this.paginasAmarillas= new YellowPages();
		this.activado= false;
		this.camino = null;
		this.tipoTrayectoria = (int)((Math.random()*10)%PathsBehaviour.NUMERO_TRAYECTORIAS); //Generamos una trayectoria aleatoria //PathsBehaviour.NADA;
		this.comportamientoTrayectorias = null;
	}

	private String generaCoordenadas() {
		
		switch(tipoTrayectoria){
		case PathsBehaviour.OCTOGONAL:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.E;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.OCTOGONAL, camino);
			}
		}break;
		case PathsBehaviour.DIAMANTE:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.NE;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.DIAMANTE, camino);
			}
		}break;
		case PathsBehaviour.CUADRADA:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.E;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.CUADRADA, camino);
			}
		}break;
		case PathsBehaviour.TRIANGULAR:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.O;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.TRIANGULAR, camino);
			}
		}break;
		case PathsBehaviour.ZIGZAG:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.NO;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.ZIGZAG, camino);
			}
		}break;
		case PathsBehaviour.BARRIDO:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.E;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.BARRIDO, camino);
			}
		}break;
		case PathsBehaviour.NADA:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.O;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.NADA, camino);
			}
		}break;
		}

		myAgent.addBehaviour(comportamientoTrayectorias);
		
		String mensaje= nombre+","+camino.getPunto().getX()+","+camino.getPunto().getY()+","+camino.getPunto().getZ()+","+camino.getOrientacion();
		
		return mensaje;	
	}

	protected void onTick() {
		
		listaAgentesComunicacion = paginasAmarillas.buscarServicio("envio-coordenadas", myAgent);
		listaAgentesDeteccionColisiones = paginasAmarillas.buscarServicio("deteccion-colisiones", myAgent);
		
		//Estamos a la escucha para recibir algun mansaje procedente del Agente Servidor
		ACLMessage mensajeEntrante = myAgent.receive();
		if(mensajeEntrante != null){
			if(mensajeEntrante.getPerformative() == ACLMessage.REQUEST){ //Recibimos un mensaje InfoCollision
				try {
					InfoCollision info = (InfoCollision)mensajeEntrante.getContentObject();
					//TODO Aqui habra que tomar una decision
					
					
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
			else{
				System.out.println("Mensaje entrante del raton: "+mensajeEntrante.getContent());
				String contenidoMensaje = mensajeEntrante.getContent();
				if(contenidoMensaje.contains("morir")){ //Matamos a los agentes
					myAgent.doDelete();
				}	
				else if(contenidoMensaje.contains("comunicacion-lista")){	//Permitimos que empiecen a generar coordenadas				
					activado= true;
				}
				else if(contenidoMensaje.contains("genera")){	//Permitimos que sigan generando coordenadas
					for(int i = 0; i<listaAgentesComunicacion.length && listaAgentesDeteccionColisiones.length>0; i++){
						if(activado) nuevoMensaje(listaAgentesComunicacion[i].getLocalName());
						mensajeInfoAgente(listaAgentesDeteccionColisiones[i].getLocalName());
						System.out.println("RATON EN "+camino.getPunto().getX()+" "+camino.getPunto().getY()+" "+camino.getPunto().getZ()+" "+camino.getOrientacion());
					}
				}
			}
		}		
		else {  //Permanecemos a la espera de que llegue el mensaje de activacion
			block();
		}		
	}
	
	private void mensajeInfoAgente(String destinatario) {
		generaCoordenadas();//TODO quitar esta linea cuando probemos con la parte C
		InfoAgent info = new InfoAgent(nombre,camino.getPunto().getX(),camino.getPunto().getY(),camino.getPunto().getZ(),camino.getOrientacion());
		
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

	private void nuevoMensaje(String destinatario){
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
