package trabalho2.servidor;

import java.util.ArrayList;


public interface Voos extends java.rmi.Remote{
    
    public String comprar(String id, String unidades, String nomes)throws java.rmi.RemoteException;
      
    public String consultar_passageiros(String id)throws java.rmi.RemoteException;
    
    public String pesquisar_destinos(String destino)throws java.rmi.RemoteException;
    
}