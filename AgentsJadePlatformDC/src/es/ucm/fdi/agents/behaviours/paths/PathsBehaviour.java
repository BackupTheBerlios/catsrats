package es.ucm.fdi.agents.behaviours.paths;

import es.ucm.fdi.agents.coordinates.Path;
import jade.core.behaviours.OneShotBehaviour;

public class PathsBehaviour extends OneShotBehaviour{
	
	public static final int NUMERO_TRAYECTORIAS = 7;
	public static final int OCTOGONAL = 0;
	public static final int DIAMANTE = 1;
	public static final int CUADRADA = 2;
	public static final int TRIANGULAR = 3;
	public static final int ZIGZAG = 4;
	public static final int BARRIDO = 5;
	public static final int NADA = 6;
	
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
		case TRIANGULAR: this.trayectoria = new TrianglePath();break;
		case ZIGZAG: this.trayectoria = new ZigzagPath();break;
		case BARRIDO: this.trayectoria = new BarridoPath();break;
		case NADA: this.trayectoria = new NadaPath();break;
		}
	}

	//Realizamos el movimiento de la trayetoria concreta
	public void action() {
		this.trayectoria.movimento(camino);
	}

}
