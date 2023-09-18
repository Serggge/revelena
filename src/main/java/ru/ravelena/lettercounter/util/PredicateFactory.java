package ru.ravelena.lettercounter.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class PredicateFactory {

     private static final Map<Condition, Predicate<Character>> predicates = new HashMap<>();

    static {
        predicates.put(Condition.LETTER, Character::isLetter);
        predicates.put(Condition.ALPHABET, Character::isAlphabetic);
        predicates.put(Condition.NUMBER, Character::isDigit);
        predicates.put(Condition.NUMBER_OR_DIGIT, Character::isLetterOrDigit);
        predicates.put(Condition.LOWER, Character::isLowerCase);
        predicates.put(Condition.UPPER, Character::isUpperCase);
        predicates.put(Condition.WHITESPACE, Character::isWhitespace);
    }

    private PredicateFactory() {

    }

    public static Predicate<Character> getPredicate(Condition condition) {
        return predicates.get(condition);
    }
}
