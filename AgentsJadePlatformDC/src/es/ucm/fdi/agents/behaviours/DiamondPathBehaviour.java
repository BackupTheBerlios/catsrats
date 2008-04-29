package es.ucm.fdi.agents.behaviours;

import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.collisionDetection.Orientation;
import jade.core.behaviours.OneShotBehaviour;

/*
 * Trayectoria en forma de diamante
 */
public class DiamondPathBehaviour extends OneShotBehaviour{

	private CatAgentBehaviour cab;
	
	public DiamondPathBehaviour(CatAgentBehaviour cab){
		this.cab = cab;
	}
	public void action() {
		double distanciaRecorrida = cab.getDistanciaRecorrida();
		double distancia = RatAgentBehaviour.distancia;
		Orientation orientacion = cab.getOrientacion();
		Point punto = cab.getPunto();
		
		if(distanciaRecorrida<distancia && orientacion == Orientation.NE){
			punto.setX(punto.getX() + 0.1);
			punto.setY(punto.getY() + 0.1);
			cab.setDistanciaRecorrida(distanciaRecorrida + 0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.NE){
			cab.setDistanciaRecorrida(0.0);
			cab.setOrientacion(Orientation.SE);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.SE){
			punto.setX(punto.getX() + 0.1);
			punto.setY(punto.getY() - 0.1);
			cab.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.SE){
			cab.setDistanciaRecorrida(0.0);
			cab.setOrientacion(Orientation.SO);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.SO){
			punto.setX(punto.getX() - 0.1);
			punto.setY(punto.getY() - 0.1);
			cab.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.SO){
			cab.setDistanciaRecorrida(0.0);
			cab.setOrientacion(Orientation.NO);
		}else if(distanciaRecorrida<distancia && orientacion == Orientation.NO){
			punto.setX(punto.getX() - 0.1);
			punto.setY(punto.getY() + 0.1);
			cab.setDistanciaRecorrida(distanciaRecorrida +0.1);
		}else if(distanciaRecorrida>=distancia && orientacion == Orientation.NO){
			cab.setDistanciaRecorrida(0.0);
			cab.setOrientacion(Orientation.NE);
		}
		
	}

}
