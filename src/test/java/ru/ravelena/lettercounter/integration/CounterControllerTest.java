package ru.ravelena.lettercounter.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.ravelena.lettercounter.dto.InputDto;
import ru.ravelena.lettercounter.service.CounterService;
import ru.ravelena.lettercounter.util.Condition;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest
class CounterControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    CounterService counterService;
    @Autowired
    ObjectMapper objectMapper;
    static LinkedHashMap<Character, Long> expected;

    @BeforeAll
    static void beforeAll() {
        expected = new LinkedHashMap<>();
        expected.put('c', 4L);
        expected.put('a', 3L);
        expected.put('b', 2L);
        expected.put('1', 1L);

    }

    @Test
    @SneakyThrows
    void calculate() {
        final String text = "aaabbcccc1";
        when(counterService.simpleCount(anyString())).thenReturn(expected);

        mvc.perform(get("/count")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(text))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", aMapWithSize(4)))
                .andExpect(jsonPath("$", allOf(
                        hasEntry("a", 3),
                        hasEntry("b", 2),
                        hasEntry("c", 4),
                        hasEntry("1", 1))))
                .andExpect(jsonPath("$[*]", contains(4, 3, 2, 1)));

        verify(counterService, times(1)).simpleCount(text);
    }

    @Test
    @SneakyThrows
    void calculateWithDto() {
        final InputDto dto = new InputDto("aaabbcccc1");
        when(counterService.simpleCount(any(InputDto.class))).thenReturn(expected);

        mvc.perform(get("/dto-count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", aMapWithSize(4)))
                .andExpect(jsonPath("$", allOf(
                        hasEntry("a", 3),
                        hasEntry("b", 2),
                        hasEntry("c", 4),
                        hasEntry("1", 1))))
                .andExpect(jsonPath("$[*]", contains(4, 3, 2, 1)));

        verify(counterService, times(1)).simpleCount(dto);
    }

    @Test
    @SneakyThrows
    void calculateWithParams() {
        final String text = "aaaaabcccc1";
        when(counterService.countWithParams(anyString(), anySet())).thenReturn(expected);

        mvc.perform(get("/params-count")
                        .queryParam("text", text)
                        .queryParam("conditions", "LETTER", "NUMBER"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", aMapWithSize(4)))
                .andExpect(jsonPath("$", allOf(
                        hasEntry("a", 3),
                        hasEntry("b", 2),
                        hasEntry("c", 4),
                        hasEntry("1", 1))))
                .andExpect(jsonPath("$[*]", contains(4, 3, 2, 1)));

        verify(counterService, times(1)).countWithParams(text, Set.of(Condition.LETTER, Condition.NUMBER));
    }

    @Test
    @SneakyThrows
    void calculateTemplateCount() {
        final String text = "catdogcat";
        Map<String, Long> expected = new HashMap<>();
        expected.put("cat", 2L);
        expected.put("dog", 1L);
        when(counterService.countByTemplate(anyString(), anySet())).thenReturn(expected);

        mvc.perform(get("/template-count")
                .queryParam("text", text)
                .queryParam("templates", "cat", "dog"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$", allOf(
                        hasEntry("cat", 2),
                        hasEntry("dog", 1))))
                .andExpect(jsonPath("$[*]", contains(2, 1)));

        verify(counterService, times(1)).countByTemplate(text, Set.of("cat", "dog"));
    }

}