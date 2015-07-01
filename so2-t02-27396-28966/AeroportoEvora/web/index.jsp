<%-- 
    Document   : index
    Created on : May 14, 2015, 12:30:24 PM
    Author     : joaomartins
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Aeroporto Evora</title>
        <link rel="stylesheet" href="estilo.css">
    </head>
    <body>
        <h1>Aeroporto Evora!</h1>

        <h2>Pagina Inicial</h2>
        <p>  <a href="consulta">Consulta</a> de voos.</p>
        
        <p> <a href="compra">Compra</a> de voos.</p>
        
        <p> <a href="pesquisa">Pesquisa</a> de voos.</p>
        
        
        
        <hr>
        
        <!-- script JSP para mostrar o IP -->
        <% 
            out.println("<p>Origem do pedido: "
                +request.getRemoteAddr()+"</p>");
        %>

    </body>
</html>
