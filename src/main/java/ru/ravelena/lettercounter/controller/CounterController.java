package ru.ravelena.lettercounter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ravelena.lettercounter.service.CounterService;

import java.util.Map;

@RestController
public class CounterController {

    @Autowired
    private CounterService srv;

    @GetMapping("/count")
    public Map<Character, Integer> calculate(@RequestBody String input) {
        return srv.count(input);
    }

}
