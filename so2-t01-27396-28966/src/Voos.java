package so2trabalho;

import java.util.ArrayList;


public interface Voos extends java.rmi.Remote{
    
    public boolean comprar(String id, int unidades, ArrayList<String> nomes)throws java.rmi.RemoteException;
      
    public String consultar_passageiros(String id)throws java.rmi.RemoteException;
    
    public String pesquisar_destinos(String destino)throws java.rmi.RemoteException;
    
    public String consultar_voos(String data)throws java.rmi.RemoteException;
    
}