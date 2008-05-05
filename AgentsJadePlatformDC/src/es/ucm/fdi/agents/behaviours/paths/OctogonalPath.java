package es.ucm.fdi.agents.behaviours.paths;

import es.ucm.fdi.agents.coordinates.Path;
import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.collisionDetection.Orientation;

/*
 * Traycetoria en forma octogonal con sentido antihorario
 */
public class OctogonalPath implements Trayectorias {

	public void movimento(Path camino) {
		
		double distanciaRecorrida = camino.getDistanciaRecorrida();
		double distancia = camino.getDistancia();
		Orientation orientacion = camino.getOrientacion();
		Point punto = camino.getPunto();
		
		if(distanciaRecorrida<distancia && orientacion == Orientation.E){
			punto.setX(punto.getX() + 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida + 0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.E){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.NE);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.NE){
			punto.setX(punto.getX() + 0.1);
			punto.setY(punto.getY() + 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.NE){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.N);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.N){
			punto.setY(punto.getY() + 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.N){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.NO);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.NO){
			punto.setX(punto.getX() - 0.1);
			punto.setY(punto.getY() + 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.NO){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.O);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.O){
			punto.setX(punto.getX() - 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.O){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.SO);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.SO){
			punto.setX(punto.getX() - 0.1);
			punto.setY(punto.getY() - 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.SO){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.S);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.S){
			punto.setY(punto.getY() - 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.S){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.SE);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.SE){
			punto.setX(punto.getX() + 0.1);
			punto.setY(punto.getY() - 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.SE){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.E);
		}
		
	}

}
