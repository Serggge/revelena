package ru.ravelena.lettercounter.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CounterServiceImpl implements CounterService {
    @Override
    public Map<Character, Integer> count(String input) {
        Map<Character, Integer> result = new HashMap<>();
        input.chars().forEach(ch -> {
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
}
