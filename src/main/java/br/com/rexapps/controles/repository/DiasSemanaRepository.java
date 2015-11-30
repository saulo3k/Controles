package br.com.rexapps.controles.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rexapps.controles.domain.DiaSemana;

/**
 * Spring Data JPA repository for the Pedido entity.
 */
public interface DiasSemanaRepository extends JpaRepository<DiaSemana,Long> {


}
