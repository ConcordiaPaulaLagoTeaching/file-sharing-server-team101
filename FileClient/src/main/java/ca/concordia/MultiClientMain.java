package ca.concordia;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiClientMain {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("\nMULTIPLE THREADING TEST\n");
        System.out.print("How many threads do you want to launch? ");

        int count = 0;

        while(true){
            if(sc.hasNextInt()){
                count = sc.nextInt();
                sc.nextLine();
                break;
            } else {
                System.out.print("Invalid number. Please enter from 1-5: ");
                sc.nextLine();
            }
        }

        int mode = 0;

        while (true) {
            System.out.println("Choose a mode:");
            System.out.println("1 — CREATE");
            System.out.println("2 — WRITE");
            System.out.println("3 — READ");
            System.out.println("4 — DELETE");
            System.out.println("5 — LIST");
            System.out.println("6 — QUIT");
            System.out.print("Enter mode: ");

            String input = sc.nextLine();

            try {
                mode = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter from 1-5: ");
                continue;
            }

            if (mode < 1 || mode > 6) {
                System.out.println("Invalid mode. Please enter from 1-5: ");
                continue;
            }
            System.out.println("Press Enter after finish to end the program.");
            break;
        }
            //work best for more than 1000 thread can change for smaller test
            ExecutorService pool = Executors.newFixedThreadPool(500);
            System.out.println("\nLaunching " + count + " clients...\n");

            for (int i = 0; i < count; i++) {
                pool.submit(new ClientRunnable(i, mode));
                Thread.sleep(5);
            }

            pool.shutdown();
            System.out.println("\nAll clients finished their operation.");
            System.in.read();
            sc.close();
            System.out.println("All clients disconnected.");

        }
    }
