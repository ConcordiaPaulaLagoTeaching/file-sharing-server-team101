package ca.concordia;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRunnable implements Runnable {

    private final int id;
    private final int mode;
    public static volatile boolean DISCONNECT = false; // controlled shutdown

    private String[] getExistingFiles(PrintWriter writer, BufferedReader reader) throws Exception {
        writer.println("LIST");
        Thread.sleep(10);

        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            if (line.equals("FILES:") || line.equals("No files on server.")) {
                continue;
            }
            sb.append(line).append("\n");
            if (!reader.ready()) break;
        }

        String all = sb.toString().trim();
        if (all.isEmpty()) return new String[0];

        return all.split("\n");
    }


    public ClientRunnable(int id, int mode) {
        this.id = id;
        this.mode = mode;
    }

    @Override
    public void run() {

        try (Socket clientSocket = new Socket("localhost", 12345);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            Thread.sleep(10);
            while (reader.ready()) reader.readLine();

            String filename;

            if (mode == 1) {
                // for create format (instead of random or type one by one)
                filename = "file" + id + ".txt";
            } else {
                // for read, create, list and delete mode
                String[] files = getExistingFiles(writer, reader);

                if (files.length == 0) {
                    System.out.println("[Client " + id + "] No files exist — creating a new one.");

                    filename = "file" + id + ".txt";
                    writer.println("CREATE " + filename);
                    reader.readLine(); // consume response
                } else {
                    filename = files[id % files.length]; // pick an existing file
                }
            }


            switch (mode) {
                case 1 -> doCreate(writer, reader, filename);
                case 2 -> doWrite(writer, reader, filename);
                case 3 -> doRead(writer, reader, filename);
                case 4 -> doDelete(writer, reader, filename);
                case 5 -> doList(writer, reader);
                case 6 -> doQuit(writer);
            }
            try {
                Thread.sleep(50 + (int) (Math.random() * 150));
            } catch (InterruptedException ignored) {
            }
            writer.println("QUIT");

        } catch (InterruptedException ie) {
            return;
        } catch (Exception e) {
            System.err.println("[Client " + id + "] Error: " + e.getMessage());
        }
    }

    private void doCreate(PrintWriter writer, BufferedReader reader, String filename) throws Exception {
        writer.println("CREATE " + filename);
        System.out.println("[CREATE] Thread " + id + " -> " + filename);
        System.out.println(reader.readLine());
    }

    private void doWrite(PrintWriter writer, BufferedReader reader, String filename) throws Exception {
        Thread.sleep(20 + (int) (Math.random() * 100)); // slight delay for concurrency test
        String data = "\"Thread " + id + " is writing to " + filename + "\"";

        writer.println("WRITE " + filename + " " + data);
        System.out.println("[WRITE] Thread " + id + " sent: " + data);
        System.out.println("[WRITE] Server -> " + reader.readLine());
    }

    private void doRead(PrintWriter writer, BufferedReader reader, String filename) throws Exception {
        writer.println("READ " + filename);
        System.out.println("[READ] Thread " + id + " reading " + filename);

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("[READ] " + id + ": " + line);
            if (line.equals("END")) break;
        }
    }

    private void doDelete(PrintWriter writer, BufferedReader reader, String filename) throws Exception {
        writer.println("DELETE " + filename);
        System.out.println("[DELETE] Thread " + id + " deleting " + filename);
        System.out.println("[DELETE] Server -> " + reader.readLine());
    }

    private void doList(PrintWriter writer, BufferedReader reader) throws Exception {
        writer.println("LIST");
        System.out.println("[LIST] Thread " + id + " listing files");

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("[LIST] " + id + ": " + line);
            if (!reader.ready()) break;
        }
    }
    private void doQuit(PrintWriter writer) throws Exception {
        writer.println("QUIT");
        System.out.println("[QUIT] Thread " + id + " quiting");
    }
}
