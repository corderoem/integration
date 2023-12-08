import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Book {
    String title;
    String author;
    int quantity;
    boolean available;
    LocalDate dueDate;

    public Book(String title, String author, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.available = true;
    }
}

class CheckoutSystem {
    List<Book> catalog;
    List<Book> selectedBooks;
    LocalDate currentDate; 
    int loanPeriod = 14;  
    double lateFeeRate = 1.0; 


    public CheckoutSystem() {
        catalog = new ArrayList<>();
        selectedBooks = new ArrayList<>();
        initializeCatalog();
        currentDate = LocalDate.now();
    }

    private void initializeCatalog() {
        // Add some sample books to the catalog
        catalog.add(new Book("Don Quijote de la Mancha", "Miguel de Cervantes", 5));
        catalog.add(new Book("Romeo y Julieta", "William Shakespeare", 3));
        catalog.add(new Book("La Odisea", "Homero", 3));
    }

    public void displayCatalog() {
        System.out.println("Catalog:");
        for (Book book : catalog) {
            System.out.println(book.title + " by " + book.author + " - Available: " + book.quantity);
        }
    }

    public void checkoutBooks() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of books to checkout (max 10): ");
        int numBooks = scanner.nextInt();

        if (numBooks <= 0 || numBooks > 10) {
            System.out.println("Invalid quantity. Please enter a number between 1 and 10.");
            return;
        }

        for (int i = 0; i < numBooks; i++) {
            displayCatalog();
            System.out.println("Enter the title of book " + (i + 1) + ": ");
            scanner.nextLine();
            String title = scanner.nextLine();

            Book selectedBook = findBook(title);
            if (selectedBook == null || !selectedBook.available) {
                System.out.println("Book not available. Please select another book.");
                i--;  // Retry the current book selection
            } else {
                System.out.println("Enter the quantity for book " + (i + 1) + ": ");
                int quantity = scanner.nextInt();

                if (quantity <= 0 || quantity > selectedBook.quantity) {
                    System.out.println("Invalid quantity. Please enter a positive number less than or equal to "
                            + selectedBook.quantity + ".");
                    i--;  // Retry the current book selection
                } else {
                    selectedBook.quantity -= quantity;
                    selectedBook.dueDate = currentDate.plusDays(loanPeriod); 
                    selectedBooks.add(new Book(selectedBook.title, selectedBook.author, quantity));
                }
            }
        }

        displayCheckoutDetails();
    }

    private Book findBook(String title) {
        for (Book book : catalog) {
            if (book.title.equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    private double calculateLateFees(LocalDate currentDate, LocalDate dueDate) {
        
        if (dueDate == null) {
        // Handle the case where dueDate is not initialized (book not found, etc.)
        return 0.0;
        }

        long daysLate = currentDate.until(dueDate).getDays();
        if (daysLate > 0) {
            return daysLate * lateFeeRate;
        } else {
            return 0.0; // No late fees if returned on or before the due date
        }
    }

    private void displayCheckoutDetails() {
        System.out.println("Selected Books for Checkout:");
        for (Book book : selectedBooks) {
            System.out.println(book.title + " by " + book.author + " - Quantity: " + book.quantity
                        + " - Due Date: " + book.dueDate);
        }

        // due date calculation 
        double totalLateFees = 0.0;
        System.out.println("Late Fees:");
        for (Book book : selectedBooks) {
            double lateFees = calculateLateFees(currentDate, book.dueDate);
            totalLateFees += lateFees;
            System.out.println(book.title + " - Late Fees: $" + lateFees);
        }

        System.out.println("Total Late Fees: $" + totalLateFees);


        System.out.println("Do you want to confirm the checkout? (yes/no)");
        Scanner scanner = new Scanner(System.in);
        String confirmation = scanner.next();

        if ("yes".equalsIgnoreCase(confirmation)) {
            System.out.println("Checkout confirmed. Due date and any applicable late fees calculated.");
            // Add code for due date calculation and late fee calculation
        } else {
            System.out.println("Checkout canceled. Please make changes to your selections.");
        }
    }

}

public class LibrarySystemApp {
    public static void main(String[] args) {
        CheckoutSystem checkoutSystem = new CheckoutSystem();
        checkoutSystem.checkoutBooks();
        // Add code for return process and other features as needed
    }
}
