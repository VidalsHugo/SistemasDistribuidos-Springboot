package org.example.repository;

import org.example.dominio.Reserva;
import org.example.dominio.Sala;
import org.example.dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    //TODO: implementar SQL QUERY para realizar a lógica de busca
    @Query("SELECT r FROM Reserva r WHERE r.usuario = :usuario")
    List<Reserva> findUsuarioByReserva(@Param("usuario") Usuario usuario);

    @Query("SELECT r FROM Reserva r WHERE r.sala = :sala")
    List<Reserva> findBySala(@Param("sala") Sala sala);

    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.sala.nome = :nomeSala AND r.data = :data AND r.hora = :hora")
    boolean existsBySalaAndDataAndHorario(@Param("nomeSala") String nomeSala, @Param("data") LocalDate data, @Param("hora") String hora);

    @Query("SELECT r FROM Reserva r WHERE r.sala.nome = :sala AND r.data = :data AND r.hora = :hora")
    Reserva findBySalaAndDataAndHorario(@Param("sala") String sala, @Param("data") LocalDate data, @Param("hora") String hora);

    @Query("SELECT r.usuario FROM Reserva r WHERE r.sala.nome = :nomeSala")
    List<Usuario> findUsuariosBySala(@Param("nomeSala") String nomeSala);

    @Query("SELECT r.sala FROM Reserva r WHERE r.usuario.nome = :nomeUsuario")
    List<Sala> findSalasByUsuario(@Param("nomeUsuario") String nomeUsuario);

    @Query(value = "DELETE FROM Reserva r WHERE r.sala.nome = :sala AND r.data = :data AND r.hora = :hora", nativeQuery = true)
    void deleteReservaBySalaAndDataAndHora(@Param("sala") String sala, @Param("data") LocalDate data, @Param("hora") String hora);
}