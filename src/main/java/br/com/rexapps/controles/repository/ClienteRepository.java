package br.com.rexapps.controles.repository;

import br.com.rexapps.controles.domain.Cliente;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cliente entity.
 */
public interface ClienteRepository extends JpaRepository<Cliente,Long> {

}
