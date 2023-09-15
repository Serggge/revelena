package ru.ravelena.lettercounter.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ravelena.lettercounter.InputDto;
import ru.ravelena.lettercounter.service.CounterService;
import ru.ravelena.lettercounter.util.Condition;
import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor(onConstructor__ = {@Autowired})
@Validated
public class CounterController {

    private final CounterService counterService;

    @GetMapping("/count")
    public Map<Character, Integer> calculate(@RequestBody @Length(min = 1) String input) {
        return counterService.count(input);
    }

    @GetMapping("/dto-count")
    public Map<Character, Integer> calculateWithDto(@RequestBody @Valid InputDto dto) {
        return counterService.count(dto);
    }

    @GetMapping("/params-count")
    public Map<Character, Long> calculateWithParams(@RequestParam @Length(min = 1) String search,
                                                       @RequestParam(defaultValue = "NONE") Set<Condition> conditions) {
        return counterService.countWithParams(search, conditions);
    }

}
