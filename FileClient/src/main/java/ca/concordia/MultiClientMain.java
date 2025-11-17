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

        int mode = 0;

        while(true) {
            System.out.println("Choose a mode:");
            System.out.println("1 — CREATE");
            System.out.println("2 — WRITE");
            System.out.println("3 — READ");
            System.out.println("4 — DELETE");
            System.out.println("5 — LIST");
            System.out.print("Enter mode: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input");
                sc.nextLine();
                continue;
            }

            mode = sc.nextInt();

            switch (mode) {
                case 1,2,3,4,5 ->
                {
                    System.out.println("Press ENTER after finish to disconnect all clients.");
                    System.out.println("\nLaunching " + count + " clients...\n");
                    //work best for more than 1000 thread can change for smaller test
                    ExecutorService pool = Executors.newFixedThreadPool(500);

                    for (int i = 0; i < count; i++) {
                        pool.submit(new ClientRunnable(i, mode));
                        Thread.sleep(5);
                    }

                    System.in.read(); // Wait for user

                    // all the client send quit
                    ClientRunnable.DISCONNECT = true;

                    pool.shutdown();

                    System.out.println("All clients disconnected.");
                    break;
                }
                default -> {
                    System.out.println("Invalid input. Try to run the program again.");
                }
            }
            break;
        }
    }
}
