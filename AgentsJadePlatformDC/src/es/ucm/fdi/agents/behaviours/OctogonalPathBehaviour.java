package es.ucm.fdi.agents.behaviours;

import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.collisionDetection.Orientation;
import jade.core.behaviours.OneShotBehaviour;

/*
 * Trayectoria octogonal
 */
public class OctogonalPathBehaviour extends OneShotBehaviour{

	private RatAgentBehaviour rab;
	
	public OctogonalPathBehaviour(RatAgentBehaviour rab){
		this.rab = rab;
	}
	public void action() {
		double distanciaRecorrida = rab.getDistanciaRecorrida();
		double distancia = RatAgentBehaviour.distancia;
		Orientation orientacion = rab.getOrientacion();
		Point punto = rab.getPunto();
		
		if(distanciaRecorrida<distancia && orientacion == Orientation.E){
			punto.setX(punto.getX() + 0.1);
			rab.setDistanciaRecorrida(distanciaRecorrida + 0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.E){
			rab.setDistanciaRecorrida(0.0);
			rab.setOrientacion(Orientation.NE);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.NE){
			punto.setX(punto.getX() + 0.1);
			punto.setY(punto.getY() + 0.1);
			rab.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.NE){
			rab.setDistanciaRecorrida(0.0);
			rab.setOrientacion(Orientation.N);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.N){
			punto.setY(punto.getY() + 0.1);
			rab.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.N){
			rab.setDistanciaRecorrida(0.0);
			rab.setOrientacion(Orientation.NO);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.NO){
			punto.setX(punto.getX() - 0.1);
			punto.setY(punto.getY() + 0.1);
			rab.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.NO){
			rab.setDistanciaRecorrida(0.0);
			rab.setOrientacion(Orientation.O);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.O){
			punto.setX(punto.getX() - 0.1);
			rab.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.O){
			rab.setDistanciaRecorrida(0.0);
			rab.setOrientacion(Orientation.SO);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.SO){
			punto.setX(punto.getX() - 0.1);
			punto.setY(punto.getY() - 0.1);
			rab.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.SO){
			rab.setDistanciaRecorrida(0.0);
			rab.setOrientacion(Orientation.S);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.S){
			punto.setY(punto.getY() - 0.1);
			rab.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.S){
			rab.setDistanciaRecorrida(0.0);
			rab.setOrientacion(Orientation.SE);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.SE){
			punto.setX(punto.getX() + 0.1);
			punto.setY(punto.getY() - 0.1);
			rab.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.SE){
			rab.setDistanciaRecorrida(0.0);
			rab.setOrientacion(Orientation.E);
		}
		
	}

}
