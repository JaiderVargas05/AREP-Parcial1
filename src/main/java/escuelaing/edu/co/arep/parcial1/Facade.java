/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package escuelaing.edu.co.arep.parcial1;

/**
 *
 * @author jaider.vargas-n
 */
import static escuelaing.edu.co.arep.parcial1.HttpServer.numbers;
import java.net.*;
import java.io.*;

public class Facade {

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 9000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        Boolean running = true;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine = "";
            Boolean firstLine = true;
            URI reqUri = null;
            String response = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (firstLine) {
                    reqUri = new URI(inputLine.split(" ")[1]);
                    firstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }
            if (reqUri.getPath().startsWith("/BACKEND")) {
                try {
                    response = HttpConnectionExample.execRequest(reqUri);
                    outputLine = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/html\r\n"
                            + "\r\n"
                            + "<!DOCTYPE html>\n"
                            + "<html>\n"
                            + "<head>\n"
                            + "<meta charset=\"UTF-8\">\n"
                            + "<title>Title of the document</title>\n"
                            + "</head>\n"
                            + "<body>\n"
                            + "<h1>"
                            + response
                            + "</h1>\n"
                            + "</body>\n"
                            + "</html>\n";
                } catch (Exception e) {
                    response = "{\"status\":\"ERR\",\"error\":\"backend_unreachable\"}";
                    outputLine = "HTTP/1.1 500 INTERNALERROR\r\n"
                            + "Content-Type: text/html\r\n"
                            + "\r\n"
                            + "<!DOCTYPE html>\n"
                            + "<html>\n"
                            + "<head>\n"
                            + "<meta charset=\"UTF-8\">\n"
                            + "<title>Title of the document</title>\n"
                            + "</head>\n"
                            + "<body>\n"
                            + "<h1>"
                            + response
                            + "</h1>\n"
                            + "</body>\n"
                            + "</html>\n";
                }
            } else if (reqUri.getPath().startsWith("/cliente")) {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "\n"
                        + "<head>\n"
                        + "    <title>AREP PARCIAL1</title>\n"
                        + "    <meta charset=\"UTF-8\">\n"
                        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "</head>\n"
                        + "\n"
                        + "<body>\n"
                        + "    <h1>Add Number</h1>\n"
                        + "    <form action=\"/add\">\n"
                        + "        <label for=\"name\">Number:</label><br>\n"
                        + "        <input type=\"text\" id=\"number\" name=\"number\" value=\"\"><br><br>\n"
                        + "        <input type=\"button\" value=\"Submit\" onclick=\"addNumber()\">\n"
                        + "    </form>\n"
                        + "    <div id=\"addnumberrespmsg\"></div>\n"
                        + "\n"
                        + "    <h1>Get Numbers</h1>\n"
                        + "    <form action=\"/list\">\n"
                        + "        <input type=\"button\" value=\"Submit\" onclick=\"listNumbers()\">\n"
                        + "    </form>\n"
                        + "    <div id=\"listnumbersrespmsg\"></div>\n"
                        + "\n"
                        + "    <h1>Get Stats</h1>\n"
                        + "    <form action=\"/stats\">\n"
                        + "        <input type=\"button\" value=\"Submit\" onclick=\"loadStats()\">\n"
                        + "    </form>\n"
                        + "    <div id=\"loadstatsrespmsg\"></div>\n"
                        + "\n"
                        + "    <h1>Clear Numbers</h1>\n"
                        + "    <form action=\"/clearStats\">\n"
                        + "        <input type=\"button\" value=\"Submit\" onclick=\"clearStats()\">\n"
                        + "    </form>\n"
                        + "    <div id=\"clearrespmsg\"></div>\n"
                        + "\n"
                        + "    <script>\n"
                        + "        function addNumber() {\n"
                        + "            let nameVar = document.getElementById(\"number\").value;\n"
                        + "            const xhttp = new XMLHttpRequest();\n"
                        + "            xhttp.onload = function () {\n"
                        + "                document.getElementById(\"addnumberrespmsg\").innerHTML =\n"
                        + "                    this.responseText;\n"
                        + "            }\n"
                        + "            xhttp.open(\"GET\", \"/BACKEND/add?x=\" + nameVar);\n"
                        + "            xhttp.send();\n"
                        + "        }\n"
                        + "    </script>\n"
                        + "\n"
                        + "    <script>\n"
                        + "        function listNumbers() {\n"
                        + "            const xhttp = new XMLHttpRequest();\n"
                        + "            xhttp.onload = function () {\n"
                        + "                document.getElementById(\"listnumbersrespmsg\").innerHTML =\n"
                        + "                    this.responseText;\n"
                        + "            }\n"
                        + "            xhttp.open(\"GET\", \"/BACKEND/list\");\n"
                        + "            xhttp.send();\n"
                        + "        }\n"
                        + "    </script>\n"
                        + "\n"
                        + "    <script>\n"
                        + "        function loadStats() {\n"
                        + "            const xhttp = new XMLHttpRequest();\n"
                        + "            xhttp.onload = function () {\n"
                        + "                document.getElementById(\"loadstatsrespmsg\").innerHTML =\n"
                        + "                    this.responseText;\n"
                        + "            }\n"
                        + "            xhttp.open(\"GET\", \"/BACKEND/stats\");\n"
                        + "            xhttp.send();\n"
                        + "        }\n"
                        + "    </script>\n"
                        + "\n"
                        + "    <script>\n"
                        + "        function clearStats() {\n"
                        + "            const xhttp = new XMLHttpRequest();\n"
                        + "            xhttp.onload = function () {\n"
                        + "                document.getElementById(\"clearrespmsg\").innerHTML =\n"
                        + "                    this.responseText;\n"
                        + "            }\n"
                        + "            xhttp.open(\"GET\", \"/BACKEND/clear\");\n"
                        + "            xhttp.send();\n"
                        + "        }\n"
                        + "    </script>\n"
                        + "\n"
                        + "</body>\n"
                        + "\n"
                        + "</html>";
            } else {
                outputLine = "HTTP/1.1 404 NOTFOUND\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>Title of the document</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h1>Page not found, go to /cliente</h1>\n"
                        + "</body>\n"
                        + "</html>\n";
            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
}
