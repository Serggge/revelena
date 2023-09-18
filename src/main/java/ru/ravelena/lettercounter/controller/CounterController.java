package ru.ravelena.lettercounter.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ravelena.lettercounter.dto.InputDto;
import ru.ravelena.lettercounter.service.CounterService;
import ru.ravelena.lettercounter.util.Condition;
import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor(onConstructor__ = {@Autowired})
@RequestMapping("/")
@Validated
public class CounterController {

    private final CounterService counterService;

    @GetMapping("/count")
    public Map<Character, Long> calculate(@RequestBody @Length(min = 1) String text) {
        return counterService.simpleCount(text);
    }

    @GetMapping("/dto-count")
    public Map<Character, Long> calculateWithDto(@RequestBody @Valid InputDto dto) {
        return counterService.simpleCount(dto);
    }

    @GetMapping("/params-count")
    public Map<Character, Long> calculateWithParams(@RequestParam @Length(min = 1) String text,
                                                    @RequestParam(defaultValue = "NONE") Set<Condition> conditions) {
        return counterService.countWithParams(text, conditions);
    }

    @GetMapping("/template-count")
    public Map<String, Long> calculateTemplateCount(@RequestParam @Length(min = 1) String text,
                                                    @RequestParam Set<String> templates) {
        return counterService.countByTemplate(text, templates);
    }

}
