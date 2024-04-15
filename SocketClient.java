package SocketServerClientDBS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClient {

    public static void main(String[] args) {
        final String SERVER_ADDRESS = "172.19.0.4";
        final int SERVER_PORT = 9998;

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // Crear un escritor de salida para enviar datos al servidor
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            // Crear un lector de entrada para recibir datos del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) { // Loop to handle multiple requests
                Scanner sc = new Scanner(System.in);
                System.out.print("Por favor, introduce la consulta: ");
                String query = sc.nextLine();

                // Send query to server
                writer.println(query);
                System.out.println("Datos enviados al servidor: " + query);

                // Get response from server
                String response;
                while ((response = reader.readLine()) != null) {
                    System.out.println("Respuesta del servidor: " + response);
                }
            }

        } catch (UnknownHostException ex) {
            System.out.println("El servidor no pudo ser encontrado: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error de E/S: " + ex.getMessage());
        }
    }
}
