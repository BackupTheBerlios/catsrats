package es.ucm.fdi.agents.behaviours;

import java.io.IOException;

import javax.print.attribute.standard.OrientationRequested;

import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.agents.yellowPages.YellowPages;
import es.ucm.fdi.collisionDetection.InfoAgent;
import es.ucm.fdi.collisionDetection.Orientation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class RatAgentBehaviour extends TickerBehaviour{
		
	private static final double distancia = 5.0;
	
	private double distanciaRecorrida;
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
		this.distanciaRecorrida = 0.0;
		punto= new Point(Math.random()*10, Math.random()*10, 0.0);
		this.nombre= myAgent.getLocalName();
		this.orientacion = Orientation.E;
		paginasAmarillas= new YellowPages();
		this.trayectoria = Math.round((Math.random()*10)%2);
		this.radioCircunferencia = Math.random()*10;
		this.anguloTrayectoriaCircular = 0.0;
		this.activado= false;
	}

	private String generaCoordenadas() {
		
		/*if(trayectoria==0)
			trayectoriaCircular();  //Movimiento en circulo
		else
			trayectoriaZigZag();  //Movimiento en zigzag??*/
		trayectoriaOctogonal();
		
		String mensaje= nombre+","+punto.getX()+","+punto.getY()+","+punto.getZ();
		
		return mensaje;	
	}

	private void trayectoriaZigZag() {
		double direccion = Math.round((Math.random()*10)%4);
		
		if(direccion == 0){//Al fondo de la pantalla a la derecha
			punto.setX(punto.getX()+ 0.5);
			punto.setY(punto.getY()+ 0.5);
			orientacion = Orientation.NE;
		}
		else if(direccion == 1){//hacia fuera de la pantalla a la derecha
			punto.setX(punto.getX()+ 0.5);
			punto.setY(punto.getY()- 0.5);
			orientacion = Orientation.SE;
		}else if(direccion == 2){//Al fondo de la pantalla a la izquierda
			punto.setX(punto.getX()- 0.5);
			punto.setY(punto.getY()+ 0.5);
			orientacion = Orientation.NO;
		}else if(direccion == 3){//Hacia fuera de la pantalla a la izquierda
			punto.setX(punto.getX()- 0.5);
			punto.setY(punto.getY()- 0.5);
			orientacion = Orientation.SO;
		}
		
	}

	private void trayectoriaCircular() {
		
		double incrementoAngulo = 5*Math.PI/180; //incrementamos el angulo en 5 grados

		punto.setX(radioCircunferencia*Math.cos(anguloTrayectoriaCircular));
		punto.setY(radioCircunferencia*Math.sin(anguloTrayectoriaCircular));
		anguloTrayectoriaCircular += incrementoAngulo;
	}
	
	private void trayectoriaOctogonal(){
		
		if(distanciaRecorrida<distancia && orientacion == Orientation.E){
			punto.setX(punto.getX() + 0.1);
			distanciaRecorrida += 0.1;
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.E){
			distanciaRecorrida = 0.0;
			orientacion = Orientation.NE;
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.NE){
			punto.setX(punto.getX() + 0.1);
			punto.setY(punto.getY() + 0.1);
			distanciaRecorrida += 0.1;
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.NE){
			distanciaRecorrida = 0.0;
			orientacion = Orientation.N;
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.N){
			punto.setY(punto.getY() + 0.1);
			distanciaRecorrida += 0.1;
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.N){
			distanciaRecorrida = 0.0;
			orientacion = Orientation.NO;
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.NO){
			punto.setX(punto.getX() - 0.1);
			punto.setY(punto.getY() + 0.1);
			distanciaRecorrida += 0.1;
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.NO){
			distanciaRecorrida = 0.0;
			orientacion = Orientation.O;
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.O){
			punto.setX(punto.getX() - 0.1);
			distanciaRecorrida += 0.1;
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.O){
			distanciaRecorrida = 0.0;
			orientacion = Orientation.SO;
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.SO){
			punto.setX(punto.getX() - 0.1);
			punto.setY(punto.getY() - 0.1);
			distanciaRecorrida += 0.1;
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.SO){
			distanciaRecorrida = 0.0;
			orientacion = Orientation.S;
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.S){
			punto.setY(punto.getY() - 0.1);
			distanciaRecorrida += 0.1;
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.S){
			distanciaRecorrida = 0.0;
			orientacion = Orientation.SE;
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.SE){
			punto.setX(punto.getX() + 0.1);
			punto.setY(punto.getY() - 0.1);
			distanciaRecorrida += 0.1;
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.SE){
			distanciaRecorrida = 0.0;
			orientacion = Orientation.E;
		}
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
			else if(contenidoMensaje.contains("comunicacion-lista")){	//Permitimos que empiecen a generar coordenadas				
				activado= true;
			}
			else if(contenidoMensaje.contains("genera")){	//Permitimos que sigan generando coordenadas
				for(int i = 0; i<listaAgentesComunicacion.length && listaAgentesDeteccionColisiones.length>0; i++){
					if(activado) nuevoMensaje(listaAgentesComunicacion[i].getLocalName());
					mensajeInfoAgente(listaAgentesDeteccionColisiones[i].getLocalName());
					System.out.println("RATON EN "+punto.getX()+" "+punto.getY()+" "+punto.getZ());
				}
			}
		}		
		else {  //Permanecemos a la espera de que llegue el mensaje de activacion
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
