package es.ucm.fdi.collisionDetection.mathCollision;
import java.util.*;

/**
 *
 * VerticeAsociado
 * <BR><BR>
 * Clase que implementa la información a almacenar de los vértices asociados 
 * como aristas a un vértice 
 *
 * @author  Fran García   fgarcia@ua.es
 */
public class VerticeAsociado{
	
	public int vertice;
	public float peso;
	
	/**
	 * Constructor de la clase VerticeAsociado
	 * @param idVertice
	 * identificador del vértice
	 * @param distancia
	 * distancia del vértice asociado al vértice especificado 
	 *
	 */
	public VerticeAsociado(int idVertice, float distancia){
		vertice = idVertice;
		peso = distancia;
	}
	
	
	/**
	 * getVertice(). Obtengo el vértice asociado al dado
	 * @return int
	 * identificador del vértice
	 *
	 */
	public int getVertice(){
		return vertice;
	}
	
	
	/**
	 * getPeso(). Obtengo el peso entre el vértice asociado y el dado
	 * @return float
	 * distancia desde el vértice asociado al vértice
	 *
	 */
	public float getPeso(){
		return peso;
	}
}