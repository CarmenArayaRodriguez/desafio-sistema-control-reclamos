package cl.praxis.controlreclamos_transporte.repositories;

import cl.praxis.controlreclamos_transporte.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}
