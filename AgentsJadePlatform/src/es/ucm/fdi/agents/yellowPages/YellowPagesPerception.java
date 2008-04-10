package es.ucm.fdi.agents.yellowPages;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class YellowPagesPerception {
	
	public YellowPagesPerception(){
		
	}
	public AID[] buscarServicio(String tipoServicio, Agent agente) {
		AID[] listaAgentes = null;
		
		//Paginas amarillas->busca agentes de comunicacion que envian coordenadas (por ejemplo el AgenteServidor)
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(tipoServicio); //Tipo de servicio buscado
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(agente, template);
			listaAgentes = new AID[result.length];
			for(int i = 0; i<result.length; i++){
				listaAgentes[i] = result[i].getName();
			}
			
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaAgentes;
		
	}
}
