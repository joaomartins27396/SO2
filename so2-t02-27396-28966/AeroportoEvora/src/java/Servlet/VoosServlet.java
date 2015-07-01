/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trabalho2.servidor.Voos;
import trabalho2.servidor.VoosImpl;

/**
 *
 * @author joaomartins
 */
public class VoosServlet extends HttpServlet {

    //static List<String> nomes;
    Voos voos;
    private static String address = "localhost";
    private static int sPort = 9000;

    public void init() {
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            voos = (Voos) java.rmi.Naming.lookup("rmi://" + address + ":" + sPort + "/voos");
        } catch (NotBoundException ex) {
            Logger.getLogger(VoosServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(VoosServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(VoosServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            String novoNome = request.getParameter("nome");

            if (request.getRequestURI().endsWith("/consulta")) {

                out.println("<p>Consulte aqui os Passageiros </p>");

                out.println(" <p><form name=\"formulario\" action=\"consultaResultado\" method=\"POST\">"
                        + "ID Voo: <input type=\"text\" name=\"idvoo\" size=\"30\">"
                        + "<input type=\"submit\" value=\"Consulte\">"
                        + "</form>"
                        + "</p>");
            } else if (request.getRequestURI().endsWith("/consultaResultado")) {
                out.println("<p> <h2> Lista de Passageiros  </h2></p>");
                String IDVoo = request.getParameter("idvoo");
                String passageiros = voos.consultar_passageiros(IDVoo);

                for (String pass : passageiros.split(";")) {

                    out.println("<p> Passageiro: " + pass + "</p>");

                }

            } else if (request.getRequestURI().endsWith("/compra")) {

                String numerPassageiros = request.getParameter("numeroPassageirosCompra");
                String nomePassageirosCompra = request.getParameter("nomePassageirosCompra");

                if (numerPassageiros != null && nomePassageirosCompra != null) {
                    out.println("<p>Compre aqui os Bilhetes </p>");

                    out.println(" <p><form name=\"formulario\" action=\"compraEfetuada\" method=\"POST\">"
                            + "ID Voo: <input type=\"text\" name=\"idvoo\" size=\"30\">"
                            + "</p>");

                    out.println("<p> Numero de Passageiros: <input type=\"text\" value=\"" + numerPassageiros + "\"name=\"numeroPassageirosCompra\" size=\"30\"> </p>");

                    out.println("<p>Nome de Passageiros: <input type=\"text\" value=\"" + nomePassageirosCompra + "\" name=\"nomePassageirosCompra\" size=\"30\"> </p>");

                    out.println("<input type=\"submit\" value=\"compre\">"
                            + "</form>"
                            + "</p>");
                } else {
                    out.println("<p>Compre aqui os Bilhetes </p>");

                    out.println(" <p><form name=\"formulario\" action=\"compraEfetuada\" method=\"POST\">"
                            + "ID Voo: <input type=\"text\" name=\"idvoo\" size=\"30\">"
                            + "</p>");

                    out.println("<p> Numero de Passageiros: <input type=\"text\" name=\"numeroPassageirosCompra\" size=\"30\"> </p>");

                    out.println("<p>Nome de Passageiros: <input type=\"text\" name=\"nomePassageirosCompra\" size=\"30\"> </p>");

                    out.println("<input type=\"submit\" value=\"compre\">"
                            + "</form>"
                            + "</p>");
                }
            } else if (request.getRequestURI().endsWith("/compraEfetuada")) {

                String IDVoo = request.getParameter("idvoo");
                String numerPassageiros = request.getParameter("numeroPassageirosCompra");
                String nomePassageirosCompra = request.getParameter("nomePassageirosCompra");

                String info = voos.comprar(IDVoo, numerPassageiros, nomePassageirosCompra);

                if (info.equals("fail")) {
                    out.println("<p> <h2> Compra Impossibilitada  </h2></p>");
                } else {
                    out.println("<p> <h2> Compra Efetuada  </h2></p>");
                    String[] infoArray = info.split(";");
                    String idvenda = infoArray[1];
                    File file = new File("recibo_" + idvenda + ".txt");

                    FileOutputStream fop = null;
                    try {
                        fop = new FileOutputStream(file);

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(VoosImpl.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(VoosImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    for (String des : info.split(";")) {
                        out.println("<p>" + des + "</p>");
                        fop.write(des.getBytes());
                    }
                }

            } else if (request.getRequestURI().endsWith("/pesquisa")) {
                out.println("<p>Pesquise aqui os voos </p>");

                out.println(" <p><form name=\"formulario\" action=\"pesquisaEfetuada\" method=\"POST\">"
                        + "Destino: <input type=\"text\" name=\"destino\" size=\"30\">"
                        + "<input type=\"submit\" value=\"pesquise\">"
                        + "</form>"
                        + "</p>");
            } else if (request.getRequestURI().endsWith("/pesquisaEfetuada")) {

                out.println("<p> <h2> Lista destinos  </h2></p>");

                for (String des : voos.pesquisar_destinos(request.getParameter("destino")).split(";")) {

                    // id voo, numero passageiros, nome passageiros
                    out.println(" <p><form name=\"formulario\" action=\"compra\" method=\"POST\">" + "<p> Destinos: " + des + "</p>"
                            + " Numero de Passageiros <input type=\"text\" name=\"numeroPassageirosCompra\" size=\"30\">"
                            + " Nome de Passageiros <input type=\"text\" name=\"nomePassageirosCompra\" size=\"30\">"
                            + "<input type=\"submit\" value=\"compre\">"
                            + "</form>"
                            + "</p>");

                }

            }

            out.println("<p> </p>");
            out.println("<a href=\"javascript: history.go(-1)\">voltar</a>");

            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet voo</title>");
            out.println("</head>");
            out.println("<body>");
            //out.println("<h1>Servlet Peticao at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
