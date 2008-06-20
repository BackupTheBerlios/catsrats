package es.ucm.fdi.collisionDetection.mathCollision;
import java.util.*;


/**
 *
 * Vertice
 * <BR><BR>
 * Clase que implementa la información a almacenar de un Vértice del Grafo
 * @see ListaAristas
 *
 * @author  Fran García   fgarcia@ua.es
 */
public class Vertice{
	
	public int id;
	public float px;
	public float py;
	public ListaAristas aristasAsociadas;
	
	
	/**
	 * Constructor de la clase Vertice()
	 * @param vid
	 * identificador para el vértice
	 * @param puntox
	 * pixel x para el vértice
	 * @param puntoy
	 * pixel y para el vértice
	 *
	 */
	public Vertice(int vid, float puntox, float puntoy){
		id = vid;
		px = puntox;
		py = puntoy;
		aristasAsociadas = new ListaAristas();
	}
	
	
	/**
	 * Constructor vacio de la clase Vertice()
	 *
	 */
	public Vertice(){
		id = 0;
		px = 0;
		py = 0;
		aristasAsociadas = new ListaAristas();
	}
	
	
	/**
	 * Constructor de copia de la clase Vertice()
	 * @param v
	 * Vertice a copiar
	 *
	 */
	public Vertice(Vertice v){
		id = v.id;
		px = v.px;
		py = v.py;
		aristasAsociadas = new ListaAristas(v.aristasAsociadas);
	}
	
	
	/**
	 * getID(). Obtengo el id del vértice
	 * @return int
	 * identificador del vértice
	 *
	 */
	public int getID(){
		return id;
	}
	
	
	/**
	 * getPX(). Obtengo el valor del punto x 
	 * @return float
	 * coordenada x del vértice
	 *
	 */
	public float getPX(){
		return px;
	}
	
	
	/**
	 * getPY(). Obtengo el valor del punto y
	 * @return float
	 * coordenada y del vértice
	 *
	 */
	public float getPY(){
		return py;
	}
	
	
	/**
	 * getAristasAsociadas(). Obtengo la ListaAristas de aristas asociadas
	 * @return ListaAristas
	 * las aristas asociadas al vértice dado
	 *
	 */
	public ListaAristas getAristasAsociadas(){
		return aristasAsociadas;
	}
	
	
	/**
	 * anyadeArista(). Anyado una arista asociada al vertice dado
	 * @param va
	 * VerticeAsociado a añadir como arista al vértice dado
	 *
	 */
	public void anyadeArista(VerticeAsociado va){
		aristasAsociadas.anyadeVerticeAsociado(va);
	}
	
	
	/**
	 * eliminaUltimaArista(). Elimina la ultima arista del Vertice dado
	 *
	 */
	public void eliminaUltimaArista(){
		aristasAsociadas.borraUltimaArista();
	}
	
}