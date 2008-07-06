package es.ucm.fdi.collisionDetection.mathCollision;
import java.awt.geom.Point2D.Float;

import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

import es.ucm.fdi.agents.behaviours.CatAgentBehaviour;
import es.ucm.fdi.collisionDetection.Orientation;


public class MathCollision {

	public static final double DISTANCIA1 = 3.54;//valor de los catetos del triangulo que se forma desde el centro hasta el vertice A.
	
	public static final double DISTANCIA2 = 7.07;// valor de la hipotenusa del triangulo rectangulo cuyos vértices son 
										//el centro del triangulo, el centro de la base y el vértice zurdo de la base
	static Logger logger = Logger.getLogger(MathCollision.class);
	
	/**
	 * Función que dado el centro del cono (triangulo) te devuelve los vértices.
	 */
	private static Vector3d[] dameVerticesCono(Orientation or, Vector3d centro){
		Vector3d a= new Vector3d();
		Vector3d b= new Vector3d();
		Vector3d c= new Vector3d();
				
		c.setZ(centro.z);
		
		if(or == Orientation.N){
			a.setX(centro.x);
			a.setY(centro.y - 5);
						
			b.setX(centro.x - 5);
			b.setY(centro.y + 5);
						
			c.setX(centro.x + 5);
			c.setY(centro.y + 5);					
		}
		else if(or == Orientation.S){
			a.setX(centro.x);
			a.setY(centro.y + 5);
			
			b.setX(centro.x + 5);
			b.setY(centro.y - 5);
						
			c.setX(centro.x - 5);
			c.setY(centro.y - 5);
		}
		else if(or == Orientation.E){
			a.setX(centro.x - 5);
			a.setY(centro.y);
			
			b.setX(centro.x + 5);
			b.setY(centro.y + 5);
			
			c.setX(centro.x + 5);
			c.setY(centro.y - 5);
		}
		else if(or == Orientation.O){
			a.setX(centro.x + 5);
			a.setY(centro.y);
			
			b.setX(centro.x - 5);
			b.setY(centro.y - 5);
			
			c.setX(centro.x - 5);
			c.setY(centro.y + 5);
		}
		else if(or == Orientation.NE){
			a.setX(centro.x - DISTANCIA1);
			a.setY(centro.y - DISTANCIA1);
			
			b.setX(centro.x);
			b.setY(centro.y + DISTANCIA2);
			
			c.setX(centro.x + DISTANCIA2);
			c.setY(centro.y);
		}
		else if(or == Orientation.NO){
			a.setX(centro.x + DISTANCIA1);
			a.setY(centro.y - DISTANCIA1);
			
			b.setX(centro.x - DISTANCIA2);
			b.setY(centro.y);
			
			c.setX(centro.x);
			c.setY(centro.y + DISTANCIA2);
		}
		else if(or == Orientation.SE){
			a.setX(centro.x - DISTANCIA1);
			a.setY(centro.y + DISTANCIA1);
			
			b.setX(centro.x + DISTANCIA2);
			b.setY(centro.y);
			
			c.setX(centro.x);
			c.setY(centro.y - DISTANCIA2);
		}
		else if(or == Orientation.SO){
			a.setX(centro.x + DISTANCIA1);
			a.setY(centro.y + DISTANCIA1);
			
			b.setX(centro.x);
			b.setY(centro.y - DISTANCIA2);
			
			c.setX(centro.x - DISTANCIA2);
			c.setY(centro.y);
		}
		
		Vector3d[] res= new Vector3d[3];
		res[0]= a;
		res[1]= b;
		res[2]= c;
		
		return res;
	}
	
	/**
	 * Función que recibe la orientación del cono, el centro del cono y el centro del circulo y responde
	 * si ha habido colisión entre el cono y la esfera indicando si ha sido por el "centro", "izquierda" o "derecha".
	 * Si no ha habido colision devuelve null
	 * @param or
	 * @param centroCono
	 * @param centroCirculo
	 * @return
	 */
	public String detectaColision(Orientation or, Vector3d centroCono, Vector3d centroCirculo){
		
		String parteConoColisionada= null;
		
		Vector3d[] res= dameVerticesCono(or,centroCono);
		
		//Guardo cada vertice del triangulo en un objeto Vertice:
		Vertice v1= new Vertice(1, (float)res[0].x, (float)res[0].y);
		Vertice v2= new Vertice(2, (float)res[1].x, (float)res[1].y);
		Vertice v3= new Vertice(3, (float)res[2].x, (float)res[2].y);
		
		//Construyo las 3 rectas que unen los 3 vertices:
		Recta r1= new Recta(v1, v2, or);
		Recta r2= new Recta(v2, v3, or);
		Recta r3= new Recta(v3, v1, or);
		
		//Guardo el centro del cono y el del circulo en objetos Vertice:
		Vertice cco= new Vertice(4, (float)centroCono.x, (float)centroCono.y);
		Vertice cci= new Vertice(5, (float)centroCirculo.x, (float)centroCirculo.y);
		
		//Construyo la recta que une los dos centros:
		Recta rectaUneCentros = new Recta(cco, cci, or);
		
		//Ahora miro si esta recta intersecta con alguna de las aristas del triangulo (r1, r2, r3):
		Float interseccionR1= rectaUneCentros.interseccionRectas(r1);
		Float interseccionR2= rectaUneCentros.interseccionRectas(r2);
		Float interseccionR3= rectaUneCentros.interseccionRectas(r3);
		
		Vertice interR1 = new Vertice(6, interseccionR1.x, interseccionR1.y);
		Vertice interR2 = new Vertice(7, interseccionR2.x, interseccionR2.y);
		Vertice interR3 = new Vertice(8, interseccionR3.x, interseccionR3.y);
		
		logger.info("Intersecta con la recta 1 en: "+interseccionR1);
		logger.info("Intersecta con la recta 2 en: "+interseccionR2);
		logger.info("Intersecta con la recta 3 en: "+interseccionR3);
		
		double d1= Double.MAX_VALUE;
		double d2= Double.MAX_VALUE;
		double d3= Double.MAX_VALUE;
		
		if(this.estaEnTriangulo(v1, v2, v3, interR1))
			d1= calculaDistanciaEuclidea(cci, interR1);
				
		if(this.estaEnTriangulo(v1, v2, v3, interR2))
			d2= calculaDistanciaEuclidea(cci, interR2);
		
		if(this.estaEnTriangulo(v1, v2, v3, interR3))
			d3= calculaDistanciaEuclidea(cci, interR3);
		
		logger.info("Distancia a la recta 1: "+d1);
		logger.info("Distancia a la recta 2: "+d2);
		logger.info("Distancia a la recta 3: "+d3);
		double dv1cci= calculaDistanciaEuclidea(v1, cci);
		double dv2cci= calculaDistanciaEuclidea(v2, cci);
		double dv3cci= calculaDistanciaEuclidea(v3, cci);
		
		if((d1 < 5.0) || (dv1cci < 5.0) || (dv2cci < 5.0) ||(dv3cci < 5.0)) 
			parteConoColisionada= determinaOrientacionR1(v1, v2, interR1);//"centro";
		else if((d2 < 5.0) || (dv1cci < 5.0) || (dv2cci < 5.0) ||(dv3cci < 5.0)){			
			parteConoColisionada= determinaOrientacionR2(v2, v3, interR2);
		}
		else if((d3 < 5.0) || (dv1cci < 5.0) || (dv2cci < 5.0) ||(dv3cci < 5.0)) 
			parteConoColisionada= determinaOrientacionR3(v1, v3, interR3);//"centro";
		
		logger.info("Colision en: "+parteConoColisionada);
		
		return parteConoColisionada;		
	}
	
	private String determinaOrientacionR1(Vertice v1, Vertice v2, Vertice interR){
		String res= "centro";
		Vertice puntoMedio= new Vertice(9, (v1.px+v2.px)/2, (v1.py+v2.py)/2);//no es un vertice
						
		double d1= this.calculaDistanciaEuclidea(v1, interR);
		double d2= this.calculaDistanciaEuclidea(puntoMedio, interR);
		double d3= this.calculaDistanciaEuclidea(v2, interR);
		
		if(d3 < d1 && d3 < d2) 
			res= "izquierda";
		else 
			res= "centro";
		
		return res;
	}
	
	private String determinaOrientacionR2(Vertice v2, Vertice v3, Vertice interR2){
		String res= "centro";
		Vertice puntoMedio= new Vertice(9, (v2.px+v3.px)/2, (v2.py+v3.py)/2);//no es un vertice
						
		double dI= this.calculaDistanciaEuclidea(v2, interR2);
		double dC= this.calculaDistanciaEuclidea(puntoMedio, interR2);
		double dD= this.calculaDistanciaEuclidea(v3, interR2);
		
		if(dI < dC && dI < dD) 
			res= "izquierda";
		else if(dD < dI && dD < dC) 
			res= "derecha";
		
		return res;
	}	
	
	private String determinaOrientacionR3(Vertice v1, Vertice v3, Vertice interR){
		String res= "centro";
		Vertice puntoMedio= new Vertice(9, (v1.px+v3.px)/2, (v1.py+v3.py)/2);//no es un vertice
						
		double d1= this.calculaDistanciaEuclidea(v1, interR);
		double d2= this.calculaDistanciaEuclidea(puntoMedio, interR);
		double d3= this.calculaDistanciaEuclidea(v3, interR);
		
		if(d3 < d1 && d3 < d2) 
			res= "derecha";
		else 
			res= "centro";
		
		return res;
	}
	
	/*
	 * Funcion que se encarga de calcular la distancia euclídea entre dos vectores:
	 */
	private double calculaDistanciaEuclidea(Vertice v1,  Vertice v2){
		double d= Math.sqrt(Math.pow(v2.px- v1.px,2) + Math.pow(v2.py - v1.py,2));
		return d;
	}
	
	//área del triángulo  A1A2A3, es > 0 sii esta orientado positivamente,else < 0
	private double areaTriangulo(Vertice A1, Vertice A2, Vertice A3){
		
     return (A1.px - A3.px)*(A2.py - A3.py)-(A1.py - A3.py)*(A2.px - A3.px);

    }

	// Decide si un punto P está dentro del triángulo orientado A1A2A3
	private boolean estaEnTriangulo(Vertice  A1, Vertice A2, Vertice A3, Vertice P){

     boolean resultado;

     if(areaTriangulo(A1,A2,A3)>=0)
         resultado =     areaTriangulo(A1, A2, P) >= 0 &&
                         areaTriangulo(A2, A3, P) >= 0 &&
                         areaTriangulo(A3, A1, P) >= 0;
         else resultado =areaTriangulo(A1, A2, P) <= 0 &&
                         areaTriangulo(A2, A3, P) <= 0 &&
                         areaTriangulo(A3, A1, P) <= 0;
     return resultado;  

 }
	
}
