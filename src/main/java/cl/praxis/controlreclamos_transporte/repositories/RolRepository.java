package cl.praxis.controlreclamos_transporte.repositories;

import cl.praxis.controlreclamos_transporte.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);
}
