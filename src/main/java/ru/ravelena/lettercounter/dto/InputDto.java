package ru.ravelena.lettercounter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class InputDto {

    @NotBlank
    private String line;
}
