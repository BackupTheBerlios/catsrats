package agents.behaviours;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import socket.DatoSocket;
import socket.SocketServidor;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class FormatAndSendBehaviour extends OneShotBehaviour{
	
	private ComunicationAgentBehaviour cab;
	
	public FormatAndSendBehaviour(ComunicationAgentBehaviour c){
		this.cab = c;
	}
	public void action() {
		
		ACLMessage msg = cab.getMsg();
		if (msg!=null) {//si hay un mensaje

			System.out.println("Recibido por el agente servidor-> "+msg.getContent());//imprime el contenido
			
			String[] msjACL=msg.getContent().split(",");
			String objetoAMover=msjACL[0];
			double cx= Double.parseDouble(msjACL[1]);
			double cy= Double.parseDouble(msjACL[2]);
			double cz= Double.parseDouble(msjACL[3]);
			
			DecimalFormat coordenada = (DecimalFormat)NumberFormat.getNumberInstance(Locale.ENGLISH);
			cab.setCoordenada(coordenada); 
			cab.getCoordenada().applyPattern("###.###");

			String xFormateada = cab.getCoordenada().format(cx);
			String yFormateada = cab.getCoordenada().format(cy);
			String zFormateada = cab.getCoordenada().format(cz);
			
			cab.setXFormateada(xFormateada);
			cab.setYFormateada(yFormateada);
			cab.setZFormateada(yFormateada);
			
			int numGatos = 0;
			int numRatones = 0;
			for(int i = 0; i<cab.getAgentesGeneradores().length;i++){
				if(cab.getAgentesGeneradores()[i].getLocalName().contains("gato"))
					numGatos++;
				else if(cab.getAgentesGeneradores()[i].getLocalName().contains("raton"))
					numRatones++;
			}

			// Se preparan los datos para enviar.
			DatoSocket x = new DatoSocket(":"+numGatos+":"+numRatones+":"+objetoAMover+"("+xFormateada+",");
			DatoSocket y = new DatoSocket(yFormateada+",");
			DatoSocket z = new DatoSocket(zFormateada+")*"); //Con el * indicamos a C el final de la cadena

			SocketServidor socket = cab.getSocket();
			try {
				x.writeObject (socket.getBufferSalida());
				y.writeObject (socket.getBufferSalida());
				z.writeObject (socket.getBufferSalida());
			} catch (IOException e) {
				socket.cerrarSocket();
				System.out.println("SE HA CERRADO EL SOCKET CORRECTAMENTE");
				ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
				//agrega contenido: queremos matar a los agentes de percepcion
				String contenido = "morir";
				mensaje.setContent(contenido);
				for(int i = 0; i<cab.getAgentesGeneradores().length; i++){
					String localName = cab.getAgentesGeneradores()[i].getLocalName();
					mensaje.addReceiver(new AID(localName,AID.ISLOCALNAME));
					myAgent.send(mensaje);
					
				}
				myAgent.doDelete(); //Matamos al agente socket
				//System.exit(0);
			}

		}
		else {
			System.out.println("BLOQUEAMOS AL AGENTE SERVIDOR");
			block();  //Si no recibimos mensaje se bloquea el agente hasta que le llegue un mensaje
		}

	}

}
