package com.universidad.seguridad.config;

import com.universidad.seguridad.service.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * BCryptPasswordEncoder con factor de costo 12.
     * Este bean es inyectado en UsuarioService y en el DaoAuthenticationProvider.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Proveedor de autenticación que combina UserDetailsService + PasswordEncoder.
     * Spring Security lo usa para verificar credenciales en el login.
     */
    @Bean
    public DaoAuthenticationProvider authProvider(
            UsuarioDetailsService uds, PasswordEncoder pe) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(pe);
        return provider;
    }

    /**
     * Cadena de filtros de seguridad: define reglas de autorización,
     * configuración del formulario de login y del logout.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ---- Reglas de autorización ----
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas: login, registro, home y recursos estáticos
                .requestMatchers("/", "/login", "/registro",
                                 "/css/**", "/js/**").permitAll()
                // Rutas de administración: solo rol ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )

            // ---- Formulario de login personalizado ----
            .formLogin(form -> form
                .loginPage("/login")                          // Vista Thymeleaf propia
                .loginProcessingUrl("/login")                 // Spring procesa el POST aquí
                .defaultSuccessUrl("/dashboard", true)        // Redirige al dashboard si login OK
                .failureUrl("/login?error=true")              // Redirige con parámetro de error
                .usernameParameter("username")                // Nombre del campo email en el form
                .passwordParameter("password")
                .permitAll()
            )

            // ---- Logout ----
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)                  // Destruye la sesión HTTP
                .deleteCookies("JSESSIONID")                  // Elimina la cookie de sesión
                .permitAll()
            );

        return http.build();
    }
}
