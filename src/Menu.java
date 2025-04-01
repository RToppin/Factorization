
import java.util.List;
import java.util.Scanner;


public class Menu {

    private Solver activeSolver = null;
    private final Scanner scanner = new Scanner(System.in);
    private boolean quit = false;


    public void display(){
        while(!quit){
            printOptions();
            int choice = getChoice(scanner);
            handleChoice(choice);

        }
        
    }

    public void printOptions(){
        // Prints out menu options (hardcoded small menu)
        System.out.println("1) Single Threaded Solver");
        System.out.println("2) Executor Solver");
        System.out.println("3) Stream Solver");
        System.out.println("4) Distributed Solver");
        System.out.println("5) Timer");
        System.out.println("0) Quit");
    }

    // Handles user input effectively
    private int getChoice(Scanner scanner) {
        while(true){
            try{
                int choice = scanner.nextInt();

                // Check for negative input
                if (choice < 0) {
                    System.out.println("Please enter a non-negative number.");
                    continue;
                }
                return choice;
            }catch(java.util.InputMismatchException e){
                System.out.println("Invalid input!");
                scanner.nextLine();
            }
        }
    }

    /**
     * Defines how each integer choice 0-5 will be handled,
     * which solver will be used, and promts the user for input.
     * @param choice
     */
    public void handleChoice(int choice){
        switch (choice){
            case 0: // Quit
                quit = true;
                System.out.println("Exiting...");
                break;
            case 1: // Single Threaded Solver
                activeSolver = new SingleThreadedSolver();
                activeSolver.precompute();

                System.out.println("\nWelcome to the Single Threaded Factorizer!");
                System.out.println("Please enter a number and we will return the factors of that number.");
                System.out.println("We'll also tell you if it's prime. Enter 0 to return to the main menu.");

                while (true) {
                    System.out.print("Enter a number: ");
                    int num = getChoice(scanner);
                    if (num == 0) break;

                    List<Integer> factors = activeSolver.getFactors(num);
                    System.out.println("Factors of " + num + ": " + factors);
                    if (factors.size() == 2 && factors.contains(1) && factors.contains(num)) {
                        System.out.println(num + " is a prime number!");
                    } else {
                        System.out.println(num + " is not a prime.");
                    }
                }
                break;
            case 2: // Executor Solver
            activeSolver = new ExecutorSolver();
                activeSolver.precompute();

                System.out.println("\nWelcome to the Executor Factorizer!");
                System.out.println("Please enter a number and we will return the factors of that number.");
                System.out.println("We'll also tell you if it's prime. Enter 0 to return to the main menu.");

                while (true) {
                    System.out.print("Enter a number: ");
                    int num = getChoice(scanner);
                    if (num == 0) break;

                    List<Integer> factors = activeSolver.getFactors(num);
                    System.out.println("Factors of " + num + ": " + factors);
                    if (factors.size() == 2 && factors.contains(1) && factors.contains(num)) {
                        System.out.println(num + " is a prime number!");
                    } else {
                        System.out.println(num + " is not a prime.");
                    }
                }
                break;
            case 3:
                System.out.println("Solver not yet implemented");
                break;
            case 4:
                System.out.println("Solver not yet implemented");
                break;
            case 5: // Timer
                if (activeSolver == null || activeSolver.getPrecomputeTimeNs() == -1) {
                    System.out.println("No solver has been run yet.");
                } else {
                    double timeSec = activeSolver.getPrecomputeTimeNs() / 1e9;
                    System.out.printf("Precompute time: %.4f seconds\n", timeSec);
                }
                break;
            default:
                System.out.println("Invalid Selection");
        }
    }
}
