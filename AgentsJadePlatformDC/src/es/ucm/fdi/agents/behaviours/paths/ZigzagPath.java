package es.ucm.fdi.agents.behaviours.paths;

import es.ucm.fdi.agents.coordinates.Path;
import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.collisionDetection.Orientation;

/*
 * Trayectoria en zigzag 
 */
public class ZigzagPath implements Trayectorias {

	public final double TOPE_SUPERIOR = 150.0;
	public final double TOPE_INFERIOR = -50.0;
	private final double AVANCE = 10.0;
	
	public void movimento(Path camino) {
		double distanciaRecorrida = camino.getDistanciaRecorrida();
		double distancia = camino.getDistancia();
		Orientation orientacion = camino.getOrientacion();
		Point punto = camino.getPunto();
		
		if(distanciaRecorrida < distancia && punto.getY()<TOPE_SUPERIOR && orientacion == Orientation.NO){
			punto.setX(punto.getX()-AVANCE);
			punto.setY(punto.getY()+AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida+AVANCE);
		}else if(distanciaRecorrida >= distancia && punto.getY() < TOPE_SUPERIOR && orientacion == Orientation.NO){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.NE);
		}else if(distanciaRecorrida < distancia && punto.getY() < TOPE_SUPERIOR && orientacion == Orientation.NE){
			punto.setX(punto.getX()+AVANCE);
			punto.setY(punto.getY()+AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida+AVANCE);
		}else if(distanciaRecorrida >= distancia && punto.getY() < TOPE_SUPERIOR && orientacion==Orientation.NE){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.NO);
		}else if(punto.getY() >= TOPE_SUPERIOR){  //Hemos llegado al TOPE_SUPERIOR y ahora vamos hacia abajo.
			camino.setDistanciaRecorrida(0.0);
			punto.setX(punto.getX()-AVANCE);
			punto.setY(punto.getY()-AVANCE);
			camino.setOrientacion(Orientation.SO);
		}else if(distanciaRecorrida < distancia && punto.getY() > TOPE_INFERIOR && orientacion == Orientation.SO){
			punto.setX(punto.getX()-AVANCE);
			punto.setY(punto.getY()-AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida+AVANCE);
		}else if(distanciaRecorrida >= distancia && punto.getY() > TOPE_INFERIOR && orientacion == Orientation.SO){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.SE);
		}else if(distanciaRecorrida < distancia && punto.getY() > TOPE_INFERIOR && orientacion == Orientation.SE){
			punto.setX(punto.getX()+AVANCE);
			punto.setY(punto.getY()-AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida+AVANCE);
		}else if(distanciaRecorrida >= distancia && punto.getY() > TOPE_INFERIOR && orientacion == Orientation.SE){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.SO);
		}else if(punto.getY() <= TOPE_INFERIOR){  //Hemos llegado al TOPE_INFERIOR y ahora vamos hacia arriba.
			camino.setDistanciaRecorrida(0.0);
			punto.setX(punto.getX()-AVANCE);
			punto.setY(punto.getY()+AVANCE);
			camino.setOrientacion(Orientation.NO);
		}

	}

}
