package ru.ravelena.lettercounter.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.ravelena.lettercounter.service.CounterService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;

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
                        .contentType(MediaType.APPLICATION_JSON)
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
    void calculateWithDto() {
    }

    @Test
    void calculateWithParams() {
    }

    @Test
    void calculateTemplateCount() {
    }

}