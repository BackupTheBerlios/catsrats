package es.ucm.fdi.collisionDetection.mathCollision;
import java.util.*;

/**
 *
 * VerticeAsociado
 * <BR><BR>
 * Clase que implementa la informaci�n a almacenar de los v�rtices asociados 
 * como aristas a un v�rtice 
 *
 * @author  Fran Garc�a   fgarcia@ua.es
 */
public class VerticeAsociado{
	
	public int vertice;
	public float peso;
	
	/**
	 * Constructor de la clase VerticeAsociado
	 * @param idVertice
	 * identificador del v�rtice
	 * @param distancia
	 * distancia del v�rtice asociado al v�rtice especificado 
	 *
	 */
	public VerticeAsociado(int idVertice, float distancia){
		vertice = idVertice;
		peso = distancia;
	}
	
	
	/**
	 * getVertice(). Obtengo el v�rtice asociado al dado
	 * @return int
	 * identificador del v�rtice
	 *
	 */
	public int getVertice(){
		return vertice;
	}
	
	
	/**
	 * getPeso(). Obtengo el peso entre el v�rtice asociado y el dado
	 * @return float
	 * distancia desde el v�rtice asociado al v�rtice
	 *
	 */
	public float getPeso(){
		return peso;
	}
}