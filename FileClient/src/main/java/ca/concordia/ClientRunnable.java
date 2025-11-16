package ca.concordia;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRunnable implements Runnable {

    private final int id;

    public ClientRunnable(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        long threadId = Thread.currentThread().getId();

        try (Socket clientSocket = new Socket("localhost", 12345);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Read and ignore the banner/header from server
            Thread.sleep(10);
            while (reader.ready()) {
                reader.readLine();
            }

            // Each client creates a unique file
            String filename = "nice" + id;
            String command = "CREATE " + filename;

            writer.println(command);
            System.out.println("[Client " + id + " / thread " + threadId + "] Sent: " + command);

            // Read one response line from server
            String response = reader.readLine();
            System.out.println("[Client " + id + "] Response: " + response);

            // Cleanly disconnect
            writer.println("QUIT");

        } catch (Exception e) {
            System.err.println("[Client " + id + "] ERROR: " + e.getMessage());
        }
    }
}
