/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package live.chat.server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author panos
 */

public class JavaLiveChatServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ArrayList<Socket> clients = new ArrayList<Socket>(10);
        ServerSocket server = new ServerSocket(65001);

        while (true) {
            if (clients.size() <= 10) {
                Socket client = server.accept();
                clients.add(client);
                HandleClient clientHandler = new HandleClient(client, clients);
                Thread runClient = new Thread(clientHandler);
                runClient.start();
                System.out.println("new client connected @"+client.getPort());
            } else {
                try {
                    System.out.println("Server has reached maximum capacity of " + clients.size() + " clients and won't accept any new clients");
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

}
