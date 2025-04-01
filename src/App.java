/**
 * Entry point to the application, calls the display funciton in the menu class. The menu class handles all menu related operations.
 */
public class App {
    public static void main(String[] args) throws Exception {
        Menu menu = new Menu();
        menu.display();
    }
}
