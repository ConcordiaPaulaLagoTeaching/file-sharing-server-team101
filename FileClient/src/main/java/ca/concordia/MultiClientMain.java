package ca.concordia;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiClientMain {
    public static void main(String[] args) {

        try {
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

            // THREAD POOL
            ExecutorService pool = Executors.newFixedThreadPool(50);

            for (int i = 0; i < count; i++) {
                pool.execute(new ClientRunnable(i, mode));
            }

            pool.shutdown();

            // WAIT FOR ALL CLIENTS
            if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("Timeout: some threads still running.");
            }

            sc.close();

        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }
    }
}
