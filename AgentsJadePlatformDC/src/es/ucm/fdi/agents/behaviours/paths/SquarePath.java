package es.ucm.fdi.agents.behaviours.paths;

import es.ucm.fdi.agents.coordinates.Path;
import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.collisionDetection.Orientation;

/*
 * Trayectoria con forma cuadrada en sentido antihorario
 */
public class SquarePath implements Trayectorias {
	
	private final double AVANCE = 10.0;

	public void movimento(Path camino) {
		double distanciaRecorrida = camino.getDistanciaRecorrida();
		double distancia = camino.getDistancia();
		Orientation orientacion = camino.getOrientacion();
		Point punto = camino.getPunto();
		
		if(distanciaRecorrida < distancia && orientacion == Orientation.E){
			punto.setX(punto.getX()+AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida + AVANCE);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.E){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.N);
		}else if(distanciaRecorrida < distancia && orientacion == Orientation.N){
			punto.setY(punto.getY()+AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida+AVANCE);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.N){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.O);
		}else if(distanciaRecorrida < distancia && orientacion == Orientation.O ){
			punto.setX(punto.getX()-AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida+AVANCE);
		}else if(distanciaRecorrida >= distancia && orientacion == Orientation.O){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.S);
		}else if(distanciaRecorrida < distancia && orientacion == Orientation.S){
			punto.setY(punto.getY()-AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida+AVANCE);
		}else if(distanciaRecorrida >= distancia && orientacion == Orientation.S){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.E);
		}

	}

}
