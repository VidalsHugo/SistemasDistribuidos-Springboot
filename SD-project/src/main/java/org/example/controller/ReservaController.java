package org.example.controller;
import java.util.List;

import org.example.dto.SalaDto;
import org.example.dto.UsuarioDto;
import org.example.repository.SalaRepository;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> cadastrarReserva(@RequestBody ReservaDto reservaDto) {

        Usuario usuario = usuarioRepository.findByName(reservaDto.usuario());
        Sala sala = salaRepository.findByName(reservaDto.sala());

        if(usuario == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Usuario não encontrado.");
        }
        if(sala == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Sala não encontrada.");
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
    public ResponseEntity<?> removerReserva(@RequestBody ReservaDto reservaDto) {
        if(reservaRepository.existsBySalaAndDataAndHorario(reservaDto.sala(), reservaDto.data(), reservaDto.hora())) {
            Reserva reserva = reservaRepository.findBySalaAndDataAndHorario(reservaDto.sala(), reservaDto.data(), reservaDto.hora());
            reservaRepository.delete(reserva);
            return ResponseEntity.status(HttpStatus.OK).body("Reserva removida com sucesso");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Reserva não encontrada");
        }
    }

    @GetMapping("/consultarReserva")
    public ResponseEntity<?> consultarReserva(@RequestBody ReservaDto reservaDto) {
        if(reservaRepository.existsBySalaAndDataAndHorario(reservaDto.sala(), reservaDto.data(), reservaDto.hora())) {
            return ResponseEntity.status(HttpStatus.OK).body("Sala reservada");
        }else {
            return ResponseEntity.status(HttpStatus.OK).body("Sala não reservada");
        }
    }

    @GetMapping("/consultarReservaSalas")
    public ResponseEntity<List<Usuario>> consultarReservaSalas(@RequestParam String sala) {
        return ResponseEntity.status(HttpStatus.OK).body(reservaRepository.findUsuariosBySala(sala));
    }

    @GetMapping("/consultarReservaUsuario")
    public ResponseEntity<List<Sala>> consultarReservaUsuario(@RequestParam String usuario) {
        return ResponseEntity.status(HttpStatus.OK).body(reservaRepository.findSalasByUsuario(usuario));
    }

    @PostMapping("/cadastrarUsuario")
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody UsuarioDto usuarioDto){
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDto, usuario);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(usuario));
    }

    @PostMapping("/cadastrarSala")
    public ResponseEntity<Sala> cadastrarSala(@RequestBody SalaDto salaDto){
        Sala sala = new Sala();
        BeanUtils.copyProperties(salaDto, sala);
        return ResponseEntity.status(HttpStatus.OK).body(salaRepository.save(sala));
    }

}

