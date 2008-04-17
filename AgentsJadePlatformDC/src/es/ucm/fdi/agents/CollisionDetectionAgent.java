package es.ucm.fdi.agents;


import es.ucm.fdi.agents.behaviours.CollisionDetectionBehaviour;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/*
 * TODO Este agente debe recibir ArrayList<Agente> agentes con la descripción de los agentes que hay en el entorno.
 */
public class CollisionDetectionAgent extends Agent{	
	
	
	//ArrayList<InfoCollision> ic= null;
	
	private DFAgentDescription dfd;
	
	private CollisionDetectionBehaviour cdb;
	
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
		
		/*ArrayList<InfoAgent> agentes= new ArrayList();//TODO quitar estas lineas cuando reciba el arraylist
		InfoAgent agente1= new InfoAgent("gato1", 4, -8, 0, Orientation.SO);
		InfoAgent agente2= new InfoAgent("raton1", -2, 0, 0, Orientation.NE);
		//Los agentes que no se mueven no tienen focus y por tanto el atributo "orientación" no es necesario.
		InfoAgent agente3= new InfoAgent("mesa", 6.5, 0, 0, null);
		agentes.add(agente1);
		agentes.add(agente2);
		agentes.add(agente3);^*/
		
		
		//ic= new ArrayList<InfoCollision>();
		
		cdb= new CollisionDetectionBehaviour(this);		
		
		addBehaviour(cdb);//añadimos el comportamiento JADE.
		
		//MainFrame v= new MainFrame(cdb.getJ3d(), m_kWidth, m_kHeight);		
	}		
		
	protected void takeDown(){
		//Desregistramos el agente:
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		System.out.println("SE HA MATADO AL AGENTE DE DETECCION DE COLISIONES");
	}
}
