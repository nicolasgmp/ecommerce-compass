package br.com.nicolas.ecommerce_compass.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.nicolas.ecommerce_compass.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
