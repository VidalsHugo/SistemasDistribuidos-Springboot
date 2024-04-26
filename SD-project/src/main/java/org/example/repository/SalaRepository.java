package org.example.repository;

import org.example.dominio.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    @Query("Select u FROM Sala u WHERE u.nome = :name")
    Sala findByName(@Param("name") String name);
}
