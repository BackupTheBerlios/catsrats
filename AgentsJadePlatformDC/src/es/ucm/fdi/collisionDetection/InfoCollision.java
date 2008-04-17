package es.ucm.fdi.collisionDetection;

public class InfoCollision {
	
	private String agenteQueVe;
	private String agenteQueEsVisto;
	private double claridadPercepcion;
	
	//Constructora con parámetros:
	public InfoCollision(String agenteQueVe, String agenteQueEsVisto, double claridadPercepcion) {		
		this.agenteQueVe = agenteQueVe;
		this.agenteQueEsVisto = agenteQueEsVisto;
		this.claridadPercepcion = claridadPercepcion;
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
	
	
	
	
	
}
