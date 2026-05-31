package com.universidad.seguridad.service;

import com.universidad.seguridad.model.Usuario;
import com.universidad.seguridad.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repo;

    public UsuarioDetailsService(UsuarioRepository repo) {
        this.repo = repo;
    }

    /**
     * Spring Security llama este método con el valor del campo "username" del formulario.
     * En este proyecto el campo se llama "email", por eso usamos email como username.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + email));

        return User.builder()
                .username(u.getEmail())
                .password(u.getContrasenia())           // BCrypt hash almacenado en BD
                .roles(u.getRol().replace("ROLE_", "")) // Quita el prefijo: "ADMIN" / "USER"
                .disabled(!u.isActivo())
                .build();
    }
}
