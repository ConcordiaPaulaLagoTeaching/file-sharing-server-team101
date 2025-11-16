package ca.concordia;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiClientMain {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("\nMULTIPLE THREADING TEST\n");
        System.out.print("How many threads do you want to launch? ");
        int count = sc.nextInt();

        System.out.println("Choose a mode:");
        System.out.println("1 — CREATE");
        System.out.println("2 — WRITE");
        System.out.println("3 — READ");
        System.out.println("4 — DELETE");
        System.out.println("5 — LIST");
        System.out.print("Enter mode: ");
        int mode = sc.nextInt();

        System.out.println("\nLaunching " + count + " clients...\n");

        ExecutorService pool = Executors.newFixedThreadPool(50);

        for (int i = 0; i < count; i++) {
            pool.submit(new ClientRunnable(i, mode));
        }

        System.out.println("\nAll clients finished their operation.");
        System.out.println("Press ENTER to disconnect all clients...");
        System.in.read(); // Wait for user

        // Tell all clients to send QUIT
        ClientRunnable.DISCONNECT = true;

        pool.shutdown();
        System.out.println("All clients disconnected.");
    }
}
