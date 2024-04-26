package org.example.controller;
import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.dto.SalaDto;
import org.example.dto.UsuarioDto;
import org.example.repository.SalaRepository;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.example.dominio.Reserva;
import org.example.dominio.Sala;
import org.example.dominio.Usuario;
import org.example.dto.ReservaDto;
import org.example.repository.ReservaRepository;


@RestController
@RequestMapping("/api")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SalaRepository salaRepository;

    @PostMapping("/cadastrarReserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva cadastrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Parametro não encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro: Sala não disponível para reserva")
    })
    public ResponseEntity<?> cadastrarReserva(@RequestBody ReservaDto reservaDto) {

        Usuario usuario = usuarioRepository.findByName(reservaDto.usuario());
        Sala sala = salaRepository.findByName(reservaDto.sala());

        if(usuario == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: Usuario não encontrado.");
        }
        if(sala == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: Sala não encontrada.");
        }

        if(reservaRepository.existsBySalaAndDataAndHorario(reservaDto.sala(), reservaDto.data(), reservaDto.hora())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Sala não disponível para reserva.");
        }else {
            Reserva reserva = new Reserva(usuario, sala, reservaDto.data(), reservaDto.hora());
            reservaRepository.save(reserva);

            return ResponseEntity.status(HttpStatus.CREATED).body(reservaRepository.save(reserva));
        }
    }

    @DeleteMapping("/removerReserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Erro: Reserva não encontrada"),
    })
    public ResponseEntity<?> removerReserva(@RequestBody ReservaDto reservaDto) {
        if(reservaRepository.existsBySalaAndDataAndHorario(reservaDto.sala(), reservaDto.data(), reservaDto.hora())) {
            Reserva reserva = reservaRepository.findBySalaAndDataAndHorario(reservaDto.sala(), reservaDto.data(), reservaDto.hora());
            reservaRepository.delete(reserva);
            return ResponseEntity.status(HttpStatus.OK).body("Reserva removida com sucesso");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: Reserva não encontrada");
        }
    }

    @PostMapping("/consultarReserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala não reservada"),
            @ApiResponse(responseCode = "409", description = "Sala ocupada"),
    })
    public ResponseEntity<?> consultarReserva(@RequestBody ReservaDto reservaDto) {
        if(reservaRepository.existsBySalaAndDataAndHorario(reservaDto.sala(), reservaDto.data(), reservaDto.hora())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("Sala ocupada: %s, Data: %s, Hora: %s", reservaDto.sala(), reservaDto.data(), reservaDto.hora()));
        }else {
            return ResponseEntity.status(HttpStatus.OK).body("Sala não reservada");
        }
    }

    @GetMapping("/consultarReservaSalas/{sala}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Nenhum usuario encontrado"),
    })
    public ResponseEntity<List<Usuario>> consultarReservaSalas(@PathVariable String sala) {
        List<Usuario> usuarios = reservaRepository.findUsuariosBySala(sala);
        if(!usuarios.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(usuarios);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usuarios);
        }
    }

    @GetMapping("/consultarReservaUsuario/{usuario}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Nenhuma sala encontrado"),
    })
    public ResponseEntity<List<Sala>> consultarReservaUsuario(@PathVariable String usuario) {
        List<Sala> salas = reservaRepository.findSalasByUsuario(usuario);
        if(!salas.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(salas);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(salas);
        }
    }

    @PostMapping("/cadastrarUsuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario criado com sucesso"),
    })
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody UsuarioDto usuarioDto){
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDto, usuario);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(usuario));
    }

    @PostMapping("/cadastrarSala")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala criada com sucesso"),
    })
    public ResponseEntity<Sala> cadastrarSala(@RequestBody SalaDto salaDto){
        Sala sala = new Sala();
        BeanUtils.copyProperties(salaDto, sala);
        return ResponseEntity.status(HttpStatus.OK).body(salaRepository.save(sala));
    }

}

