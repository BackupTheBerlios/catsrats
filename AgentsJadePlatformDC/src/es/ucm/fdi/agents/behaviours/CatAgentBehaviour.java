package es.ucm.fdi.agents.behaviours;

import java.io.IOException;
import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.agents.yellowPages.YellowPages;
import es.ucm.fdi.collisionDetection.InfoAgent;
import es.ucm.fdi.collisionDetection.Orientation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class CatAgentBehaviour extends TickerBehaviour{
	
	public static final double distancia = 5.0;
	
	private double distanciaRecorrida;
	private Point punto;
	private String nombre;
	private AID[] listaAgentesComunicacion;
	private AID[] listaAgentesDeteccionColisiones;
	private YellowPages paginasAmarillas;
	private Orientation orientacion;
	private boolean activado;

	public CatAgentBehaviour(Agent agente, long tiempo) {
		super(agente, tiempo);
		this.distanciaRecorrida = 0.0;
		this.punto= new Point(Math.random()*10, Math.random()*10, 0.0);
		this.nombre= myAgent.getLocalName();
		this.orientacion = Orientation.NE;
		this.paginasAmarillas= new YellowPages();
		this.activado= false;
	}

	public String generaCoordenadas(){
	
		myAgent.addBehaviour(new DiamondPathBehaviour(this));
	    String mensaje= nombre+","+punto.getX()+","+punto.getY()+","+punto.getZ();
	    
	    return mensaje;			
	}

	protected void onTick() {
		
		listaAgentesComunicacion = paginasAmarillas.buscarServicio("envio-coordenadas", myAgent);
		listaAgentesDeteccionColisiones = paginasAmarillas.buscarServicio("deteccion-colisiones", myAgent);
		
		//Estamos a la escucha para recibir algun mansaje procedente del Agente Servidor
		ACLMessage mensajeEntrante = myAgent.receive();
		if(mensajeEntrante != null){
			System.out.println("Mensaje entrante del gato: "+mensajeEntrante.getContent());
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
					System.out.println("GATO EN "+punto.getX()+" "+punto.getY()+" "+punto.getZ());
				}
			}
		}		
		else {  //Permanecemos a la espera de que llegue un mensaje
			block();
		}		
	}
	
	private void mensajeInfoAgente(String destinatario) {
		//generaCoordenadas();//TODO quitar esta linea cuando probemos con la parte C
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

	public double getDistanciaRecorrida() {
		return distanciaRecorrida;
	}

	public void setDistanciaRecorrida(double distanciaRecorrida) {
		this.distanciaRecorrida = distanciaRecorrida;
	}

	public Orientation getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(Orientation orientacion) {
		this.orientacion = orientacion;
	}

	public Point getPunto() {
		return punto;
	}

	public void setPunto(Point punto) {
		this.punto = punto;
	}

	
}
