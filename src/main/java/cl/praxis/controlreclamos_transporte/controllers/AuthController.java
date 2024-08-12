package cl.praxis.controlreclamos_transporte.controllers;

import cl.praxis.controlreclamos_transporte.dto.UsuarioDTO;
import cl.praxis.controlreclamos_transporte.models.Rol;
import cl.praxis.controlreclamos_transporte.repositories.RolRepository;
import cl.praxis.controlreclamos_transporte.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import cl.praxis.controlreclamos_transporte.models.Usuario;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, RolRepository rolRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String iniciarSesion() {
        return "login"; // Nombre del template de Thymeleaf para la página de inicio de sesión
    }

    @GetMapping("/registro")
    public String formularioRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "registro";  // Confirma que existe una vista 'registro.html'
    }

    private Set<Rol> convertirRolesDTO(List<String> rolesDTO) {
        return rolesDTO.stream()
                .map(rol -> {
                    Rol foundRol = rolRepository.findByNombre(rol);
                    if (foundRol == null) {
                        throw new IllegalArgumentException("Rol no encontrado: " + rol);
                    }
                    return foundRol;
                })
                .collect(Collectors.toSet());
    }

@Transactional
@PostMapping("/registro")
public String registrarUsuario(@ModelAttribute("usuario") UsuarioDTO usuarioDTO, BindingResult result) {
    if (result.hasErrors()) {
        return "registro";
    }

    log.debug("Creando usuario con username: {}", usuarioDTO.getUsername());
    Usuario nuevoUsuario = new Usuario();
    nuevoUsuario.setUsername(usuarioDTO.getUsername());
    nuevoUsuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
    nuevoUsuario.setActive(true);

    Set<Rol> roles = convertirRolesDTO(usuarioDTO.getRoles());
    nuevoUsuario.setRoles(roles);

    usuarioService.save(nuevoUsuario);
    log.debug("Usuario guardado con éxito");

    return "redirect:/auth/login";
}

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/auth/login?logout";
    }
}
