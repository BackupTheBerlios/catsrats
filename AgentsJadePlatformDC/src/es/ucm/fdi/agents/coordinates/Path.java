package es.ucm.fdi.agents.coordinates;

import es.ucm.fdi.collisionDetection.Orientation;

/*
 * Esta clase guarda todos los parametros necesarios para poder mover a un agente mediante las trayectorias definidas
 */
public class Path {
	private double distanciaRecorrida;  //Distancia recorrida que lleva el agente
	private double distancia;  //Longitud del segmento que debe recorrer el agente cuando describe una trayectoria
	private Orientation orientacion;  //Hacia donde se dirige el agente
	private Point punto;  //Localizacion exacta del agente en un momento dado
	
	public Path(double distanciaRecorrida, double distancia, Orientation orientacion, Point punto){
		this.distanciaRecorrida = distanciaRecorrida;
		this.distancia = distancia;
		this.orientacion = orientacion;
		this.punto = punto;
	}

	public double getDistanciaRecorrida() {
		return distanciaRecorrida;
	}

	public void setDistanciaRecorrida(double distanciaRecorrida) {
		this.distanciaRecorrida = distanciaRecorrida;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
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
