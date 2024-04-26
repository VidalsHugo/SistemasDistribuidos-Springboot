package org.example.dto;
import java.time.LocalDate;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.example.dominio.Sala;
import org.example.dominio.Usuario;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReservaDto(String usuario, String sala, LocalDate data, LocalTime hora) {

}