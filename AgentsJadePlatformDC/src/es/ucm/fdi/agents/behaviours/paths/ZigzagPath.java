package es.ucm.fdi.agents.behaviours.paths;

import es.ucm.fdi.agents.coordinates.Path;
import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.collisionDetection.Orientation;

/*
 * Trayectoria en zigzag 
 */
public class ZigzagPath implements Trayectorias {

	public static final double TOPE_SUPERIOR = 15.0;
	public static final double TOPE_INFERIOR = -5.0;
	
	public void movimento(Path camino) {
		double distanciaRecorrida = camino.getDistanciaRecorrida();
		double distancia = camino.getDistancia();
		Orientation orientacion = camino.getOrientacion();
		Point punto = camino.getPunto();
		
		if(distanciaRecorrida < distancia && punto.getY()<TOPE_SUPERIOR && orientacion == Orientation.NO){
			punto.setX(punto.getX()-0.1);
			punto.setY(punto.getY()+0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida+0.1);
		}else if(distanciaRecorrida >= distancia && punto.getY() < TOPE_SUPERIOR && orientacion == Orientation.NO){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.NE);
		}else if(distanciaRecorrida < distancia && punto.getY() < TOPE_SUPERIOR && orientacion == Orientation.NE){
			punto.setX(punto.getX()+0.1);
			punto.setY(punto.getY()+0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida+0.1);
		}else if(distanciaRecorrida >= distancia && punto.getY() < TOPE_SUPERIOR && orientacion==Orientation.NE){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.NO);
		}else if(punto.getY() >= TOPE_SUPERIOR){  //Hemos llegado al TOPE_SUPERIOR y ahora vamos hacia abajo.
			camino.setDistanciaRecorrida(0.0);
			punto.setX(punto.getX()-0.1);
			punto.setY(punto.getY()-0.1);
			camino.setOrientacion(Orientation.SO);
		}else if(distanciaRecorrida < distancia && punto.getY() > TOPE_INFERIOR && orientacion == Orientation.SO){
			punto.setX(punto.getX()-0.1);
			punto.setY(punto.getY()-0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida+0.1);
		}else if(distanciaRecorrida >= distancia && punto.getY() > TOPE_INFERIOR && orientacion == Orientation.SO){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.SE);
		}else if(distanciaRecorrida < distancia && punto.getY() > TOPE_INFERIOR && orientacion == Orientation.SE){
			punto.setX(punto.getX()+0.1);
			punto.setY(punto.getY()-0.1);
			camino.setDistanciaRecorrida(distanciaRecorrida+0.1);
		}else if(distanciaRecorrida >= distancia && punto.getY() > TOPE_INFERIOR && orientacion == Orientation.SE){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.SO);
		}else if(punto.getY() <= TOPE_INFERIOR){  //Hemos llegado al TOPE_INFERIOR y ahora vamos hacia arriba.
			camino.setDistanciaRecorrida(0.0);
			punto.setX(punto.getX()-0.1);
			punto.setY(punto.getY()+0.1);
			camino.setOrientacion(Orientation.NO);
		}

	}

}
