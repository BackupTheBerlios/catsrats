package es.ucm.fdi.collisionDetection.mathCollision;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.geom.Line2D.*;
import java.awt.geom.Point2D.*;


/**
 *
 * Recta
 * <BR><BR>
 * Clase que implementa los datos necesarios para una recta
 *
 * @author  Fran García   fgarcia@ua.es
 */
public class Recta{
	
	public Vertice origen;
	public Vertice destino;
	public float x;
	public float y;
	public float c;
	
	
	/**
	 * Constructor de la clase Recta()
	 *
	 */
	public Recta(){
		origen = new Vertice();
		destino = new Vertice();
		ecuacionGeneral();
	}
	
	
	/**
	 * Constructor de copia de la clase Recta()
	 * @param r
	 * Recta a copiar
	 *
	 */
	public Recta(Recta r){
		origen = new Vertice(r.getP1().getID(), r.getP1().getPX(), r.getP1().getPY());
		destino = new Vertice(r.getP2().getID(), r.getP2().getPX(), r.getP2().getPY());
		ecuacionGeneral();
	}
	
	
	/**
	 * Constructor de la clase Recta()
	 * @param porigen
	 * Vertice origen de la Recta
	 * @param pdestino
	 * Vertice destino de la Recta
	 *
	 */
	public Recta(Vertice porigen, Vertice pdestino){
		origen = new Vertice(porigen);
		destino = new Vertice(pdestino);
		ecuacionGeneral();
	}
	
	
	/**
	 * ecuacionGeneral(). Calcula los valores para m y n de la Recta
	 *
	 */
	public void ecuacionGeneral(){
		x = (float)((destino.getPY() - origen.getPY()) / (destino.getPX() - origen.getPX()));
		y = -1;
		c = (float)(origen.getPY() - origen.getPX()*x);
	}
	
	/**
	 * perpendicular(). Calcula los valores de m para la recta perpendicular que pasa por un Point2D.Float
	 * @param p
	 * Point2D.Float por el que tiene que pasar la recta perpendicular
	 * @return Recta
	 * recta perpendicular a la dada que pasa por el punto especificado
	 *
	 */
	public Recta perpendicular(Point2D.Float p){
		Recta r = new Recta();
		r.x = -1/this.x;
		r.y = -1;
		r.c = (float)p.getY() - r.x*(float)p.getX();
		//System.out.println(r.x+"X +"+r.y+"Y +"+r.c);
		return r;
	}
	
	
	/**
	 * interseccionRectas(). Calcula el punto de intersección entre 2 Rectas
	 * @param r
	 * Recta para calcular la interseccion
	 * @return Point2D.Float 
	 * punto de intersección de las dos rectas dadas
	 *
	 */
	public Point2D.Float interseccionRectas(Recta r){
		float puntox,puntoy;
		
		puntox = (this.c - r.c) / (r.x - this.x);
		puntoy = (this.x * puntox) + this.c;
		//System.out.println("Las rectas intersectan en ("+puntox+","+puntoy+")");
		
		Point2D.Float p = new Point2D.Float(puntox,puntoy);
		return p;
	}
	
	
	/**
	 * distancia(). Calcula la distancia de un Punto a una Recta dada
	 * @param p
	 * Point2D.Float para calcular la distancia a la recta dada
	 * @return float
	 * distancia calculada
	 *
	 */
	public float distancia(Point2D.Float p){
		Point2D.Float p1 = new Point2D.Float(this.origen.getPX(), this.origen.getPY());
		Point2D.Float p2 = new Point2D.Float(this.destino.getPX(), this.origen.getPY());
		
		Line2D.Float recta = new Line2D.Float(p1, p2);
		
		return (float)recta.ptSegDist(p);
	}
	
	
	/**
	 * getP1(). Obtengo el origen de la Recta
	 * @return Vertice
	 * el origen de la Recta
	 *
	 */
	public Vertice getP1(){
		return origen;
	}
	
	
	/**
	 * getP2(). Obtengo el fin de la Recta
	 * @return Vertice
	 * el destino de la Recta
	 */
	public Vertice getP2(){
		return destino;
	}
}