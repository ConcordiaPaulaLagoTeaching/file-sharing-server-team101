package ca.concordia;

public class MultiClientMain {
    public static void main(String[] args) {

        int clientCount = 10;  // default

        if (args.length > 0) {
            try {
                clientCount = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid number, using default 10 clients.");
            }
        }

        System.out.println("Starting " + clientCount + " automated clients...");

        for (int i = 0; i < clientCount; i++) {
            Thread t = new Thread(new ClientRunnable(i));
            t.start();
            System.out.println("[Main] Started client " + i + " on thread " + t.getId());
        }
    }
}
