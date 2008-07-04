package es.ucm.fdi.mcdm;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;



//import org.math.plot.*;

public class DecisionMaker {
	
	public DecisionMaker(){}

	/*
	 * Toma de decisiones del gato.
	 */
	public static String tomaDeDecisionesGato(String tipoAgente, OrientacionAgenteVisto orientacion, double claridad_percepcion, double distancia, String decisionAnt, double w1, double w2, double w3, double w4, double w5){
		/*System.out.println("TOMA DE DECISIONES DEL GATO");
		System.out.println("Tres criterios:");
		System.out.println("Criterio 1: Tipo de agente (beneficio)");
		System.out.println("Criterio 2: Orientación (beneficio)");
		System.out.println("Criterio 3: Claridad de percepción (beneficio)");
		System.out.println("Criterio 4: Distancia (coste)");*/
		int numCriterios= 5;
		String decision= "";
		//Normalizamos las entradas entre 0 y 1:
		double tipoAgenteN= normalizaTipoAgenteG(tipoAgente);
		double orientacionN= normalizaOrientacion(orientacion); 
		double claridad_percepcionN= claridad_percepcion;
		double distanciaN= normalizaDistancia(distancia);
		double decisionAntN= normalizaDecisionAntG(decisionAnt);
		
		int[] modeloA1= {0, 0, 0, 1, 0};//Alternativa 1: no hacer nada
		//System.out.println("Modelo para la alternativa 1, NO HACER NADA: "+modeloA1[0]+" "+modeloA1[1]+" "+modeloA1[2]+" "+modeloA1[3]);
		int[] modeloA2= {1, 1, 1, 0, 1};//Alternativa 2: perseguir
		//System.out.println("Modelo para la alternativa 2, PERSEGUIR:     "+modeloA2[0]+" "+modeloA2[1]+" "+modeloA2[2]+" "+modeloA2[3]);
		int[] modeloA3= {0, 0, 1, 0, 1/2};//Alternativa 2: esquivar (esquivo si no es ratón, me da igual la orientación, le veo bien y está muy cerca)
		//System.out.println("Modelo para la alternativa 3, ESQUIVAR:      "+modeloA3[0]+" "+modeloA3[1]+" "+modeloA3[2]+" "+modeloA3[3]);
		
		
		//Convertimos cada una de las tres entradas en 3 números fuzzy ordenados de menor a mayor:
		//System.out.print("Números fuzzy triangulares para el criterio 1: ");
		double[] _tipoAgente= generaNumAleatorio(tipoAgenteN);		
		//System.out.print("Números fuzzy triangulares para el criterio 2: ");
		double[] _orientacion= generaNumAleatorio(orientacionN);
		//System.out.print("Números fuzzy triangulares para el criterio 3: ");
		double[] _claridad_percepcion= generaNumAleatorio(claridad_percepcionN);
		//System.out.print("Números fuzzy triangulares para el criterio 4: ");
		double[] _distancia= generaNumAleatorio(distanciaN);
		//System.out.print("Números fuzzy triangulares para el criterio 5: ");
		double[] _decisionAnt= generaNumAleatorio(decisionAntN);
		
		//Rellenamos la tabla:
		//Alternativa 1:
		double[] a1_tipoAgente= new double[3];
		double[] a1_orientacion= new double[3];
		double[] a1_claridad_percepcion= new double[3];
		double[] a1_distancia= new double[3];
		double[] a1_decisionAnt= new double[3];
		//Alternativa 2:
		double[] a2_tipoAgente= new double[3];
		double[] a2_orientacion= new double[3];
		double[] a2_claridad_percepcion= new double[3];
		double[] a2_distancia= new double[3];
		double[] a2_decisionAnt= new double[3];
		//Alternativa 3:
		double[] a3_tipoAgente= new double[3];
		double[] a3_orientacion= new double[3];
		double[] a3_claridad_percepcion= new double[3];
		double[] a3_distancia= new double[3];
		double[] a3_decisionAnt= new double[3];
		
		for(int k= 0; k< 5; k++){//Recorremos los criterios:
			if(k==0){//Son criterios de beneficio: Tipo de agente
				if(modeloA1[k] == 1){//entonces (modeloA2[k] == 0)
					for(int i= 0; i< 3; i++){
						a1_tipoAgente[i]= _tipoAgente[i];
						a2_tipoAgente[i]= 1- _tipoAgente[i];
						a3_tipoAgente[i]= _tipoAgente[i];
					}
				}
				else{//modeloA1[k] == 0 entonces (modeloA2[k] == 1)
					for(int i= 0; i< 3; i++){
						a1_tipoAgente[i]= 1- _tipoAgente[i];
						a2_tipoAgente[i]= _tipoAgente[i];
						a3_tipoAgente[i]= 1 - _tipoAgente[i];
					}
				}
			}
			else if(k==1){//Son criterios de beneficio: Orientación
				if(modeloA1[k] == 1){//entonces (modeloA2[k] == 0)
					for(int i= 0; i< 3; i++){
						a1_orientacion[i]= _orientacion[i];
						a2_orientacion[i]= 1 - _orientacion[i];
						a3_orientacion[i]= _orientacion[i];
					}
				}
				else{//modeloA1[k] == 0 entonces (modeloA2[k] == 1)
					for(int i= 0; i< 3; i++){
						a1_orientacion[i]= 1 - _orientacion[i];	
						a2_orientacion[i]= _orientacion[i];
						a3_orientacion[i]= 1 - _orientacion[i];
					}
				}			
			}	
			else if(k==2){//Son criterios de beneficio: Claridad de percepción
					if(modeloA1[k] == 1){//entonces (modeloA2[k] == 0)
						for(int i= 0; i< 3; i++){
							a1_claridad_percepcion[i]= _claridad_percepcion[i];
							a2_claridad_percepcion[i]= 1 - _claridad_percepcion[i];
							a3_claridad_percepcion[i]= 1 - _claridad_percepcion[i];
						}
					
					}
					else{//modeloA1[k] == 0	entonces (modeloA2[k] == 1)
						for(int i= 0; i< 3; i++){
							a1_claridad_percepcion[i]= 1 - _claridad_percepcion[i];
							a2_claridad_percepcion[i]= _claridad_percepcion[i];
							a3_claridad_percepcion[i]= _claridad_percepcion[i];
						}
					}			
					
			}
			else if(k==3){// Son criterios de coste: Distancia
				if(modeloA1[k] == 1){//entonces (modeloA2[k] == 0)
					for(int i= 0; i< 3; i++){ 
						a1_distancia[i]= 1- _distancia[i];
						a2_distancia[i]= _distancia[i];					
						a3_distancia[i]= _distancia[i];
					}
				}
				else{//modeloA1[k] == 0	entonces (modeloA2[k] == 1)
					for(int i= 0; i< 3; i++){
						a1_distancia[i]= _distancia[i];
						a2_distancia[i]= 1- _distancia[i];
						a3_distancia[i]= 1- _distancia[i];
					}
				}	
			}
			else{//k=4. Criterio de beneficio: Decision anterior
				if(modeloA1[k] == 1){//entonces (modeloA2[k] == 0)
					for(int i= 0; i< 3; i++){
						a1_decisionAnt[i]= _decisionAnt[i];
						a2_decisionAnt[i]= 1 - _decisionAnt[i];
						a3_decisionAnt[i]= 0.5 - _decisionAnt[i];
					}
				
				}
				else{//modeloA1[k] == 0	entonces (modeloA2[k] == 1)
					for(int i= 0; i< 3; i++){
						a1_decisionAnt[i]= 1 - _decisionAnt[i];
						a2_decisionAnt[i]= _decisionAnt[i];
						a3_decisionAnt[i]= 0.5 - _decisionAnt[i];
					}
				}		
			}
		}
		/*System.out.println("Mostramos la tabla rellena (ANTES de aplicar los pesos): ");
		System.out.println("Criterio 1, alternativa 1: "+ a1_tipoAgente[0]+", "+a1_tipoAgente[1]+", "+a1_tipoAgente[2]); 
		System.out.println("Criterio 1, alternativa 2: "+ a2_tipoAgente[0]+", "+a2_tipoAgente[1]+", "+a2_tipoAgente[2]);
		System.out.println("Criterio 1, alternativa 3: "+ a3_tipoAgente[0]+", "+a3_tipoAgente[1]+", "+a3_tipoAgente[2]);
		
		System.out.println("Criterio 2, alternativa 1: "+ a1_orientacion[0]+", "+a1_orientacion[1]+", "+a1_orientacion[2]); 
		System.out.println("Criterio 2, alternativa 2: "+ a2_orientacion[0]+", "+a2_orientacion[1]+", "+a2_orientacion[2]);
		System.out.println("Criterio 2, alternativa 3: "+ a3_orientacion[0]+", "+a3_orientacion[1]+", "+a3_orientacion[2]);
		
		System.out.println("Criterio 3, alternativa 1: "+ a1_claridad_percepcion[0]+", "+a1_claridad_percepcion[1]+", "+a1_claridad_percepcion[2]); 
		System.out.println("Criterio 3, alternativa 2: "+ a2_claridad_percepcion[0]+", "+a2_claridad_percepcion[1]+", "+a2_claridad_percepcion[2]);
		System.out.println("Criterio 3, alternativa 3: "+ a3_claridad_percepcion[0]+", "+a3_claridad_percepcion[1]+", "+a3_claridad_percepcion[2]);
		
		System.out.println("Criterio 4, alternativa 1: "+ a1_distancia[0]+", "+a1_distancia[1]+", "+a1_distancia[2]);
		System.out.println("Criterio 4, alternativa 2: "+ a2_distancia[0]+", "+a2_distancia[1]+", "+a2_distancia[2]);
		System.out.println("Criterio 4, alternativa 3: "+ a3_distancia[0]+", "+a3_distancia[1]+", "+a3_distancia[2]);*/
		//Aplicamos los diversos pesos:
		for(int i= 0; i< 3; i++){
			a1_tipoAgente[i]= w1 * a1_tipoAgente[i];
			a2_tipoAgente[i]= w1 * a2_tipoAgente[i];
			a3_tipoAgente[i]= w1 * a3_tipoAgente[i];
			a1_orientacion[i]= w2 * a1_orientacion[i];
			a2_orientacion[i]= w2 * a2_orientacion[i];
			a3_orientacion[i]= w2 * a3_orientacion[i];
			a1_claridad_percepcion[i]= w3 * a1_claridad_percepcion[i];
			a2_claridad_percepcion[i]= w3 * a2_claridad_percepcion[i];
			a3_claridad_percepcion[i]= w3 * a3_claridad_percepcion[i];
			a1_distancia[i]= w4 * a1_distancia[i];
			a2_distancia[i]= w4 * a2_distancia[i];
			a3_distancia[i]= w4 * a3_distancia[i];
			a1_decisionAnt[i]= w5 * a1_decisionAnt[i];
			a2_decisionAnt[i]= w5 * a2_decisionAnt[i];
			a3_decisionAnt[i]= w5 * a3_decisionAnt[i];
		}
		
		/*System.out.println("Mostramos la tabla rellena (DESPUES de aplicar los pesos): ");
		System.out.println("Criterio 1, alternativa 1: "+ a1_tipoAgente[0]+", "+a1_tipoAgente[1]+", "+a1_tipoAgente[2]); 
		System.out.println("Criterio 1, alternativa 2: "+ a2_tipoAgente[0]+", "+a2_tipoAgente[1]+", "+a2_tipoAgente[2]);
		System.out.println("Criterio 1, alternativa 3: "+ a3_tipoAgente[0]+", "+a3_tipoAgente[1]+", "+a3_tipoAgente[2]);
		
		System.out.println("Criterio 2, alternativa 1: "+ a1_orientacion[0]+", "+a1_orientacion[1]+", "+a1_orientacion[2]); 
		System.out.println("Criterio 2, alternativa 2: "+ a2_orientacion[0]+", "+a2_orientacion[1]+", "+a2_orientacion[2]);
		System.out.println("Criterio 2, alternativa 3: "+ a3_orientacion[0]+", "+a3_orientacion[1]+", "+a3_orientacion[2]);
		
		System.out.println("Criterio 3, alternativa 1: "+ a1_claridad_percepcion[0]+", "+a1_claridad_percepcion[1]+", "+a1_claridad_percepcion[2]); 
		System.out.println("Criterio 3, alternativa 2: "+ a2_claridad_percepcion[0]+", "+a2_claridad_percepcion[1]+", "+a2_claridad_percepcion[2]);
		System.out.println("Criterio 3, alternativa 3: "+ a3_claridad_percepcion[0]+", "+a3_claridad_percepcion[1]+", "+a3_claridad_percepcion[2]);
		
		System.out.println("Criterio 4, alternativa 1: "+ a1_distancia[0]+", "+a1_distancia[1]+", "+a1_distancia[2]);
		System.out.println("Criterio 4, alternativa 2: "+ a2_distancia[0]+", "+a2_distancia[1]+", "+a2_distancia[2]);
		System.out.println("Criterio 4, alternativa 3: "+ a3_distancia[0]+", "+a3_distancia[1]+", "+a3_distancia[2]);*/
		
		
		//Calculamos d1+, d1-, d2+ y d2-:
		double[] pMasBeneficio= {1, 1, 1};
		double[] pMenosBeneficio= {0, 0, 0};
		double[] pMasCoste= {0, 0, 0};
		double[] pMenosCoste= {1, 1, 1};
		
		double d1Mas= calculaDistancia(a1_tipoAgente, pMasBeneficio) + calculaDistancia(a1_orientacion, pMasBeneficio) + calculaDistancia(a1_claridad_percepcion, pMasBeneficio) + calculaDistancia(a1_distancia, pMasCoste) + calculaDistancia(a1_decisionAnt, pMasBeneficio);
		double d1Menos= calculaDistancia(a1_tipoAgente, pMenosBeneficio) + calculaDistancia(a1_orientacion, pMenosBeneficio) + calculaDistancia(a1_claridad_percepcion, pMenosBeneficio) + calculaDistancia(a1_distancia, pMenosCoste) + calculaDistancia(a1_decisionAnt, pMenosBeneficio);
		double d2Mas= calculaDistancia(a2_tipoAgente, pMasBeneficio) + calculaDistancia(a2_orientacion, pMasBeneficio) + calculaDistancia(a2_claridad_percepcion, pMasBeneficio) + calculaDistancia(a2_distancia, pMasCoste) + calculaDistancia(a2_decisionAnt, pMasBeneficio);
		double d2Menos= calculaDistancia(a2_tipoAgente, pMenosBeneficio) + calculaDistancia(a2_orientacion, pMenosBeneficio) + calculaDistancia(a2_claridad_percepcion, pMenosBeneficio) + calculaDistancia(a2_distancia, pMenosCoste) + calculaDistancia(a2_decisionAnt, pMenosBeneficio);
		double d3Mas= calculaDistancia(a3_tipoAgente, pMasBeneficio) + calculaDistancia(a3_orientacion, pMasBeneficio) + calculaDistancia(a3_claridad_percepcion, pMasBeneficio) + calculaDistancia(a3_distancia, pMasCoste) + calculaDistancia(a3_decisionAnt, pMasBeneficio);
		double d3Menos= calculaDistancia(a3_tipoAgente, pMenosBeneficio) + calculaDistancia(a3_orientacion, pMenosBeneficio) + calculaDistancia(a3_claridad_percepcion, pMenosBeneficio) + calculaDistancia(a3_distancia, pMenosCoste) + calculaDistancia(a3_decisionAnt, pMenosBeneficio);
		
		
		//Calculamos p1, p2 y p3:
		double p1= (d1Menos + numCriterios - d1Mas) / (2 * numCriterios);
		double p2= (d2Menos + numCriterios - d2Mas) / (2 * numCriterios);
		double p3= (d3Menos + numCriterios - d3Mas) / (2 * numCriterios);
		/*System.out.println("Se tomará la decisión que mas se aproxime a 1: ");
		System.out.println("No hacer nada= "+p1);
		System.out.println("Perseguir=     "+p2);
		System.out.println("Esquivar=      "+p3);*/
		
		//Tomamos la decisión:
		double alternativa1= Math.abs(p1 - 1);
		double alternativa2= Math.abs(p2 - 1);
		double alternativa3= Math.abs(p3 - 1);
		
		if((alternativa1 < alternativa2) && (alternativa1 < alternativa3))
			decision= "no hacer nada";//a1
		else if ((alternativa2 < alternativa1) && (alternativa2 < alternativa3))
			decision= "perseguir";//a2		
		else if ((alternativa3 < alternativa1) && (alternativa3 < alternativa2))
			decision= "esquivar";//a3
		return decision;
	}
	
	/*
	 * Toma de decisiones del ratón.
	 */
	public static String tomaDeDecisionesRaton(String tipoAgente, OrientacionAgenteVisto orientacion, double claridad_percepcion, double distancia, String decisionAnt, double w1, double w2, double w3, double w4, double w5){//TODO
		/*System.out.println("TOMA DE DECISIONES DEL RATON");
		System.out.println("Tres criterios:");
		System.out.println("Criterio 1: Tipo de agente (beneficio)");
		System.out.println("Criterio 2: Orientación (coste)");
		System.out.println("Criterio 3: Claridad de percepción (beneficio)");
		System.out.println("Criterio 4: Distancia (coste)");*/
		int numCriterios= 5;
		String decision= "";
		//Normalizamos las entradas entre 0 y 1:
		double tipoAgenteN= normalizaTipoAgenteR(tipoAgente);
		double orientacionN= normalizaOrientacion(orientacion); 
		double claridad_percepcionN= claridad_percepcion;
		double distanciaN= normalizaDistancia(distancia);
		double decisionAntN= normalizaDecisionAntR(decisionAnt);
		
		int[] modeloA1= {0, 1, 0, 1, 0};//Alternativa 1: no hacer nada
		//System.out.println("Modelo para la alternativa 1, NO HACER NADA: "+modeloA1[0]+" "+modeloA1[1]+" "+modeloA1[2]+" "+modeloA1[3]);
		int[] modeloA2= {1, 0, 1, 0, 1};//Alternativa 2: huir
		//System.out.println("Modelo para la alternativa 2, HUIR:          "+modeloA2[0]+" "+modeloA2[1]+" "+modeloA2[2]+" "+modeloA2[3]);
		int[] modeloA3= {0, 0, 1, 0, 1/2};//Alternativa 2: esquivar (esquivo si no es gato, me da igual la orientación, le veo bien y está muy cerca)
		//System.out.println("Modelo para la alternativa 3, ESQUIVAR:      "+modeloA3[0]+" "+modeloA3[1]+" "+modeloA3[2]+" "+modeloA3[3]);
		
		
		//Convertimos cada una de las tres entradas en 3 números fuzzy ordenados de menor a mayor:
		//System.out.print("Números fuzzy triangulares para el criterio 1: ");
		double[] _tipoAgente= generaNumAleatorio(tipoAgenteN);		
		//System.out.print("Números fuzzy triangulares para el criterio 2: ");
		double[] _orientacion= generaNumAleatorio(orientacionN);
		//System.out.print("Números fuzzy triangulares para el criterio 3: ");
		double[] _claridad_percepcion= generaNumAleatorio(claridad_percepcionN);
		//System.out.print("Números fuzzy triangulares para el criterio 4: ");
		double[] _distancia= generaNumAleatorio(distanciaN);
		//System.out.print("Números fuzzy triangulares para el criterio 5: ");
		double[] _decisionAnt= generaNumAleatorio(decisionAntN);
		
		//Rellenamos la tabla:
		//Alternativa 1:
		double[] a1_tipoAgente= new double[3];
		double[] a1_orientacion= new double[3];
		double[] a1_claridad_percepcion= new double[3];
		double[] a1_distancia= new double[3];
		double[] a1_decisionAnt= new double[3];
		//Alternativa 2:
		double[] a2_tipoAgente= new double[3];
		double[] a2_orientacion= new double[3];
		double[] a2_claridad_percepcion= new double[3];
		double[] a2_distancia= new double[3];
		double[] a2_decisionAnt= new double[3];
		//Alternativa 3:
		double[] a3_tipoAgente= new double[3];
		double[] a3_orientacion= new double[3];
		double[] a3_claridad_percepcion= new double[3];
		double[] a3_distancia= new double[3];
		double[] a3_decisionAnt= new double[3];
		
		for(int k= 0; k< 5; k++){//Recorremos los criterios:
			if(k==0){//Son criterios de beneficio: Tipo de agente
				if(modeloA1[k] == 1){//entonces (modeloA2[k] == 0)
					for(int i= 0; i< 3; i++){
						a1_tipoAgente[i]= _tipoAgente[i];
						a2_tipoAgente[i]= 1- _tipoAgente[i];
						a3_tipoAgente[i]= _tipoAgente[i];
					}
				}
				else{//modeloA1[k] == 0 entonces (modeloA2[k] == 1)
					for(int i= 0; i< 3; i++){
						a1_tipoAgente[i]= 1 - _tipoAgente[i];
						a2_tipoAgente[i]= _tipoAgente[i];
						a3_tipoAgente[i]= 1 - _tipoAgente[i];
					}
				}
			}
			else if(k==1){//Son criterios de coste: Orientación
				if(modeloA1[k] == 1){//entonces (modeloA2[k] == 0)
					for(int i= 0; i< 3; i++){
						a1_orientacion[i]= 1- _orientacion[i];
						a2_orientacion[i]= _orientacion[i];
						a3_orientacion[i]= _orientacion[i];
					}
				}
				else{//modeloA1[k] == 0 entonces (modeloA2[k] == 1)
					for(int i= 0; i< 3; i++){
						a1_orientacion[i]= _orientacion[i];	
						a2_orientacion[i]= 1 - _orientacion[i];
						a3_orientacion[i]= 1 - _orientacion[i];
					}
				}			
			}	
			else if(k==2){//Son criterios de beneficio: Claridad de percepción
					if(modeloA1[k] == 1){//entonces (modeloA2[k] == 0)
						for(int i= 0; i< 3; i++){
							a1_claridad_percepcion[i]= _claridad_percepcion[i];
							a2_claridad_percepcion[i]= 1 - _claridad_percepcion[i];
							a3_claridad_percepcion[i]= 1 - _claridad_percepcion[i];
						}
					
					}
					else{//modeloA1[k] == 0	entonces (modeloA2[k] == 1)
						for(int i= 0; i< 3; i++){
							a1_claridad_percepcion[i]= 1 - _claridad_percepcion[i];
							a2_claridad_percepcion[i]= _claridad_percepcion[i];
							a3_claridad_percepcion[i]= _claridad_percepcion[i];
						}
					}			
					
			}
			else if(k==3){//k==3. Son criterios de coste: Distancia
				if(modeloA1[k] == 1){//entonces (modeloA2[k] == 0)
					for(int i= 0; i< 3; i++){ 
						a1_distancia[i]= 1- _distancia[i];
						a2_distancia[i]= _distancia[i];					
						a3_distancia[i]= _distancia[i];
					}
				}
				else{//modeloA1[k] == 0	entonces (modeloA2[k] == 1)
					for(int i= 0; i< 3; i++){
						a1_distancia[i]= _distancia[i];
						a2_distancia[i]= 1- _distancia[i];
						a3_distancia[i]= 1- _distancia[i];
					}
				}	
			}
			else{//k=4. Criterio de beneficio: Decision anterior
				if(modeloA1[k] == 1){//entonces (modeloA2[k] == 0)
					for(int i= 0; i< 3; i++){
						a1_decisionAnt[i]= _decisionAnt[i];
						a2_decisionAnt[i]= 1 - _decisionAnt[i];
						a3_decisionAnt[i]= 0.5 - _decisionAnt[i];
					}
				
				}
				else{//modeloA1[k] == 0	entonces (modeloA2[k] == 1)
					for(int i= 0; i< 3; i++){
						a1_decisionAnt[i]= 1 - _decisionAnt[i];
						a2_decisionAnt[i]= _decisionAnt[i];
						a3_decisionAnt[i]= 0.5 - _decisionAnt[i];
					}
				}		
			}
		}
		/*System.out.println("Mostramos la tabla rellena (ANTES de aplicar los pesos): ");
		System.out.println("Criterio 1, alternativa 1: "+ a1_tipoAgente[0]+", "+a1_tipoAgente[1]+", "+a1_tipoAgente[2]); 
		System.out.println("Criterio 1, alternativa 2: "+ a2_tipoAgente[0]+", "+a2_tipoAgente[1]+", "+a2_tipoAgente[2]);
		System.out.println("Criterio 1, alternativa 3: "+ a3_tipoAgente[0]+", "+a3_tipoAgente[1]+", "+a3_tipoAgente[2]);
		
		System.out.println("Criterio 2, alternativa 1: "+ a1_orientacion[0]+", "+a1_orientacion[1]+", "+a1_orientacion[2]); 
		System.out.println("Criterio 2, alternativa 2: "+ a2_orientacion[0]+", "+a2_orientacion[1]+", "+a2_orientacion[2]);
		System.out.println("Criterio 2, alternativa 3: "+ a3_orientacion[0]+", "+a3_orientacion[1]+", "+a3_orientacion[2]);
		
		System.out.println("Criterio 3, alternativa 1: "+ a1_claridad_percepcion[0]+", "+a1_claridad_percepcion[1]+", "+a1_claridad_percepcion[2]); 
		System.out.println("Criterio 3, alternativa 2: "+ a2_claridad_percepcion[0]+", "+a2_claridad_percepcion[1]+", "+a2_claridad_percepcion[2]);
		System.out.println("Criterio 3, alternativa 3: "+ a3_claridad_percepcion[0]+", "+a3_claridad_percepcion[1]+", "+a3_claridad_percepcion[2]);
		
		System.out.println("Criterio 4, alternativa 1: "+ a1_distancia[0]+", "+a1_distancia[1]+", "+a1_distancia[2]);
		System.out.println("Criterio 4, alternativa 2: "+ a2_distancia[0]+", "+a2_distancia[1]+", "+a2_distancia[2]);
		System.out.println("Criterio 4, alternativa 3: "+ a3_distancia[0]+", "+a3_distancia[1]+", "+a3_distancia[2]);*/
		//Aplicamos los diversos pesos:
		for(int i= 0; i< 3; i++){
			a1_tipoAgente[i]= w1 * a1_tipoAgente[i];
			a2_tipoAgente[i]= w1 * a2_tipoAgente[i];
			a3_tipoAgente[i]= w1 * a3_tipoAgente[i];
			a1_orientacion[i]= w2 * a1_orientacion[i];
			a2_orientacion[i]= w2 * a2_orientacion[i];
			a3_orientacion[i]= w2 * a3_orientacion[i];
			a1_claridad_percepcion[i]= w3 * a1_claridad_percepcion[i];
			a2_claridad_percepcion[i]= w3 * a2_claridad_percepcion[i];
			a3_claridad_percepcion[i]= w3 * a3_claridad_percepcion[i];
			a1_distancia[i]= w4 * a1_distancia[i];
			a2_distancia[i]= w4 * a2_distancia[i];
			a3_distancia[i]= w4 * a3_distancia[i];
			a1_decisionAnt[i]= w5 * a1_decisionAnt[i];
			a2_decisionAnt[i]= w5 * a2_decisionAnt[i];
			a3_decisionAnt[i]= w5 * a3_decisionAnt[i];
		}
		
		/*System.out.println("Mostramos la tabla rellena (DESPUES de aplicar los pesos): ");
		System.out.println("Criterio 1, alternativa 1: "+ a1_tipoAgente[0]+", "+a1_tipoAgente[1]+", "+a1_tipoAgente[2]); 
		System.out.println("Criterio 1, alternativa 2: "+ a2_tipoAgente[0]+", "+a2_tipoAgente[1]+", "+a2_tipoAgente[2]);
		System.out.println("Criterio 1, alternativa 3: "+ a3_tipoAgente[0]+", "+a3_tipoAgente[1]+", "+a3_tipoAgente[2]);
		
		System.out.println("Criterio 2, alternativa 1: "+ a1_orientacion[0]+", "+a1_orientacion[1]+", "+a1_orientacion[2]); 
		System.out.println("Criterio 2, alternativa 2: "+ a2_orientacion[0]+", "+a2_orientacion[1]+", "+a2_orientacion[2]);
		System.out.println("Criterio 2, alternativa 3: "+ a3_orientacion[0]+", "+a3_orientacion[1]+", "+a3_orientacion[2]);
		
		System.out.println("Criterio 3, alternativa 1: "+ a1_claridad_percepcion[0]+", "+a1_claridad_percepcion[1]+", "+a1_claridad_percepcion[2]); 
		System.out.println("Criterio 3, alternativa 2: "+ a2_claridad_percepcion[0]+", "+a2_claridad_percepcion[1]+", "+a2_claridad_percepcion[2]);
		System.out.println("Criterio 3, alternativa 3: "+ a3_claridad_percepcion[0]+", "+a3_claridad_percepcion[1]+", "+a3_claridad_percepcion[2]);
		
		System.out.println("Criterio 4, alternativa 1: "+ a1_distancia[0]+", "+a1_distancia[1]+", "+a1_distancia[2]);
		System.out.println("Criterio 4, alternativa 2: "+ a2_distancia[0]+", "+a2_distancia[1]+", "+a2_distancia[2]);
		System.out.println("Criterio 4, alternativa 3: "+ a3_distancia[0]+", "+a3_distancia[1]+", "+a3_distancia[2]);*/
		
		
		//Calculamos d1+, d1-, d2+ y d2-:
		double[] pMasBeneficio= {1, 1, 1, 1};
		double[] pMenosBeneficio= {0, 0, 0, 0};
		double[] pMasCoste= {0, 0, 0, 0};
		double[] pMenosCoste= {1, 1, 1, 1};
		
		double d1Mas= calculaDistancia(a1_tipoAgente, pMasBeneficio) + calculaDistancia(a1_orientacion, pMasCoste) + calculaDistancia(a1_claridad_percepcion, pMasBeneficio) + calculaDistancia(a1_distancia, pMasCoste) + calculaDistancia(a1_decisionAnt, pMasBeneficio);
		double d1Menos= calculaDistancia(a1_tipoAgente, pMenosBeneficio) + calculaDistancia(a1_orientacion, pMenosCoste) + calculaDistancia(a1_claridad_percepcion, pMenosBeneficio) + calculaDistancia(a1_distancia, pMenosCoste) + calculaDistancia(a1_decisionAnt, pMenosBeneficio);
		double d2Mas= calculaDistancia(a2_tipoAgente, pMasBeneficio) + calculaDistancia(a2_orientacion, pMasCoste) + calculaDistancia(a2_claridad_percepcion, pMasBeneficio) + calculaDistancia(a2_distancia, pMasCoste) + calculaDistancia(a2_decisionAnt, pMasBeneficio);
		double d2Menos= calculaDistancia(a2_tipoAgente, pMenosBeneficio) + calculaDistancia(a2_orientacion, pMenosCoste) + calculaDistancia(a2_claridad_percepcion, pMenosBeneficio) + calculaDistancia(a2_distancia, pMenosCoste) + calculaDistancia(a2_decisionAnt, pMenosBeneficio);
		double d3Mas= calculaDistancia(a3_tipoAgente, pMasBeneficio) + calculaDistancia(a3_orientacion, pMasCoste) + calculaDistancia(a3_claridad_percepcion, pMasBeneficio) + calculaDistancia(a3_distancia, pMasCoste) + calculaDistancia(a3_decisionAnt, pMasBeneficio);
		double d3Menos= calculaDistancia(a3_tipoAgente, pMenosBeneficio) + calculaDistancia(a3_orientacion, pMenosCoste) + calculaDistancia(a3_claridad_percepcion, pMenosBeneficio) + calculaDistancia(a3_distancia, pMenosCoste) + calculaDistancia(a3_decisionAnt, pMenosBeneficio);
		
		
		//Calculamos p1, p2 y p3:
		double p1= (d1Menos + numCriterios - d1Mas) / (2 * numCriterios);
		double p2= (d2Menos + numCriterios - d2Mas) / (2 * numCriterios);
		double p3= (d3Menos + numCriterios - d3Mas) / (2 * numCriterios);
		/*System.out.println("Se tomará la decisión que mas se aproxime a 1: ");
		System.out.println("No hacer nada= "+p1);
		System.out.println("Huir=          "+p2);
		System.out.println("Esquivar=      "+p3);*/
		
		//Tomamos la decisión:
		double alternativa1= Math.abs(p1 - 1);
		double alternativa2= Math.abs(p2 - 1);
		double alternativa3= Math.abs(p3 - 1);
		
		if((alternativa1 < alternativa2) && (alternativa1 < alternativa3))
			decision= "no hacer nada";//a1
		else if ((alternativa2 < alternativa1) && (alternativa2 < alternativa3))
			decision= "huir";//a2		
		else if ((alternativa3 < alternativa1) && (alternativa3 < alternativa2))
			decision= "esquivar";//a3
		return decision;
	}
	
	private static double normalizaDecisionAntG(String decisionAnt) {
		double resultado= 0.0;
		if(decisionAnt.equals("no hacer nada"))
			resultado= 0.0;
		else if(decisionAnt.equals("esquivar"))
			resultado= 0.5;
		else if(decisionAnt.equals("perseguir"))
			resultado= 1;

		return resultado;
	}
	
	private static double normalizaDecisionAntR(String decisionAnt) {
		double resultado= 0.0;
		if(decisionAnt.equals("no hacer nada"))
			resultado= 0.0;
		else if(decisionAnt.equals("esquivar"))
			resultado= 0.5;
		else if(decisionAnt.equals("huir"))
			resultado= 1;

		return resultado;
	}
	
	private static double normalizaTipoAgenteG(String tipoAgente) {
		double resultado;
		if(tipoAgente.equals("raton"))
			resultado= 1;
		else if(tipoAgente.equals("gato"))
			resultado= 0;
		else resultado= 0;//mesa, etc (agentes que no son ni gato ni ratón)
		
		return resultado;
	}
	
	private static double normalizaTipoAgenteR(String tipoAgente) {
		double resultado;
		if(tipoAgente.equals("gato"))
			resultado= 1;
		else if(tipoAgente.equals("raton"))
			resultado= 0;
		else resultado= 0;//mesa, etc (agentes que no son ni gato ni ratón)
		
		return resultado;
	}

	private static double normalizaOrientacion(OrientacionAgenteVisto orientacion){
		double resultado;
		switch(orientacion){
			case FRENTE: resultado= 0; break;
			case FRENTE_DERECHA: resultado= 0.5; break;
			case FRENTE_IZQUIERDA: resultado= 0.5; break;
			case LADO_DERECHA: resultado= 1; break;
			case LADO_IZQUIERDA: resultado= 1; break;
			case ESPALDAS_DERECHA: resultado= 1.5; break;
			case ESPALDAS_IZQUIERDA: resultado= 1.5; break;
			default:  resultado= 2; break;//ESPALDAS
		}
		resultado= resultado / 2;
		return resultado;
	}
	
	private static double normalizaDistancia(double distancia){
		double resultado= 0.0;
		if(distancia >= 0 || distancia<= 15)
			resultado= distancia / 15;			
		else
			System.out.println("La distancia introducida no están entre 0 y 15");
		return resultado;
	}
	
	/*
	 * Esta función devuelve un array de 3 elementos compuestos por el número pasado
	 * como argumento, junto con dos número aleatorios.
	 * Los 3 número deben estar dentro del rango [0,1]
	 * El array devuelto está ordenado de menor a mayor.
	 */	
	private static double[] generaNumAleatorio(double numero){
		double[] res= new double[3];
		double num1= 2;
		while(!((num1 >= (numero - 0.1))&&(num1 <= (numero + 0.1))&&(num1 != numero))){
			if(numero == 0)
				num1= numero + Math.random();
			else
				num1= numero - Math.random();
		}
		double num2= 2;
		while(!((num2 >= (numero - 0.1))&&(num2 <= (numero + 0.1))&&(num2 != numero)&&(num2 != num1))){
			if(numero == 1)
				num2= numero - Math.random();
			else
				num2= numero + Math.random();
		}
		
		res[0]= numero;
		res[1]= num1;
		res[2]= num2;
		//Algoritmo de ordenación de la burbuja
		for(int i= 1; i< 3; i++){
			for(int j= 0; j< 2; j++){
				if(res[j]>res[j+1]){
					double temp= res[j];
					res[j]= res[j+1];
					res[j+1]= temp;
				}
			}	
		}		
		//System.out.println(res[0]+" ,"+res[1]+" ,"+res[2]);	
		return res;
	}
	
	private static double calculaDistancia(double[] param1, double[] param2){
		//double res= Math.sqrt((1/3) * (Math.pow(param1[0]-param2[0], 2) + Math.pow(param1[1]-param2[1], 2) + Math.pow(param1[2]-param2[2], 2)));
		double res1= Math.pow((param1[0]-param2[0]), 2);
		double res2= Math.pow((param1[1]-param2[1]), 2);
		double res3= Math.pow((param1[2]-param2[2]), 2);
		double res4= res1 + res2 + res3;
		double res5= res4/3;
		double res6= Math.sqrt(res5);
		return res6;
	}
	
	
	/**
	 * @param args
	 */
	/*public static void main(String[] args) {
		//Entradas:
		String tipoAgente= "raton"; //gato, raton, ...
		Orientacion orientacion= Orientacion.de_frente;//de_frente, de_lado, de_espaldas
		double claridad_percepcion= 0.5;//[0, 1]
		double distancia= 10;//[0, 15]
		DecisionMaker decisionMaker= new DecisionMaker();
		//GATO:
		//String decision= decisionMaker.tomaDeDecisionesGato(tipoAgente, orientacion, claridad_percepcion, distancia, 0.4, 0.2, 0.2, 0.2);
		//RATON:
		//String decision= decisionMaker.tomaDeDecisionesRaton(tipoAgente, orientacion, claridad_percepcion, distancia, 0.4, 0.2, 0.2, 0.2);
		//System.out.println("DECISIÓN TOMADA--> "+decision);
		
		double [] x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		double [] y = new double[10];
		String decision;
		for(int i= 0; i< 10; i++){
			decision= decisionMaker.tomaDeDecisionesGato(tipoAgente, orientacion, claridad_percepcion, distancia, 0.4, 0.2, 0.2, 0.2);
			if(decision.equals("no hacer nada"))
				y[i]= 1.0;
			else if (decision.equals("huir") || (decision.equals("perseguir")))
				y[i]= 2.0;
			else if (decision.equals("esquivar"))
				y[i]= 3.0;
		}
		// define your data
		//double[] x = { 1, 2, 3, 4, 5, 6 };
		//double[] y = { 45, 89, 6, 32, 63, 12 };
 
		// create your PlotPanel (you can use it as a JPanel)
		Plot2DPanel plot = new Plot2DPanel();
 
		// define the legend position
		plot.addLegend("SOUTH");
 
		// add a line plot to the PlotPanel
		plot.addLinePlot("Decisiones tomadas. 1 = no hacer nada, 2 = huir/perseguir, 3 = esquivar", x, y);
		
		// put the PlotPanel in a JFrame like a JPanel
		JFrame frame = new JFrame("Multi Criteria Decision Making");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);

	}*/

}
