package es.ucm.fdi.agents.behaviours.paths;

import es.ucm.fdi.agents.coordinates.Path;
import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.collisionDetection.Orientation;

public class SquarePath implements Trayectorias {

	public void movimento(Path camino) {
		double distanciaRecorrida = camino.getDistanciaRecorrida();
		double distancia = camino.getDistancia();
		Orientation orientacion = camino.getOrientacion();
		Point punto = camino.getPunto();
		
		if(distanciaRecorrida < distancia && orientacion == Orientation.E){
			punto.setX(punto.getX()+0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida + 0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.E){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.N);
		}else if(distanciaRecorrida < distancia && orientacion == Orientation.N){
			punto.setY(punto.getY()+0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida+0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.N){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.O);
		}else if(distanciaRecorrida < distancia && orientacion == Orientation.O ){
			punto.setX(punto.getX()-0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida+0.1);
		}else if(distanciaRecorrida >= distancia && orientacion == Orientation.O){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.S);
		}else if(distanciaRecorrida < distancia && orientacion == Orientation.S){
			punto.setY(punto.getY()-0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida+0.1);
		}else if(distanciaRecorrida >= distancia && orientacion == Orientation.S){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.E);
		}

	}

}
