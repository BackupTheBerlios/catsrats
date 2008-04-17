package es.ucm.fdi.socket;

import java.io.*;

/**
 * Dato para enviar por el socket. Sus atributos son simples y una Clase Atributo
 */
public class DatoSocket implements Serializable
 {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
   * Constructor. Guarda la cadena en el atributo d y calcula su longitud para guardarla
   * en el atricuto c.
   */
   public DatoSocket (String cadena)
   {
      // Si la cadena no es null, se guarda la cadena y su longitud 
      if (cadena != null)
      {
         d = cadena;
         
      }
   }

   
     
   /** Primer atributo, un String */
   public String d = "";
     
   /** Método para devolver un String en el que se represente el valor de
    * todos los atributos. */
   public String toString ()
   {
       String resultado;
       resultado = " --> " + d;
       return resultado;
   }

   /**
   * Método para escribir los atributos de esta clase en un DataOutputStream de forma
   * que luego pueda entenderlos un programa en C.
   */
   public void writeObject(java.io.DataOutputStream out)
         throws IOException
     {
	   
	   //Se envía la cadena como bytes.
         out.writeBytes (d);

     }
    
      /**
      * Método que lee los atributos de esta clase de un DataInputStream tal cual nos los
      * envía un programa en C.
      * Este método no contempla el caso de que se envíe una cadena "", es decir, un
      * único \0.
      */
     public void readObject(java.io.DataInputStream in)
     throws IOException
     {
    	 
         // Array de bytes auxiliar para la lectura de la cadena.
         byte [] aux = null;
         
         aux = new byte[512];    // Se le da el tamaño 
         in.read(aux, 0, 512);   // Se leen los bytes
         

        d = (new String (aux)).trim(); // Se convierten a String
        
     }
}