package ca.concordia;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiClientMain {

    public static void main(String[] args) {

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
        int poolSize = Math.min(count, 1000);
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        for (int i = 0; i < count; i++) {
            executor.submit(new ClientRunnable(i, mode));
            System.out.println("[Main] Submitted client " + i + " to pool");
        }

        executor.shutdown();  // allow pool to finish

        sc.close();
    }
}
