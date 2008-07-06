package es.ucm.fdi.agents;


import org.apache.log4j.Logger;

import com.sun.j3d.utils.applet.MainFrame;

import es.ucm.fdi.agents.behaviours.CollisionDetectionBehaviour;
import es.ucm.fdi.collisionDetection.Java3d;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/*
 * Este agente debe recibir ArrayList<Agente> agentes con la descripción de los agentes que hay en el entorno.
 */
public class CollisionDetectionAgent extends Agent{	
	
	private static int m_kWidth = 400;	
	private static int m_kHeight = 400;
	
	private Java3d j3d;
	private DFAgentDescription dfd;
	private CollisionDetectionBehaviour cdb;
	static Logger logger = Logger.getLogger(CollisionDetectionAgent.class);
	
	//Inicializaciones del agente
	protected void setup(){
		
		//Registramos el agente en las paginas amarillas:
		dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("deteccion-colisiones");  //Tipo del servicio que ofrece el agente en las paginas amarillas
		sd.setName("colisiones");  //Nombre del servicio ofrecido
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {			
			e.printStackTrace();
		}
		
		logger.info("Agente "+getAID().getName()+" está listo");
		
		cdb= new CollisionDetectionBehaviour(this, 50);		
		
		addBehaviour(cdb);//añadimos el comportamiento JADE.
		
		j3d= new Java3d(null);//Este null es para la lista de agentes, que al principio es vacia.
		//j3d.createSceneBranchGroup();
		j3d.initJava3d();
		
		MainFrame v= new MainFrame(j3d, m_kWidth, m_kHeight);		
	}		
		
	protected void takeDown(){
		//Desregistramos el agente:
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		logger.info("SE HA MATADO AL AGENTE DE DETECCION DE COLISIONES");
	}

	public Java3d getJ3d() {
		return j3d;
	}
}
