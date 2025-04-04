import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;



/*
 * TO RUN
 * Navigate to src/server/
 * run javac Server.java in the terminal to compile
 * run java Server to run the server
 * Output server is listening on PORT should appear
 */
public class Server {

    private static final int PORT = 12345; // Port to listen on
    private static final ExecutorService executor = Executors.newFixedThreadPool(10); // Executor service for handling client requests

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                // Accept client connections
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Handle each client request in a separate task
                executor.submit(new FactorTask(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Task to compute factors of a number
    private static class FactorTask implements Runnable {

        private final Socket socket;

        public FactorTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                // Read the number to be factored from the client
                int n = (int) in.readObject();

                // Compute the factors
                List<Integer> factors = getFactors(n);

                // Send the list of factors back to the client
                out.writeObject(factors);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Compute factors of a number
        private List<Integer> getFactors(int n) {
            List<Integer> factors = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                if (n % i == 0) {
                    factors.add(i);
                }
            }
            return factors;
        }
    }
}
