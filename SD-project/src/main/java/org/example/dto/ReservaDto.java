package org.example.dto;
import java.time.LocalDate;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.examples.Example;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.dominio.Sala;
import org.example.dominio.Usuario;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReservaDto(@NotBlank String usuario,
                         @NotBlank String sala,
                         @NotNull LocalDate data,
                         @JsonFormat(pattern = "HH:mm") @Schema(example = "HH:MM") String hora) {
}