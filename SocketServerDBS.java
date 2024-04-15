package SocketServerClientDBS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class SocketServerDBS {

    public SocketServerDBS() {
        // Constructor vacío
    }

    public static void main(String[] args) {
        final int PORT = 9998;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor escuchando en el puerto " + PORT);

            // Cargar el controlador JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión
            String url = "jdbc:mysql://172.19.0.2:3306/db_ciudades_personas";
            String usuario = "norman";
            String contraseña = "qweqweqwe";
            try (Connection conn = DriverManager.getConnection(url, usuario, contraseña)) {
                System.out.println("Configuracion de la base de dato: OK");
                Statement stmt = conn.createStatement();
                System.out.println("Configuracion del statemente : OK");
                while (true) {
                    try (Socket socket = serverSocket.accept()) {
                        System.out.println("Cliente conectado desde " + socket.getInetAddress().getHostAddress());

                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                        String data="";                   
                        
                        while ((data = reader.readLine()) != null) {
                            try {
                                ResultSet rs = stmt.executeQuery(data);
                                System.out.println("Leyendo datos desde Base de datos...");
                                
                                java.sql.ResultSetMetaData metaData = rs.getMetaData();
                                int columnCount = metaData.getColumnCount();

                                while (rs.next()) {
                                    StringBuilder salida = new StringBuilder();
                                    for (int i = 1; i <= columnCount; i++) {
                                        salida.append(metaData.getColumnName(i)).append(": ").append(rs.getString(i)).append(", ");
                                    }
                                    System.out.println(salida.toString());
                                    writer.println("Consulta ejecutada");
                                    writer.println(salida.toString());
                                }
                                
                               
                            } catch (SQLException e) {
                                System.out.println("Consulta invalida y/o datos no existen" + e.getMessage());
                            }
                        }
                    } catch (IOException ex) {
                        System.out.println("Error al aceptar la conexión del cliente: " + ex.getMessage());
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            }
        } catch (IOException ex) {
            System.out.println("Error al iniciar el servidor: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al cargar el controlador JDBC: " + ex.getMessage());
        }
    }
}
