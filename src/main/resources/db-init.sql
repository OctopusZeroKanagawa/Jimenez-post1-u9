-- ============================================================
-- Script de inicialización - Unidad 9 Seguridad Web
-- Autor: Andres Felipe Jimenez Ramirez
-- ============================================================

-- Crear base de datos (si no existe del laboratorio anterior)
CREATE DATABASE IF NOT EXISTS estudiantes_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE estudiantes_db;

-- La tabla 'usuarios' es creada automáticamente por Hibernate (ddl-auto=update)
-- Este script inserta el usuario ADMIN una vez que la tabla exista.

-- IMPORTANTE: El hash corresponde a la contraseña "admin123" con BCrypt factor 12.
-- Si ejecutas GenerarHashTest, reemplaza el hash de abajo por el generado.
INSERT INTO usuarios (nombre, email, contrasenia, rol, activo)
VALUES (
    'Administrador',
    'admin@universidad.edu',
    '$2a$12$eImiTXuWVxfM37uY4JANjQ==',   -- REEMPLAZAR con hash generado por GenerarHashTest
    'ROLE_ADMIN',
    1
);

-- Usuario de prueba con rol USER (contraseña: user123)
-- También requiere reemplazar el hash
INSERT INTO usuarios (nombre, email, contrasenia, rol, activo)
VALUES (
    'Usuario Prueba',
    'user@universidad.edu',
    '$2a$12$eImiTXuWVxfM37uY4JANjQ==',   -- REEMPLAZAR con hash generado por GenerarHashTest
    'ROLE_USER',
    1
);

-- Verificar inserción
SELECT id, nombre, email,
       LEFT(contrasenia, 15) AS hash_parcial,
       rol, activo
FROM usuarios;
