package es.ucm.fdi.agents.behaviours;

import java.io.IOException;

import es.ucm.fdi.agents.behaviours.paths.PathsBehaviour;
import es.ucm.fdi.agents.coordinates.Path;
import es.ucm.fdi.agents.coordinates.Point;
import es.ucm.fdi.agents.yellowPages.YellowPages;
import es.ucm.fdi.collisionDetection.InfoAgent;
import es.ucm.fdi.collisionDetection.InfoCollision;
import es.ucm.fdi.collisionDetection.Orientation;
import es.ucm.fdi.mcdm.DecisionMaker;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class CatAgentBehaviour extends TickerBehaviour{
	
	public static final double DISTANCIA = 10;//2000.0;
	
	private Point punto;
	private String nombre;
	private AID[] listaAgentesComunicacion;
	private AID[] listaAgentesDeteccionColisiones;
	private YellowPages paginasAmarillas;
	private boolean activado;
	private Path camino;
	private PathsBehaviour comportamientoTrayectorias;
	private int tipoTrayectoria;
	private String decisionAnterior;

	public CatAgentBehaviour(Agent agente, long tiempo) {
		super(agente, tiempo);
		this.punto= new Point(0,0,0);//new Point(Math.random()*200, Math.random()*200, 0.0);
		this.nombre= myAgent.getLocalName();
		this.paginasAmarillas= new YellowPages();
		this.activado= false;
		this.camino = null;
		this.tipoTrayectoria = (int)((Math.random()*10)%PathsBehaviour.NUMERO_TRAYECTORIAS); //Generamos una trayectoria aleatoria //PathsBehaviour.DIAMANTE;
		this.comportamientoTrayectorias = null;
		this.decisionAnterior= "no hacer nada";
	}

	public String generaCoordenadas(){

		switch(tipoTrayectoria){
		case PathsBehaviour.OCTOGONAL:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.E;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.OCTOGONAL, camino);
			}
		}break;
		case PathsBehaviour.DIAMANTE:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.NE;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.DIAMANTE, camino);
			}
		}break;
		case PathsBehaviour.CUADRADA:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.E;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.CUADRADA, camino);
			}
		}break;
		case PathsBehaviour.TRIANGULAR:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.O;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.TRIANGULAR, camino);
			}
		}break;
		case PathsBehaviour.ZIGZAG:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.NO;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.ZIGZAG, camino);
			}
		}break;
		case PathsBehaviour.BARRIDO:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.E;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.BARRIDO, camino);
			}
		}break;
		case PathsBehaviour.NADA:{
			if(comportamientoTrayectorias == null){
				Orientation orientacion = Orientation.E;
				camino = new Path(0.0, DISTANCIA, orientacion, punto);
				comportamientoTrayectorias = new PathsBehaviour(PathsBehaviour.NADA, camino);
			}
		}break;
		}

		myAgent.addBehaviour(comportamientoTrayectorias);
		
	    String mensaje= nombre+","+camino.getPunto().getX()+","+camino.getPunto().getY()+","+camino.getPunto().getZ()+","+camino.getOrientacion();
	    
	    return mensaje;			
	}

	protected void onTick() {
		
		listaAgentesComunicacion = paginasAmarillas.buscarServicio("envio-coordenadas", myAgent);
		listaAgentesDeteccionColisiones = paginasAmarillas.buscarServicio("deteccion-colisiones", myAgent);
		
		//Estamos a la escucha para recibir algun mansaje procedente del Agente Servidor
		ACLMessage mensajeEntrante = myAgent.receive();
		if(mensajeEntrante != null){
			if(mensajeEntrante.getPerformative() == ACLMessage.REQUEST){ //Recibimos un mensaje InfoCollision
				try {
					InfoCollision info = (InfoCollision)mensajeEntrante.getContentObject();
					//Dentro del try está la parte relativa a la toma de decisiones:
					String decision;
					if(this.decisionAnterior.equals("perseguir"))
						decision= DecisionMaker.tomaDeDecisionesGato(info.getTipoAgente(), info.getOrientacion(), info.getClaridadPercepcion(), info.getDistancia(), this.decisionAnterior, 0.3, 0.1, 0.1, 0.1, 0.4);
					else
						decision= DecisionMaker.tomaDeDecisionesGato(info.getTipoAgente(), info.getOrientacion(), info.getClaridadPercepcion(), info.getDistancia(), this.decisionAnterior, 0.5, 0.1, 0.1, 0.1, 0.2);
					
					System.out.println("--> DECISION TOMADA POR EL GATO: "+decision);
					String contenido;
					if(decision.equals("perseguir")){
						contenido = generaCoordenadasPerseguir(info);
						comportamientoTrayectorias= null;
					}
					else if(decision.equals("esquivar")){
						contenido = generaCoordenadasEsquivar(info);
						comportamientoTrayectorias= null;
					}
					else{//"no hacer nada"
						switch(tipoTrayectoria){
							case PathsBehaviour.OCTOGONAL: camino.setOrientacion(Orientation.E); break;
							case PathsBehaviour.DIAMANTE: camino.setOrientacion(Orientation.NE); break;
							case PathsBehaviour.CUADRADA: camino.setOrientacion(Orientation.E); break;
							case PathsBehaviour.TRIANGULAR: camino.setOrientacion(Orientation.O); break;
							case PathsBehaviour.ZIGZAG: camino.setOrientacion(Orientation.NO); break;
							case PathsBehaviour.BARRIDO: camino.setOrientacion(Orientation.E); break;//nuevo
						}						
						contenido = generaCoordenadas();
					}	
					
					for(int i = 0; i<listaAgentesComunicacion.length && listaAgentesDeteccionColisiones.length>0; i++){
						if(activado) nuevoMensaje(listaAgentesComunicacion[i].getLocalName(),contenido);
						mensajeInfoAgente(listaAgentesDeteccionColisiones[i].getLocalName());
						System.out.println("GATO EN "+camino.getPunto().getX()+" "+camino.getPunto().getY()+" "+camino.getPunto().getZ()+" "+camino.getOrientacion());
					}
					
					//Almacenamos la decisión para tenerla en cuenta la vez siguiente: es un criterio.
					this.decisionAnterior= decision;					
					
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
			else{
				System.out.println("Mensaje entrante del gato: "+mensajeEntrante.getContent());
				String contenidoMensaje = mensajeEntrante.getContent();
				if(contenidoMensaje.contains("morir")){ //Matamos a los agentes
					myAgent.doDelete();
				}			
				else if(contenidoMensaje.contains("comunicacion-lista")){	//Permitimos que empiecen a generar coordenadas				
					activado= true;
				}
				else if(contenidoMensaje.contains("genera")){	//Permitimos que sigan generando coordenadas
					String contenido = generaCoordenadas();
					for(int i = 0; i<listaAgentesComunicacion.length && listaAgentesDeteccionColisiones.length>0; i++){
						if(activado) nuevoMensaje(listaAgentesComunicacion[i].getLocalName(), contenido);
						mensajeInfoAgente(listaAgentesDeteccionColisiones[i].getLocalName());
						System.out.println("GATO EN "+camino.getPunto().getX()+" "+camino.getPunto().getY()+" "+camino.getPunto().getZ()+" "+camino.getOrientacion());
					}
				}
			}
		}		
		else {  //Permanecemos a la espera de que llegue un mensaje
			block();
		}		
	}
	
	private String generaCoordenadasEsquivar(InfoCollision info) {
		double distancia= 0.5;
		String parteConoColisionada= info.getParteConoColisionada();
		switch(camino.getOrientacion()){
		case N: {if(parteConoColisionada.equals("izquierda")){
			 		camino.setOrientacion(Orientation.NE);
			 		camino.getPunto().setX(camino.getPunto().getX()+distancia);
			 		camino.getPunto().setY(camino.getPunto().getY()+distancia);
				 }
			   	 else if(parteConoColisionada.equals("centro")){
			   		camino.setOrientacion(Orientation.O);
			 		camino.getPunto().setX(camino.getPunto().getX()-distancia);
			   	 }
				 else if(parteConoColisionada.equals("derecha")){					
					 camino.setOrientacion(Orientation.NO);
					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);
				 }
				 break;
				}
		case NE:{if(parteConoColisionada.equals("izquierda")){ 
					camino.setOrientacion(Orientation.E);
					camino.getPunto().setX(camino.getPunto().getX()+distancia);					
	        	 }
				 else if(parteConoColisionada.equals("centro")){
					 camino.setOrientacion(Orientation.NO);
					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);
				 }
		         else if(parteConoColisionada.equals("derecha")){
		        	 camino.setOrientacion(Orientation.N);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);		        	 
		         }
				 break;
		        }
		case E:{if(parteConoColisionada.equals("izquierda")){
					camino.setOrientacion(Orientation.SE);
					camino.getPunto().setX(camino.getPunto().getX()+distancia);
					camino.getPunto().setY(camino.getPunto().getY()-distancia);
				}
		 		else if(parteConoColisionada.equals("centro")){
		 			 camino.setOrientacion(Orientation.N);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);
		 		}
		 		else if(parteConoColisionada.equals("derecha")){
		 			 camino.setOrientacion(Orientation.NE);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);		 			
		 		}
				break;
        	   }
		case SE:{if(parteConoColisionada.equals("izquierda")){
			 		camino.setOrientacion(Orientation.S);
				    camino.getPunto().setY(camino.getPunto().getY()-distancia);
				 }
 				 else if(parteConoColisionada.equals("centro")){
 					 camino.setOrientacion(Orientation.NE);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);	
 				 }
 				 else if(parteConoColisionada.equals("derecha")){
 					 camino.setOrientacion(Orientation.E);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia); 					
 				 }
				 break;
				}
		case S:{if(parteConoColisionada.equals("izquierda")){
					camino.setOrientacion(Orientation.SO);
					camino.getPunto().setX(camino.getPunto().getX()-distancia);
					camino.getPunto().setY(camino.getPunto().getY()-distancia);
				}
		 		else if(parteConoColisionada.equals("centro")){
		 			 camino.setOrientacion(Orientation.E);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
		 		}
			    else if(parteConoColisionada.equals("derecha")){
			    	 camino.setOrientacion(Orientation.SE);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);			    	 
			    }
				break;
			   }
		case SO:{if(parteConoColisionada.equals("izquierda")){
					camino.setOrientacion(Orientation.O);
					camino.getPunto().setX(camino.getPunto().getX()-distancia);
				 }
 				 else if(parteConoColisionada.equals("centro")){
 					 camino.setOrientacion(Orientation.SE);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);
 				 }
 				 else if(parteConoColisionada.equals("derecha")){
 					 camino.setOrientacion(Orientation.S);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia); 					 
 				 }
				 break;
				}
		case O:{if(parteConoColisionada.equals("izquierda")){
					camino.setOrientacion(Orientation.NO);
					camino.getPunto().setX(camino.getPunto().getX()-distancia);
					camino.getPunto().setY(camino.getPunto().getY()+distancia);
				}
				else if(parteConoColisionada.equals("centro")){
					 camino.setOrientacion(Orientation.S);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia); 	
				}
			    else if(parteConoColisionada.equals("derecha")){
			    	 camino.setOrientacion(Orientation.SO);
					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);			    	 
			    }
				break;
			   }
		default:{if(parteConoColisionada.equals("izquierda")){
			 		camino.setOrientacion(Orientation.N);
			 		camino.getPunto().setY(camino.getPunto().getY()+distancia);
				 }
				 else if(parteConoColisionada.equals("centro")){
					 camino.setOrientacion(Orientation.SO);
					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);
				 }
				 else if(parteConoColisionada.equals("derecha")){
					 camino.setOrientacion(Orientation.O);
					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
					
				 }
				} //NO
		}//fin del switch	
		
		String mensaje= nombre+","+camino.getPunto().getX()+","+camino.getPunto().getY()+","+camino.getPunto().getZ()+","+camino.getOrientacion();
		
		return mensaje;

	}

	private String generaCoordenadasPerseguir(InfoCollision info) {	
		double distancia= 0.5;
		String parteConoColisionada= info.getParteConoColisionada();
		switch(camino.getOrientacion()){
		case N: {if(parteConoColisionada.equals("izquierda")){
					camino.setOrientacion(Orientation.NO);
					camino.getPunto().setX(camino.getPunto().getX()-distancia);
					camino.getPunto().setY(camino.getPunto().getY()+distancia);
				 }
			   	 else if(parteConoColisionada.equals("centro")){
			   		 camino.setOrientacion(Orientation.N);
			   		 camino.getPunto().setY(camino.getPunto().getY()+distancia);
			   	 }
				 else if(parteConoColisionada.equals("derecha")){ 
					 camino.setOrientacion(Orientation.NE);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);
				 }
				 break;
				}
		case NE:{if(parteConoColisionada.equals("izquierda")){ 
					 camino.setOrientacion(Orientation.N);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);
					
	        	 }
				 else if(parteConoColisionada.equals("centro")){
					 camino.setOrientacion(Orientation.NE);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);
				 }
		         else if(parteConoColisionada.equals("derecha")){
		        	 camino.setOrientacion(Orientation.E);
		        	 camino.getPunto().setX(camino.getPunto().getX()+distancia);
		         }
				 break;
		        }
		case E:{if(parteConoColisionada.equals("izquierda")){
					 camino.setOrientacion(Orientation.NE);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);
				}
		 		else if(parteConoColisionada.equals("centro")){
		 			 camino.setOrientacion(Orientation.E);
		 			 camino.getPunto().setX(camino.getPunto().getX()+distancia);
		 		}
		 		else if(parteConoColisionada.equals("derecha")){
		 		   	 camino.setOrientacion(Orientation.SE);
		 			 camino.getPunto().setX(camino.getPunto().getX()+distancia);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);
		 			
		 		}
				break;
        	   }
		case SE:{if(parteConoColisionada.equals("izquierda")){
					 camino.setOrientacion(Orientation.E);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
				 }
 				 else if(parteConoColisionada.equals("centro")){
 					 camino.setOrientacion(Orientation.SE);
 					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);
 				 }
 				 else if(parteConoColisionada.equals("derecha")){
 					 camino.setOrientacion(Orientation.S);
 					 camino.getPunto().setY(camino.getPunto().getY()-distancia);
 				 }
				 break;
				}
		case S:{if(parteConoColisionada.equals("izquierda")){
					 camino.setOrientacion(Orientation.SE);
					 camino.getPunto().setX(camino.getPunto().getX()+distancia);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);
				}
		 		else if(parteConoColisionada.equals("centro")){
		 			 camino.setOrientacion(Orientation.S);
		 			 camino.getPunto().setY(camino.getPunto().getY()-distancia);
		 		}
			    else if(parteConoColisionada.equals("derecha")){
			    	 camino.setOrientacion(Orientation.SO);
			    	 camino.getPunto().setX(camino.getPunto().getX()-distancia);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);
			    }
				break;
			   }
		case SO:{if(parteConoColisionada.equals("izquierda")){
					 camino.setOrientacion(Orientation.S);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);
				 }
 				 else if(parteConoColisionada.equals("centro")){
 					 camino.setOrientacion(Orientation.SO);
 					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);
 				 }
 				 else if(parteConoColisionada.equals("derecha")){
 					 camino.setOrientacion(Orientation.O);
 					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
 				 }
				 break;
				}
		case O:{if(parteConoColisionada.equals("izquierda")){
					 camino.setOrientacion(Orientation.SO);
					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
					 camino.getPunto().setY(camino.getPunto().getY()-distancia);
				}
				else if(parteConoColisionada.equals("centro")){
					 camino.setOrientacion(Orientation.O);
					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
				}
			    else if(parteConoColisionada.equals("derecha")){
			    	 camino.setOrientacion(Orientation.NO);
			    	 camino.getPunto().setX(camino.getPunto().getX()-distancia);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);
			    }
				break;
			   }
		default:{if(parteConoColisionada.equals("izquierda")){
					 camino.setOrientacion(Orientation.O);
					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
				 }
				 else if(parteConoColisionada.equals("centro")){
					 camino.setOrientacion(Orientation.NO);
					 camino.getPunto().setX(camino.getPunto().getX()-distancia);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);
				 }
				 else if(parteConoColisionada.equals("derecha")){
					 camino.setOrientacion(Orientation.N);
					 camino.getPunto().setY(camino.getPunto().getY()+distancia);
				 }
				} //NO
		}//fin del switch	
		
		String mensaje= nombre+","+camino.getPunto().getX()+","+camino.getPunto().getY()+","+camino.getPunto().getZ()+","+camino.getOrientacion();
		
		return mensaje;
	}

	
	private void mensajeInfoAgente(String destinatario) {
		generaCoordenadas();//TODO quitar esta linea cuando probemos con la parte C
		InfoAgent info = new InfoAgent(nombre,camino.getPunto().getX(),camino.getPunto().getY(),camino.getPunto().getZ(),camino.getOrientacion());
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		//agrega contenido
		try {
			msg.setContentObject(info);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//agrega la direccion del destinatario
		msg.addReceiver( new AID(destinatario, AID.ISLOCALNAME) );
		//envia mensaje
		myAgent.send(msg);
		
	}

	protected void nuevoMensaje(String destinatario, String contenido){
		//Realizamos las acciones necesarias cada cierto tiempo
		//Construye mensaje de tipo INFORM
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		//agrega contenido
		//String contenido = generaCoordenadas();
		msg.setContent(contenido);
		//agrega la direccion del destinatario
		msg.addReceiver( new AID(destinatario, AID.ISLOCALNAME) );
		//envia mensaje
		myAgent.send(msg);
	}
	
}
