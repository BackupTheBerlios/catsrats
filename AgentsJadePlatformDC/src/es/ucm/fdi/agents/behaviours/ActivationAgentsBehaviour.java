package es.ucm.fdi.agents.behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ActivationAgentsBehaviour extends OneShotBehaviour{
	
	private ComunicationAgentBehaviour cab;
	
	public ActivationAgentsBehaviour(ComunicationAgentBehaviour c){
		this.cab = c;
	}
	
	public void action() {

		boolean activarAgentes = cab.getEstablecerComunicacion();
		
		if(activarAgentes){
			//Construye mensaje de tipo INFORM
			ACLMessage mensajeActivacion = new ACLMessage(ACLMessage.INFORM);
			//agregamos el contenido
			String contenidoMensaje= "comunicacion-lista";
			mensajeActivacion.setContent(contenidoMensaje);
			for(int i = 0; i<cab.getAgentesGeneradores().length; i++){
				String localName = cab.getAgentesGeneradores()[i].getLocalName();
				mensajeActivacion.addReceiver(new AID(localName,AID.ISLOCALNAME));
				myAgent.send(mensajeActivacion);				
			}
			for(int i = 0; i<cab.getAgentesDeteccionColisiones().length; i++){
				String localName = cab.getAgentesDeteccionColisiones()[i].getLocalName();
				mensajeActivacion.addReceiver(new AID(localName,AID.ISLOCALNAME));
				myAgent.send(mensajeActivacion);				
			}
			
			cab.setEstablecerComunicacion(false);
		}	
		
	}

}
