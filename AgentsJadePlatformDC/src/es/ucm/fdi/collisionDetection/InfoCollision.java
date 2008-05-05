package es.ucm.fdi.collisionDetection;

import java.io.Serializable;

import es.ucm.fdi.mcdm.OrientacionAgenteVisto;

public class InfoCollision implements Serializable {
	
	private String agenteQueVe;
	private String agenteQueEsVisto;
	
	private String tipoAgente;
	private OrientacionAgenteVisto orientacion;
	private double claridadPercepcion;
	private double distancia;
	
	
	//Constructora con parámetros:
	public InfoCollision(String agenteQueVe, String agenteQueEsVisto, String tipoAgente, OrientacionAgenteVisto orientacion, double claridadPercepcion, double distancia) {		
		this.agenteQueVe = agenteQueVe;
		this.agenteQueEsVisto = agenteQueEsVisto;
		this.tipoAgente= tipoAgente;
		this.orientacion= orientacion;
		this.claridadPercepcion = claridadPercepcion;
		this.distancia= distancia;
	}
	
	//Getter y setter:
	public String getAgenteQueEsVisto() {
		return agenteQueEsVisto;
	}
	public void setAgenteQueEsVisto(String agenteQueEsVisto) {
		this.agenteQueEsVisto = agenteQueEsVisto;
	}
	public String getAgenteQueVe() {
		return agenteQueVe;
	}
	public void setAgenteQueVe(String agenteQueVe) {
		this.agenteQueVe = agenteQueVe;
	}
	public double getClaridadPercepcion() {
		return claridadPercepcion;
	}
	public void setClaridadPercepcion(double claridadPercepcion) {
		this.claridadPercepcion = claridadPercepcion;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public String getTipoAgente() {
		return tipoAgente;
	}

	public void setTipoAgente(String tipoAgente) {
		this.tipoAgente = tipoAgente;
	}

	public OrientacionAgenteVisto getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(OrientacionAgenteVisto orientacion) {
		this.orientacion = orientacion;
	}
	
	
	
	
	
}
