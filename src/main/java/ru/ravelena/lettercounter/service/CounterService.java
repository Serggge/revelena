package ru.ravelena.lettercounter.service;

import ru.ravelena.lettercounter.dto.InputDto;
import ru.ravelena.lettercounter.util.Condition;
import java.util.Map;
import java.util.Set;

public interface CounterService {

    Map<Character, Long> simpleCount(String text);

    Map<Character, Long> simpleCount(InputDto inputDto);

    Map<Character, Long> countWithParams(String text, Set<Condition> conditions);

    Map<String, Long> countByTemplate(String text, Set<String> templates);
}
