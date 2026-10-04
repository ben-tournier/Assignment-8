import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple tests for FineCalculator (no JUnit needed)
 * Run with run_tests.bat - prints PASS/FAIL for each check
 */
public class FineCalculatorTest {
    private static int passed = 0;
    private static int failed = 0;
    private static final LocalDate TODAY = LocalDate.of(2026, 10, 4);

    public static void main(String[] args) {
        Book available = new Book("B1", "Available Book", "Author", "Cat");
        Book notDueYet = issued("B2", "M001", "2026-10-10");
        Book dueToday = issued("B3", "M001", "2026-10-04");
        Book fourDaysLate = issued("B4", "M002", "2026-09-30");
        Book veryLate = issued("B5", "M002", "2026-01-01");
        Book badDate = issued("B6", "M003", "not-a-date");
        Book noDate = issued("B7", "M003", null);

        // Days overdue
        check("available book is not overdue", FineCalculator.getDaysOverdue(available, TODAY) == 0);
        check("book due in future is not overdue", FineCalculator.getDaysOverdue(notDueYet, TODAY) == 0);
        check("book due today is not overdue", FineCalculator.getDaysOverdue(dueToday, TODAY) == 0);
        check("book 4 days late = 4 days overdue", FineCalculator.getDaysOverdue(fourDaysLate, TODAY) == 4);
        check("bad date does not crash, 0 days", FineCalculator.getDaysOverdue(badDate, TODAY) == 0);
        check("missing date does not crash, 0 days", FineCalculator.getDaysOverdue(noDate, TODAY) == 0);
        check("null book does not crash, 0 days", FineCalculator.getDaysOverdue(null, TODAY) == 0);

        // Fines
        check("4 days late = $1.00 fine", close(FineCalculator.calculateFine(fourDaysLate, TODAY), 1.00));
        check("fine is capped at max", close(FineCalculator.calculateFine(veryLate, TODAY), FineCalculator.MAX_FINE_PER_BOOK));
        check("not-late book has $0 fine", close(FineCalculator.calculateFine(notDueYet, TODAY), 0));

        // Lists and totals
        List<Book> books = new ArrayList<>();
        books.add(available);
        books.add(notDueYet);
        books.add(dueToday);
        books.add(fourDaysLate);
        books.add(veryLate);
        books.add(badDate);
        check("2 overdue books found", FineCalculator.getOverdueBooks(books, TODAY).size() == 2);
        check("M002 owes $11.00", close(FineCalculator.getTotalFinesForMember(books, "M002", TODAY), 11.00));
        check("M001 owes $0.00", close(FineCalculator.getTotalFinesForMember(books, "M001", TODAY), 0));
        check("library total = $11.00", close(FineCalculator.getTotalFines(books, TODAY), 11.00));

        // Returning a book clears it from the overdue list
        fourDaysLate.returnBook();
        check("returned book is no longer overdue", !FineCalculator.isOverdue(fourDaysLate, TODAY));

        System.out.println("\n" + passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static Book issued(String id, String memberId, String returnDate) {
        return new Book(id, "Title " + id, "Author", "Cat", true, memberId, "2026-01-01", returnDate);
    }

    private static boolean close(double a, double b) {
        return Math.abs(a - b) < 0.001;
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }
}
