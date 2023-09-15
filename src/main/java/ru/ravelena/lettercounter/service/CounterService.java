package ru.ravelena.lettercounter.service;

import ru.ravelena.lettercounter.InputDto;
import ru.ravelena.lettercounter.util.Condition;
import java.util.Map;
import java.util.Set;

public interface CounterService {

    Map<Character, Integer> count(String input);

    Map<Character, Integer> count(InputDto inputDto);

    Map<Character, Long> countWithParams(String search, Set<Condition> conditions);
}
