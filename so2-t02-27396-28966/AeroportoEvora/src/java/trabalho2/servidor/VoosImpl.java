/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho2.servidor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joaomartins
 */
public class VoosImpl extends UnicastRemoteObject implements Voos, java.io.Serializable {

    java.sql.Connection con = null;
    java.sql.Statement stmt = null;

    public VoosImpl() throws java.rmi.RemoteException {
        super();
    }

    @Override
    public String comprar(String id, String unidades, String nomes) throws RemoteException {

        synchronized (this) {

            boolean reservado = false;
            int id_venda = 1;
            int un;

            String recibo = "";

            if (unidades.equals("1")) {
                un = 1;
            } else {
                un = new Integer(unidades);
            }

            try {
                Class.forName("org.postgresql.Driver");

                con = DriverManager.getConnection("jdbc:postgresql://" + "alunos.di.uevora.pt" + ":5432/" + "l27396",
                        "l27396",
                        "1234");

                stmt = con.createStatement();

                int lugaresNovo = 0;

                ResultSet rs = stmt.executeQuery("select lugares from voo where id=" + id + ";");
                while (rs.next()) {
                    if (rs.getInt("lugares") - un >= 0) {
                        reservado = true;
                        lugaresNovo = rs.getInt("lugares") - un;

                    }
                }

                if (reservado) {
                    stmt.execute("update voo set lugares = " + lugaresNovo + " where id =" + id + ";");
                    String query;
                    ResultSet select = stmt.executeQuery("select id_venda from venda;");

                    while (select.next()) {
                        if (select != null) {
                            id_venda = select.getInt("id_venda");
                        }
                    }
                    id_venda = id_venda + 1;
                    recibo = "Id venda; " + id_venda + "; Origem: Evora;";
                    ResultSet dest = stmt.executeQuery("select destino from voo where id=" + id + ";");
                    while (dest.next()) {
                        if (dest != null) {
                            recibo += "Destino: " + dest.getString("destino") + ";";
                        }
                    }
                    ResultSet dat = stmt.executeQuery("select data from voo where id=" + id + ";");
                    while (dat.next()) {
                        if (dat != null) {
                            recibo += dat.getString("data") + ";";
                        }

                    }
                    recibo += "Passageiro;" + nomes + ";";

                    for (String nome : nomes.split(";")) {
                        query = "insert into venda values(" + id_venda + "," + id + ",'" + nome + "');";
                        stmt.execute(query);
                        query = "insert into venda1 values(" + id_venda + "," + id + ",'" + nome + "');";
                        stmt.execute(query);
                        query = "insert into venda2 values(" + id_venda + "," + id + ",'" + nome + "');";
                        stmt.execute(query);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            File file = new File("digester_" + id_venda + ".txt");
            FileOutputStream fop;

            try {
                fop = new FileOutputStream(file);
                MessageDigest md = null;
                md = MessageDigest.getInstance("SHA-1");
                byte[] ciph = md.digest(recibo.getBytes());
                fop.write(ciph);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(VoosImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VoosImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(VoosImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!reservado) {
                return "fail";
            }
            return recibo;
        }
    }

    @Override
    public String consultar_passageiros(String id) throws RemoteException {
        String passageiros = "";
        int id2 = new Integer(id);

        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection("jdbc:postgresql://" + "alunos.di.uevora.pt" + ":5432/" + "l27396",
                    "l27396",
                    "1234");

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select passageiro from venda where id_voo=" + id2 + ";");
            while (rs.next()) {
                passageiros += rs.getString("passageiro") + ";";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems setting the connection");

        }
        return passageiros;
    }

    @Override
    public String pesquisar_destinos(String destino) throws RemoteException {
        String voos = "";
        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection("jdbc:postgresql://" + "alunos.di.uevora.pt" + ":5432/" + "l27396",
                    "l27396",
                    "1234");

            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select * from voo where destino = '" + destino + "';");

            while (rs.next()) {
                int ids = rs.getInt("id");
                int lugares = rs.getInt("lugares");
                String data = rs.getString("data");
                int hora = rs.getInt("hora");
                voos += "Id: " + ids + " lugares: " + lugares + " data:" + data + " hora" + hora + ";";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return voos;
    }

}
