/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package live.chat.server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author panos
 */
public class HandleClient implements Runnable {

    private ArrayList<Socket> clients;
    private Socket currentClient;
    private InputStream clientInput;
    private OutputStream clientOutput;

    public HandleClient(Socket clientSocket, ArrayList<Socket> clientList) throws IOException {
        //this.clients = new ArrayList<Socket>();
        this.clients = clientList;
        this.currentClient = clientSocket;
        //clientSocket.getInputStream()
        this.clientInput = currentClient.getInputStream();
        this.clientOutput = currentClient.getOutputStream();

    }

    public void run() {
        try {
            String line = "";
            int data = 0;
            while (!this.currentClient.isClosed()) {
                System.out.println("Waiting for data...");
                try{
                while ((data = clientInput.read()) != 4) {
                    System.out.println(data);
                    
                    line += (char) data;
                }}catch(SocketException ex){
                    line = "\\bye";
                    System.out.println("client forced closed");
                }
                data=0;
                System.out.println("got msg : " + line + " clients size : " + clients.size());
                if (line.equals("\\bye")) {
                    this.currentClient.close();
                    this.clientInput.close();
                    this.clientOutput.close();
                    System.out.println("did client removed from the clients list = " + clients.remove(currentClient));
                } 
                else {
                    for (Socket client : clients) {
                        OutputStream clientOut = client.getOutputStream();
                        //char[] message = line.toCharArray();
                        byte[] bytes = line.getBytes();
                        clientOut.write(bytes);
                        clientOut.write(4);
                    }
                    line = "";
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Thread has reached the end of it's execution for client: " + this.currentClient.getInetAddress().toString());
    }

    public ArrayList<Socket> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Socket> clients) {
        this.clients = clients;
    }

}
