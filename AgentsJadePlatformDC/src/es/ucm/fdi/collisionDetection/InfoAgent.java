package es.ucm.fdi.collisionDetection;

import java.io.Serializable;

public class InfoAgent implements Serializable {
	//public enum Orientacion {N, NE, E, SE, S, SO, O, NO};
	
	private String nombreAgente;
	private double x;
	private double y;
	private double z;
	private Orientation orientacion;
		
	public InfoAgent(String nombreAgente, double x, double y, double z, Orientation orientacion) {
		super();		
		this.nombreAgente = nombreAgente;
		this.x = x;
		this.y = y;
		this.z = z;	
		this.orientacion= orientacion;
	}
	
	public InfoAgent() {
	}

	public String getNombreAgente() {
		return nombreAgente;
	}
	public void setNombreAgente(String nombreAgente) {
		this.nombreAgente = nombreAgente;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}

	public Orientation getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(Orientation orientacion) {
		this.orientacion= orientacion;
	}
	

}
