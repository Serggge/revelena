package ru.ravelena.lettercounter.service;

import org.springframework.stereotype.Service;
import ru.ravelena.lettercounter.dto.InputDto;
import ru.ravelena.lettercounter.util.Condition;
import ru.ravelena.lettercounter.util.PredicateFactory;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CounterServiceImpl implements CounterService {

    @Override
    public Map<Character, Long> simpleCount(String text) {
        return countWithParams(text, Collections.emptySet());
    }

    @Override
    public Map<Character, Long> simpleCount(InputDto inputDto) {
        return simpleCount(inputDto.getText());
    }

    @Override
    public Map<Character, Long> countWithParams(String text, Set<Condition> conditions) {
        Predicate<Character> totalPredicate = (conditions == null || conditions.isEmpty() || conditions.contains(Condition.NONE))
                ? x -> true
                : conditions.stream()
                .map(PredicateFactory::getPredicate)
                .reduce(Predicate::or)
                .orElse(x -> false);
        Map<Character, Long> result = text.codePoints()
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

    @Override
    public Map<String, Long> countByTemplate(String text, Set<String> templates) {
        if (templates == null || templates.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Long> result = templates.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        template -> Pattern.compile(template)
                                .matcher(text)
                                .results()
                                .count()));
        return result.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

}
