package hr.fer.oprpp1.hw05.shell.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class CommandUtilsTest {
    @Test
    void testSplitWithQuotesWhenNoQuotes() {
        assertEquals(List.of("abc", "abc"), CommandUtils.splitWithQuotes("abc abc", Set.of(0)));
    }

    @Test
    void testSplitWithQuotesWhenSingleQuotes() {
        assertThrows(IllegalArgumentException.class, () -> CommandUtils.splitWithQuotes("\"marko", Set.of(0)));
    }

    @Test
    void testSplitWithQuotesWhenOneWithQuotesOneWithout() {
        assertEquals(List.of("a bc", "abc"), CommandUtils.splitWithQuotes("\"a bc\" abc", Set.of(0)));
    }

    @Test
    void testSplitWithQuotesWhenOneWithQuotesOneWithoutAndNoSpaceInBetween() {
        assertThrows(IllegalArgumentException.class, () -> CommandUtils.splitWithQuotes("\"abc\"abc", Set.of(0)));
    }

    @Test
    void testSplitWithQuotesWhenTwoArgumentsWithQUotesNoSpaceInBetween() {
        assertThrows(IllegalArgumentException.class,
                () -> CommandUtils.splitWithQuotes("\"abc\"\"abc\"", Set.of(0, 1)));
    }

    @Test
    void testSplitWithQuotesWhenFirstWithoutAndSecondWithQuotes() {
        assertEquals(List.of("abc", "abc"), CommandUtils.splitWithQuotes(" abc \"abc\"", Set.of(1)));
    }

    @Test
    void testSplitWithQuotesWhenSecondWithQuotesFirstWithoutAndNoSpaceInBetween() {
        assertThrows(IllegalArgumentException.class, () -> CommandUtils.splitWithQuotes("abc\"abc\"", Set.of(1)));
    }

    @Test
    void testSplitWithQuotesWhenTwoArgumentsWithQUotes() {
        assertEquals(List.of("abc", "abc"), CommandUtils.splitWithQuotes("\"abc\" \"abc\"", Set.of(0, 1)));
    }

    @Test
    void testSplitWithQuotesWhenQuotingOnIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> CommandUtils.splitWithQuotes("\"abc\" \"abc\"", Set.of(0)));
    }
}
