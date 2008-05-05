package es.ucm.fdi.agents.behaviours.paths;

import es.ucm.fdi.agents.coordinates.Path;
import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.collisionDetection.Orientation;

/*
 * Trayectoria en forma de diamante con sentido horario
 */
public class DiamondPath implements Trayectorias {

	public void movimento(Path camino) {
		
		double distanciaRecorrida = camino.getDistanciaRecorrida();
		double distancia = camino.getDistancia();
		Orientation orientacion = camino.getOrientacion();
		Point punto = camino.getPunto();
		
		if(distanciaRecorrida<distancia && orientacion == Orientation.NE){
			punto.setX(punto.getX() + 0.1);
			punto.setY(punto.getY() + 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida + 0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.NE){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.SE);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.SE){
			punto.setX(punto.getX() + 0.1);
			punto.setY(punto.getY() - 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.SE){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.SO);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.SO){
			punto.setX(punto.getX() - 0.1);
			punto.setY(punto.getY() - 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.SO){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.NO);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.NO){
			punto.setX(punto.getX() - 0.1);
			punto.setY(punto.getY() + 0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.NO){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.NE);
		}

	}

}
