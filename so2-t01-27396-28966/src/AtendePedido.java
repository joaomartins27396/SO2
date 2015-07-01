
package so2trabalho;


public class AtendePedido{
    
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
