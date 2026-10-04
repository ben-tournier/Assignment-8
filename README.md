# CS 67 – Library Management System Assignment

Ben Tournier

**GitHub Repository:** _TODO: add link to my repo_

Original project: https://github.com/himanshu-firke/library-management-system (original README kept in [ORIGINAL_README.md](ORIGINAL_README.md))

---

## Part 1: Reading Reflection – "Coding Is Not Solved"

**1. Summarize the author's main arguments.**

The authors main arguments were talking about how despite the hype around LLM AI models they are not 'solving' software development because while these models can make the initial code a lot easier they do not currently have the ability to  make decisions and think through more complicated problems. 

**2. What claims does the author make about AI and software development?**

The main claims that the author made were talking about LLMs not being able to be held accountable when things go wrong which can happen with unchecked AI generated code. This also kind of combined with another point that was talking about how AI code is good for generating code as that is the main function but it is not good at maintaining the code to do it. He also brought up a couple other points about how code cannot understand context around it and overreliance on it can cause software developers to hurt themselves in multiple ways. On top of the technical ones I mentioned earlier, there is also an issue with the knowledge they have getting almost dusty as a result of lack of use because the script writers are only scanning AI code rarely using their own logic to use it. 

**3. Which point do you agree with most? Why?**

I agree most with the last one that talks about the harm that could be caused against developers. I know I have had some issues using AI on an assignment or project before going back and trying to do it all on my own after a couple weeks. Obviously my experience is lesser because I have not spent as much time writing code to lose that knowledge or experience. 

**4. Which point do you disagree with or question? Why?**

I think that the one I disagree with the most would be that AI is built for making code not maintaining it. And I may be going a little more extreme than he intended his point but in my basic projects or forking GitHub repositories I feel like it is not that hard to actually update and change the code.

---

## Part 2: Exploring the Codebase (Option C – Library Management System)

**Can you successfully check out a book to a member?**

Yes. Going to Issue/Return Books and choosing Issue Book works fine. After issuing a book I could see the change saved in both `data/books.txt` and `data/members.txt`.

**What happens when you select "Display Issued Books"? Why does it generate an error?**

While I did not see the error when running my code, I talked through it with the professor and we figured out what the issue was. The box that prints the book list is set to a fixed width of 70 characters, and each book's info is printed on one long line. When that line is longer than the box, the program tries to add a negative number of spaces to fill out the row, and that causes the crash.

---

## Part 3: New Feature – Overdue Books & Fines

I added overdue tracking and late fines. The new class is `FineCalculator` (in `src/FineCalculator.java`). It figures out how many days late a checked-out book is and charges $0.25 a day, up to a max of $10 per book.

To hook it into the program, I changed `Library.returnBook()` so it tells you the fine when a late book gets returned, and I added `getOverdueBooks()` and `getTotalFinesForMember()` to the `Library` class. In the menus, Issue/Return Books now has a new option, "4. View Overdue Books & Fines", which shows a table of every late book and what is owed. The "View Member's Issued Books" screen now shows that member's fines, and Library Statistics shows how many books are overdue. So there's something to look at right away, I set book B003 as checked out to member M002 with a due date of 2026-09-15, which makes it overdue.

---

## Part 4: Testing and Documentation

### 1. Description of Changes

The feature I added is overdue book tracking with late fines. I picked it because every book already gets a due date 14 days after it's checked out, but the program never actually did anything with that date. Fines felt like the most obvious missing piece of a library system, and it fit in without having to redesign anything.

I added two classes: `FineCalculator`, which is the feature itself, and `FineCalculatorTest`, which tests it. I modified `src/Library.java` to check for fines on returns and to add the overdue and fine methods, and `src/LibraryManagementApp.java` for the new menu option, the overdue table, the fines on the member screen, and the overdue count in the statistics. I also changed the root `LibraryManagementApp.java` to fix the crash from Part 2.

`FineCalculator` only has static methods, which is the same style the project already uses for `FileHandler`. `Library` and the menu code call it with a list of books and today's date. I made the date a parameter instead of having the class look it up itself, so the tests can use fixed dates and always get the same answers.

### 2. Impact Analysis

I'm fairly confident I didn't break anything. Most of my changes are new methods, and the only existing methods I touched (`returnBook()`, the issue/return menu, and the statistics screen) got small additions rather than rewrites.

For testing, I wrote `FineCalculatorTest`, which runs 15 checks and all of them pass. It covers books that aren't due yet, a book due today, a book 4 days late, the $10 cap, bad or missing dates in the data file, the total a member owes, and making sure a book stops counting as overdue once it's returned. I also ran the program myself and checked that the overdue table, the member fines, and the statistics showed the right numbers, and that returning B003 late printed the fine and removed it from the overdue list. I then went back through the original features (issuing, returning, and displaying issued books) to make sure they still worked and that the data files saved correctly. In the root version I checked that Display Issued Books and Display Available Books no longer crash.

My testing does have limits. I didn't use JUnit, so the tests are just a main method that prints PASS or FAIL. The menus were only tested by hand, and I didn't go through every single menu option after making the changes.

There are a few places where bugs could still be hiding. Fines aren't saved anywhere and there's no way to pay one; they just get recalculated from the due date every time. The new feature is only in the `src/` version of the program, not the root copy. There are also some problems that were already in the project that I didn't fix: a comma in a book title or name will break the data files since they're comma separated, there's a junk book (`21,12,12,1`) in `data/books.txt`, and the root version has other fixed-width boxes (menus, error and info messages) that could crash the same way as Part 2 if the text gets long enough.

---

## How to Run

To run the program, open a terminal in the project folder and run:

```
javac -d build src/*.java
java -cp build LibraryManagementApp
```

To run the tests, use `run_tests.bat`, or run the same `javac` command followed by `java -cp build FineCalculatorTest`.

The original `compile_and_run.bat` runs the program from inside the `build/` folder, which means it uses its own separate `build/data/` folder and won't show the overdue sample book. Running the commands above from the project folder avoids that.


## AI Citation 

Claude code Opus 5.5 was used at several points to brainstorm, implement, and rephrase adjustments to the code that satisfied the assignments requirements. It was used to brainstorm options to do and help find where and how to put these into the program. Then helped me to proofread and summarize all that was added. 