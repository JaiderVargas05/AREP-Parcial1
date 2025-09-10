/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package escuelaing.edu.co.arep.parcial1;

/**
 *
 * @author jaider.vargas-n
 */
import java.net.*;
import java.io.*;
import java.util.LinkedList;

public class HttpServer {

    public static LinkedList numbers = new LinkedList();

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9001);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 9001.");
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
                switch (reqUri.getPath()) {
                    case "/BACKEND/list":
                        response = "{\"status\":\"OK\",\"values\":" + numbers.toString() + "}";
                        break;
                    case "/BACKEND/clear":
                        numbers.clear();
                        response = "{\"status\":\"OK\",\"error\":\"list_cleared\"}";
                        break;
                    case "/BACKEND/stats":
                        double media = 0;
                        double sum = 0;
                        double mean = 0;
                        double sumatory = 0;
                        double stddev = 0;
                        try {
                            if (numbers.isEmpty()) {
                                throw new Exception("empty_list");
                            }
                            if (numbers.size() == 1) {
                                throw new Exception("The list must have more than one value for stats");
                            }
                            for (var obj : numbers) {
                                double number = Double.parseDouble(obj.toString());
                                sum = sum + number;
                            }
                            mean = sum / numbers.size();
                            for (var obj : numbers) {
                                double number = Double.parseDouble(obj.toString());
                                sumatory = sumatory + Math.pow(number - mean, 2);
                            }
                            stddev = Math.pow(sumatory / (numbers.size() - 1), 1 / 2);
                            response = "{\"status\":\"OK\",\"mean\":" + mean + ",\"stddev\":" + stddev + "\"count\":" + numbers.size() + "}";

                        } catch (Exception e) {
                            response = "{\"status\":\"ERR\",\"error\":\"" + e.getMessage() + "\"}";
                        }
                        break;
                    case "/BACKEND/add":
                        try {
                            String stringNumber = reqUri.getQuery().split("=")[1];
                            double number = Double.parseDouble(stringNumber);
                            numbers.add(number);
                            response = "{\"status\":\"OK\",\"added\":" + number + ",\"count\":" + numbers.size() + "}";
                        } catch (Exception e) {
                            response = "{\"status\":\"ERR\",\"error\":\"invalid_number\"}";
                        }
                        break;
                }
            }
            if (response.contains("error")) {
                outputLine = "HTTP/1.1 400 BADREQUEST\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + response;
            }
            if (response.isEmpty()) {
                response = "{\"status\":\"ERR\",\"error\":\"resource_not_found\"}";

                outputLine = "HTTP/1.1 404 NOTFOUND\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + response;
            } 
            else {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + response;
            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }

        serverSocket.close();
    }
}
