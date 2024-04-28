package org.example.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.dominio.Sala;
import org.example.dominio.Usuario;
import org.example.dto.SalaDto;
import org.example.dto.UsuarioDto;
import org.example.repository.SalaRepository;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CadastroController {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    SalaRepository salaRepository;

    @PostMapping("/cadastrarUsuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario criado com sucesso"),
    })
    public ResponseEntity<Usuario> cadastrarUsuario(@Valid @RequestBody UsuarioDto usuarioDto){
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDto, usuario);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(usuario));
    }

    @PostMapping("/cadastrarSala")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala criada com sucesso"),
    })
    public ResponseEntity<Sala> cadastrarSala(@Valid @RequestBody SalaDto salaDto){
        Sala sala = new Sala();
        BeanUtils.copyProperties(salaDto, sala);
        return ResponseEntity.status(HttpStatus.OK).body(salaRepository.save(sala));
    }
}
