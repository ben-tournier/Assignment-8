import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FineCalculator class for overdue book tracking
 * Calculates days overdue and late fees based on a book's return date
 */
public class FineCalculator {
    public static final double FINE_PER_DAY = 0.25;
    public static final double MAX_FINE_PER_BOOK = 10.00;

    // Number of days past the return date (0 if not issued or not overdue)
    public static long getDaysOverdue(Book book, LocalDate today) {
        if (book == null || !book.isIssued() || book.getReturnDate() == null) {
            return 0;
        }
        try {
            LocalDate dueDate = LocalDate.parse(book.getReturnDate());
            long days = ChronoUnit.DAYS.between(dueDate, today);
            return Math.max(0, days);
        } catch (DateTimeParseException e) {
            return 0; // Bad date in data file - don't charge a fine
        }
    }

    public static boolean isOverdue(Book book, LocalDate today) {
        return getDaysOverdue(book, today) > 0;
    }

    // Fine for one book, capped at MAX_FINE_PER_BOOK
    public static double calculateFine(Book book, LocalDate today) {
        return Math.min(getDaysOverdue(book, today) * FINE_PER_DAY, MAX_FINE_PER_BOOK);
    }

    public static List<Book> getOverdueBooks(List<Book> books, LocalDate today) {
        return books.stream()
                .filter(book -> isOverdue(book, today))
                .collect(Collectors.toList());
    }

    // Total fines currently owed by one member
    public static double getTotalFinesForMember(List<Book> books, String memberId, LocalDate today) {
        return books.stream()
                .filter(book -> book.isIssued() && memberId.equals(book.getIssuedTo()))
                .mapToDouble(book -> calculateFine(book, today))
                .sum();
    }

    // Total fines owed across the whole library
    public static double getTotalFines(List<Book> books, LocalDate today) {
        return books.stream()
                .mapToDouble(book -> calculateFine(book, today))
                .sum();
    }
}
