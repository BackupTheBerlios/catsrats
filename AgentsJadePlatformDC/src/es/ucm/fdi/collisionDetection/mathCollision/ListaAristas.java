package es.ucm.fdi.collisionDetection.mathCollision;
import java.util.*;

/**
 *
 * ListaAristas
 * <BR><BR>
 * Clase que contiene la información de aristas asociadas a un determinado Vertice
 *
 * @author  Fran García   fgarcia@ua.es
 */
public class ListaAristas{
	
	public Vector verticesAsociados;//se guardarán los VerticesAsociados
	
	/**
	 * Constructor de la clase ListaAristas. Inicializa un vector de vértices asociados
	 *
	 */
	public ListaAristas(){
		verticesAsociados = new Vector();
	}
	
	
	/**
	 * Constructor de copia de la clase ListaAristas
	 * @param la
	 * ListaAristas a copiar
	 *
	 */
	public ListaAristas(ListaAristas la){
		verticesAsociados = new Vector(la.verticesAsociados);
	}
	
	
	/**
	 * anyadeVerticeAsociado(). Anyade un vértice asociado
	 * @param arista
	 * VerticeAsociado arista a añadir al vértice dado
	 *
	 */
	public void anyadeVerticeAsociado(VerticeAsociado arista){
		verticesAsociados.add(arista);
	}
	
	/**
	 * borraUltimaArista(). Borra el ultimo vértice asociado
	 *
	 */
	public void borraUltimaArista(){
		verticesAsociados.removeElementAt(verticesAsociados.size()-1);
	}
	
	/**
	 * getVerticesAsociados(). Obtengo los vértices asociados
	 * @return Vector
	 * vector con todos los vertices asociados
	 *
	 */
	public Vector getVerticesAsociados(){
		return verticesAsociados;
	}
	
	/**
	 * lengthVerticesAsociados(). Devuelvo el número de vértices asociados
	 * @return int
	 * entero con la longitud de los vértices asociados
	 *
	 */
	public int lengthVerticesAsociados(){
		return verticesAsociados.size();
	}
	
	/**
	 * getVerticeAsociado(). Obtengo un determinado vértice asociado
	 * @param p
	 * int vertice asociado a devolver
	 * @return VerticeAsociado
	 *
	 */
	public VerticeAsociado getVerticeAsociado(int p){
		return (VerticeAsociado)verticesAsociados.elementAt(p);
	}
}