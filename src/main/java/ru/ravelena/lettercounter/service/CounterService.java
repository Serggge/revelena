package ru.ravelena.lettercounter.service;

import java.util.Map;

public interface CounterService {

    Map<Character, Integer> count(String input);
}
