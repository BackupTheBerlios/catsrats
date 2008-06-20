package es.ucm.fdi.collisionDetection.mathCollision;
import java.util.*;

/**
 *
 * ListaAristas
 * <BR><BR>
 * Clase que contiene la informaci�n de aristas asociadas a un determinado Vertice
 *
 * @author  Fran Garc�a   fgarcia@ua.es
 */
public class ListaAristas{
	
	public Vector verticesAsociados;//se guardar�n los VerticesAsociados
	
	/**
	 * Constructor de la clase ListaAristas. Inicializa un vector de v�rtices asociados
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
	 * anyadeVerticeAsociado(). Anyade un v�rtice asociado
	 * @param arista
	 * VerticeAsociado arista a a�adir al v�rtice dado
	 *
	 */
	public void anyadeVerticeAsociado(VerticeAsociado arista){
		verticesAsociados.add(arista);
	}
	
	/**
	 * borraUltimaArista(). Borra el ultimo v�rtice asociado
	 *
	 */
	public void borraUltimaArista(){
		verticesAsociados.removeElementAt(verticesAsociados.size()-1);
	}
	
	/**
	 * getVerticesAsociados(). Obtengo los v�rtices asociados
	 * @return Vector
	 * vector con todos los vertices asociados
	 *
	 */
	public Vector getVerticesAsociados(){
		return verticesAsociados;
	}
	
	/**
	 * lengthVerticesAsociados(). Devuelvo el n�mero de v�rtices asociados
	 * @return int
	 * entero con la longitud de los v�rtices asociados
	 *
	 */
	public int lengthVerticesAsociados(){
		return verticesAsociados.size();
	}
	
	/**
	 * getVerticeAsociado(). Obtengo un determinado v�rtice asociado
	 * @param p
	 * int vertice asociado a devolver
	 * @return VerticeAsociado
	 *
	 */
	public VerticeAsociado getVerticeAsociado(int p){
		return (VerticeAsociado)verticesAsociados.elementAt(p);
	}
}