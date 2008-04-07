package agents;


import agents.behaviours.ComunicationAgentBehaviour;
import socket.SocketServidor;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ComunicationAgent extends Agent{
	
	private ComunicationAgentBehaviour cab;
	private DFAgentDescription dfd;
	
	//Inicializaciones del agente
	protected void setup(){
			
		//Paginas amarillas
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
		
		//Creamos el socket servidor y lo dejamos preparado a la escucha		
		final SocketServidor socketServ=new SocketServidor();
		cab = new ComunicationAgentBehaviour(this, 100, socketServ, true);
		addBehaviour(cab);
	}
	
	protected void takeDown(){
		System.out.println("Agente "+getAID().getName()+" terminando...");
	}
}





