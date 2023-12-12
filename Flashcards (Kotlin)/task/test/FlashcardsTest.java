import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.List;

public class FlashcardsTest extends StageTest<String> {
    private static final List<String[]> flashcards = List.of(
            new String[] { "brusque", "short and abrupt" },
            new String[] { "(a + b)^2", "a^2 + b^2 + 2ab" }
    );
    
    @Override
    public List<TestCase<String>> generate() {
        return flashcards.stream().map((flashcard) -> new TestCase<String>()
                .setDynamicTesting(() -> {
                    TestedProgram main = new TestedProgram();
                    String output;

                    output = main.start().toLowerCase();
                    if (!output.trim().startsWith("card:")) {
                        return CheckResult.wrong("Your program should print \"Card:\" as the first line");
                    }

                    if (main.isFinished())
                        return CheckResult.wrong("Your program should wait for user input");

                    String term = flashcard[0];
                    output = main.execute(term).toLowerCase();
                    String[] lines = output.trim().split("\n");
                    if (lines.length < 1 || !lines[0].trim().equals(term)) {
                        return CheckResult.wrong("Your program should print the term provided as input on the second line");
                    }

                    if (lines.length < 2 || !lines[1].trim().equals("definition:")) {
                        return CheckResult.wrong("Your program should print \"Definition:\" as the third line");
                    }

                    if (main.isFinished())
                        return CheckResult.wrong("Your program should wait for user input");

                    String definition = flashcard[1];
                    output = main.execute(definition).toLowerCase();

                    if (!output.trim().startsWith(definition)) {
                        return CheckResult.wrong("Your program should print the definition provided as input on the fourth line");
                    }

                    return CheckResult.correct();
                })
        ).toList();
    }
    
}
