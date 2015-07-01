/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho2.servidor;


/**
 *
 * @author joaomartins
 */
public class Servidor{
    
    public static void main(String[] args) {
        int serverPort= 9000;
        try {
            VoosImpl obj = new VoosImpl();
            
            java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry(serverPort);

            registry.rebind("voos", obj);
            
            System.out.println("Bound RMI object in registry");

            System.out.println("servidor pronto");
	} 
	catch (Exception ex) {
	    ex.printStackTrace();
	}
 
   }
}
