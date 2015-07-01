
package so2trabalho;

import java.rmi.server.UnicastRemoteObject;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;


public class VoosImpl extends UnicastRemoteObject implements Voos, java.io.Serializable {

    public VoosImpl() throws java.rmi.RemoteException {
        super();
    }

    @Override
    public boolean comprar(String id, int unidades, ArrayList<String> nomes) throws java.rmi.RemoteException {
        java.sql.Connection con = null;
        java.sql.Statement stmt = null;
        boolean reservado = false;

        if (unidades != nomes.size()) {
            return reservado;
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
                if (rs.getInt("lugares") - unidades >= 0) {
                    reservado = true;
                    lugaresNovo = rs.getInt("lugares") - unidades;

                }
            }

            if (reservado) {
                stmt.execute("update voo set lugares = " + lugaresNovo + " where id =" + id + ";");
                String query;
                ResultSet select = stmt.executeQuery("select id_venda from venda;");

                int i = 1;
                while (select.next()) {
                    i = i + 1;
                }
                for (String nome : nomes) {
                    query = "insert into venda values(" + i + "," + id + ",'" + nome + "');";
                    i = i + 1;
                    stmt.execute(query);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return reservado;
    }

    @Override
    public String consultar_passageiros(String id) throws java.rmi.RemoteException {
        java.sql.Connection con = null;
        java.sql.Statement stmt = null;
        String passageiros = "";

        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection("jdbc:postgresql://" + "alunos.di.uevora.pt" + ":5432/" + "l27396",
                    "l27396",
                    "1234");

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select passageiro from venda where id_voo=" + id + ";");
            while (rs.next()) {
                passageiros += rs.getString("passageiro") + "\n";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems setting the connection");

        }
        return passageiros;
    }

    @Override
    public String pesquisar_destinos(String d) throws java.rmi.RemoteException {
        java.sql.Connection con = null;
        java.sql.Statement stmt = null;
        String voos = "";
        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection("jdbc:postgresql://" + "alunos.di.uevora.pt" + ":5432/" + "l27396",
                    "l27396",
                    "1234");

            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select * from voo where destino = '" + d + "';");

            while (rs.next()) {
                int ids = rs.getInt("id");
                int lugares = rs.getInt("lugares");
                String data = rs.getString("data");
                int hora = rs.getInt("hora");
                voos = "Id: " + ids + " lugares: " + lugares + " data:" + data + " hora" + hora + "\n";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return voos;
    }

    @Override
    public String consultar_voos(String dt) throws java.rmi.RemoteException {
        java.sql.Connection con = null;
        java.sql.Statement stmt = null;
        String voos = "";
        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection("jdbc:postgresql://" + "alunos.di.uevora.pt" + ":5432/" + "l27396",
                    "l27396",
                    "1234");

            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select * from voo where data = '" + dt + "';");

            while (rs.next()) {
                int ids = rs.getInt("id");
                int lugares = rs.getInt("lugares");
                String destino = rs.getString("destino");
                int hora = rs.getInt("hora");
                voos = "Id:" + ids + " lugares: " + lugares + " destino:" + destino + " hora:" + hora + "\n";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return voos;
    }

}
