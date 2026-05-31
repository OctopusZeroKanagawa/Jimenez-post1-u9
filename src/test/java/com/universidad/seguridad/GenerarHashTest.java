package com.universidad.seguridad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase temporal para generar el hash BCrypt del usuario ADMIN.
 * Ejecutar UNA SOLA VEZ, copiar el hash impreso en consola
 * y usarlo en el INSERT de MySQL (ver README).
 *
 * Para ejecutar solo este test:
 *   mvn test -Dtest=GenerarHashTest
 */
@SpringBootTest
class GenerarHashTest {

    @Autowired
    private PasswordEncoder encoder;

    @Test
    void generarHashAdmin() {
        String hash = encoder.encode("admin123");
        System.out.println("============================================");
        System.out.println("Hash BCrypt para 'admin123':");
        System.out.println(hash);
        System.out.println("============================================");
        System.out.println("Usa este valor en el INSERT de MySQL.");
    }

    @Test
    void generarHashUsuarioPrueba() {
        String hash = encoder.encode("user123");
        System.out.println("============================================");
        System.out.println("Hash BCrypt para 'user123':");
        System.out.println(hash);
        System.out.println("============================================");
    }
}
