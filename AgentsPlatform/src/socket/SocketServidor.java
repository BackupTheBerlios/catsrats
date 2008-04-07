package socket;
/*
 * Javier Abellán. 9 Dic 2003
 *
 * SocketServidor.java
 * Ejemplo de un socket servidor en java que se conecta con un cliente C.
 */

import java.net.*;
import java.io.*;
import java.text.*;
import java.util.Locale;

/**
 * Clase principal que instancia un socket servidor, acepta una conexión
 * de un cliente y le envía un entero y una cadena de caracteres.
 */
public class SocketServidor
{   	
	private ServerSocket socket;
	private Socket cliente;
	private DataInputStream bufferEntrada;
	private DataOutputStream bufferSalida;
	private DatoSocket aux;

    
     /**
      * Constructor por defecto. Hace todo lo que hace el ejemplo.
      */
    public SocketServidor()
    {
        
    	
    	try
    	{
    		// Se crea un socket servidor atendiendo a un determinado puerto.
    		// Por ejemplo, el 25557.
    		socket = new ServerSocket (25557); //Creamos una vez el canal de comunicacion y lo dejamos abierto para recibir las 
			// Se acepata una conexión con un cliente. Esta llamada se queda
    			// bloqueada hasta que se arranque el cliente.
    		System.out.println ("Esperando cliente..."); //peticiones de los clientes
    		cliente = socket.accept();
    		System.out.println ("Conectado con cliente de " + cliente.getInetAddress());

    			// Se hace que el cierre del socket sea "gracioso". Esta llamada sólo
    			// es necesaria si cerramos el socket inmediatamente después de
    			// enviar los datos (como en este caso).
    			// setSoLinger() a true hace que el cierre del socket espere a que
    			// el cliente lea los datos, hasta un máximo de 10 segundos de espera.
    			// Si no ponemos esto, el socket se cierra inmediatamente y si el 
    			// cliente no ha tenido tiempo de leerlos, los datos se pierden.
    			cliente.setSoLinger (true, 10);

    			// Se prepara el flujo de entrada de datos, es decir, la clase encargada
    			// de leer datos del socket.
    			bufferEntrada =	new DataInputStream (cliente.getInputStream());

    			// Se crea un dato a leer y se le dice que se rellene con el flujo de
    			// entrada de datos.
    			aux = new DatoSocket("");

    			aux.readObject (bufferEntrada);
    			System.out.println ("Recibido: " + aux.toString());
    			// Se prepara un flujo de salida de datos, es decir, la clase encargada
    			// de escribir datos en el socket.
    			bufferSalida = new DataOutputStream (cliente.getOutputStream());
    			
    		
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }


	public DataOutputStream getBufferSalida() {
		return bufferSalida;
	}
	
	//Metodo encargado de cerrar el socket con el cliente
	public void cerrarSocket(){
		try {
			cliente.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
   
}
