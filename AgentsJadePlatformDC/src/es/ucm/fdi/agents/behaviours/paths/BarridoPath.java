package es.ucm.fdi.agents.behaviours.paths;

import es.ucm.fdi.agents.coordinates.Path;
import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.collisionDetection.Orientation;

public class BarridoPath implements Trayectorias {
	private final double AVANCE = 10.0;
	private final double DESCENSO = 5.0;
	
	int lado;
	public BarridoPath(){
		lado = 0; 
	}
	
	public void movimento(Path camino) {
		Orientation orientacion = camino.getOrientacion();
		Point punto = camino.getPunto();
		double distancia = camino.getDistancia();
		double distanciaRecorrida = camino.getDistanciaRecorrida();
		
		if(orientacion == Orientation.E && distanciaRecorrida < distancia && lado == 0){
			punto.setX(punto.getX()+AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida + AVANCE);
		}else if(orientacion == Orientation.E && distanciaRecorrida >= distancia){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.S);
			lado = 1;//Estamos en la derecha
		}else if(orientacion == Orientation.S && distanciaRecorrida < DESCENSO && lado == 1){
			punto.setY(punto.getY()-AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida + AVANCE);
		}else if(orientacion == Orientation.S && distanciaRecorrida >= DESCENSO && lado == 1){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.O);
		}else if(orientacion == Orientation.O && distanciaRecorrida < distancia && lado == 1){
			punto.setX(punto.getX()-AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida + AVANCE);
		}else if(orientacion == Orientation.O && distanciaRecorrida >= distancia){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.S);
			lado = 0;
		}else if(orientacion == Orientation.S && distanciaRecorrida < DESCENSO && lado == 0){
			punto.setY(punto.getY()-AVANCE);
			camino.setDistanciaRecorrida(distanciaRecorrida + AVANCE);
		}else if(orientacion == Orientation.S && distanciaRecorrida >= DESCENSO && lado == 0){
			camino.setDistanciaRecorrida(0.0);
			camino.setOrientacion(Orientation.E);
		}
	}

}
