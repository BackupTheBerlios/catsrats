package es.ucm.fdi.agents;


import org.apache.log4j.Logger;

import es.ucm.fdi.agents.behaviours.ComunicationAgentBehaviour;
import es.ucm.fdi.socket.SocketServidor;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ComunicationAgent extends Agent{
	
	private ComunicationAgentBehaviour cab;
	private DFAgentDescription dfd;
	static Logger logger = Logger.getLogger(ComunicationAgent.class);
	
	//Inicializaciones del agente
	protected void setup(){
			
		//Paginas amarillas -> Se anuncia en las paginas amarillas ofreciendo un servicio de "envio-coordenadas"
		dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("envio-coordenadas");  //Tipo del servicio que ofrece el agente en las paginas amarillas
		sd.setName("agente-comunicacion");  //Nombre del servicio ofrecido
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Agente "+getAID().getName()+" está listo");
		logger.info("Agente "+getAID().getName()+" está listo");
		
		//Creamos el socket servidor y lo dejamos preparado a la escucha		
		final SocketServidor socketServ= new SocketServidor();
		cab = new ComunicationAgentBehaviour(this, 30, socketServ);
		addBehaviour(cab);
	}
	
	protected void takeDown(){
		
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Agente "+getAID().getName()+" terminando...");
		logger.info("Agente "+getAID().getName()+" terminando...");
		
	}
}





