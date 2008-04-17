package es.ucm.fdi.agents;

import org.apache.log4j.Logger;

import es.ucm.fdi.agents.behaviours.CatAgentBehaviour;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;


public class CatAgent extends Agent{	
	
	private CatAgentBehaviour cab;
	private DFAgentDescription dfd;
	static Logger logger = Logger.getLogger(CatAgent.class);
	
	//Inicializaciones del agente
	protected void setup(){	
		
		//Paginas amarillas -> Se anuncia en las paginas amarillas ofreciendo un servicio de "generacion de coordenadas"
		dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("generacion-coordenadas");  //Tipo del servicio que ofrece el agente en las paginas amarillas
		sd.setName("coordenadas-gato");  //Nombre del servicio ofrecido
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("Agente "+getAID().getName()+" está listo");
		logger.info("Agente "+getAID().getName()+" está listo");
		cab = new CatAgentBehaviour(this, 1);
		addBehaviour(cab);		
	}
	
	protected void takeDown(){
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
		}
		System.out.println("Agente "+getAID().getName()+" terminando...");
		logger.info("Agente "+getAID().getName()+" terminando...");
	}
	
}//fin de clase CatAgent

