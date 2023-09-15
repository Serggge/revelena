package ru.ravelena.lettercounter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class InputDto {

    @NotBlank
    private String line;
}
