package es.ucm.fdi.agents.behaviours;

import java.text.DecimalFormat;

import es.ucm.fdi.agents.yellowPages.YellowPages;
import es.ucm.fdi.socket.SocketServidor;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class ComunicationAgentBehaviour extends TickerBehaviour{
	
	private String xFormateada;
	private String yFormateada;
	private String zFormateada;
	private DecimalFormat coordenada; 
	private ACLMessage msg;
	private SocketServidor socket;
	private AID[] agentesGeneradores;
	private YellowPages paginasAmarillas;
	private AID[] agentesDeteccionColisiones;
	private boolean establecerComunicacion;//el socket ya está activo para recibir coordenadas. 
	
	public ComunicationAgentBehaviour(Agent agente, long tiempo, SocketServidor socket) {
		super(agente, tiempo);
		this.xFormateada=new String();
		this.yFormateada=new String();
		this.zFormateada=new String();
		this.socket = socket;
		this.paginasAmarillas = new YellowPages();
		this.establecerComunicacion= true;
	}

	protected void onTick() {
		
		agentesGeneradores = paginasAmarillas.buscarServicio("generacion-coordenadas", myAgent);
		
		agentesDeteccionColisiones= paginasAmarillas.buscarServicio("deteccion-colisiones", myAgent);
		
		myAgent.addBehaviour(new ActivationAgentsBehaviour(this));
		
		msg =myAgent.receive(); //captura un mensaje de la bandeja de entrada 
		
		//Comportamiento normal del agente de comunicacion: recibir coordenadas y mandarlas al entorno grafico
		myAgent.addBehaviour(new FormatAndSendBehaviour(this));
		
	}

	public String getXFormateada() {
		return xFormateada;
	}

	public void setXFormateada(String formateada) {
		xFormateada = formateada;
	}

	public String getYFormateada() {
		return yFormateada;
	}

	public void setYFormateada(String formateada) {
		yFormateada = formateada;
	}

	public String getZFormateada() {
		return zFormateada;
	}

	public void setZFormateada(String formateada) {
		zFormateada = formateada;
	}

	public DecimalFormat getCoordenada() {
		return coordenada;
	}

	public void setCoordenada(DecimalFormat coordenada) {
		this.coordenada = coordenada;
	}

	public ACLMessage getMsg() {
		return msg;
	}

	public void setMsg(ACLMessage msg) {
		this.msg = msg;
	}

	public SocketServidor getSocket() {
		return socket;
	}

	public void setSocket(SocketServidor socket) {
		this.socket = socket;
	}

	public AID[] getAgentesGeneradores() {
		return agentesGeneradores;
	}

	public void setAgentesGeneradores(AID[] agentesGeneradores) {
		this.agentesGeneradores = agentesGeneradores;
	}

	public AID[] getAgentesDeteccionColisiones() {
		return agentesDeteccionColisiones;
	}

	public boolean getEstablecerComunicacion() {
		return establecerComunicacion;
	}

	public void setEstablecerComunicacion(boolean establecerComunicacion) {
		this.establecerComunicacion = establecerComunicacion;
	}
}
