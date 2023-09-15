package ru.ravelena.lettercounter.service;

import org.springframework.stereotype.Service;
import ru.ravelena.lettercounter.InputDto;
import ru.ravelena.lettercounter.util.Condition;
import ru.ravelena.lettercounter.util.PredicateFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CounterServiceImpl implements CounterService {

    @Override
    public Map<Character, Integer> count(String input) {

        Map<Character, Integer> result = new HashMap<>();
        input.chars()
                .forEach(ch -> {
                    int count = result.getOrDefault((char) ch, 0);
                    result.put((char) ch, ++count);
                });
        return result.entrySet()
                .stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    @Override
    public Map<Character, Integer> count(InputDto inputDto) {
        return count(inputDto.getLine());
    }

    @Override
    public Map<Character, Long> countWithParams(String search, Set<Condition> conditions) {
        Predicate<Character> totalPredicate = (conditions == null || conditions.isEmpty() || conditions.contains(Condition.NONE))
                ? x -> true
                : conditions.stream()
                .map(PredicateFactory::getPredicate)
                .reduce(Predicate::or)
                .orElse(x -> false);
        Map<Character, Long> result = search.codePoints()
                .mapToObj(ch -> (char) ch)
                .filter(totalPredicate)
                .collect(Collectors.groupingBy(Character::charValue, Collectors.counting()));
        return result.entrySet()
                .stream()
                .sorted(Map.Entry.<Character, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
