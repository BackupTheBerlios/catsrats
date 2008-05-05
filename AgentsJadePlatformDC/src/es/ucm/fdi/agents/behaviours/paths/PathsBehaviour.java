package es.ucm.fdi.agents.behaviours.paths;

import es.ucm.fdi.agents.coordinates.Path;
import jade.core.behaviours.OneShotBehaviour;

public class PathsBehaviour extends OneShotBehaviour{
	
	public static final int OCTOGONAL = 0;
	public static final int DIAMANTE = 1;
	public static final int CUADRADA = 2;
	
	private Trayectorias trayectoria;
	private Path camino;
	
	public PathsBehaviour(int tipoTrayectoria, Path camino){
		setTipoTrayectoria(tipoTrayectoria);
		this.camino = camino;
	}
	
	//Creamos la instancia concreta segun el tipo de trayectoria (Patron Strategy)
	private void setTipoTrayectoria(int tipoTrayectoria) {
		switch(tipoTrayectoria){
		case OCTOGONAL: this.trayectoria = new OctogonalPath();break;
		case DIAMANTE: this.trayectoria = new DiamondPath();break;
		case CUADRADA: this.trayectoria = new SquarePath();break;
		}
	}

	//Realizamos el movimiento de la trayetoria concreta
	public void action() {
		this.trayectoria.movimento(camino);
	}

}
