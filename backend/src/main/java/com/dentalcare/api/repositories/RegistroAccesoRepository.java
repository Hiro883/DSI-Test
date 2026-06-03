//Se crea este repositorio para implementar la funcionalidad del pbi revisar accesos, 
// que permite a los administradores revisar los intentos de acceso al sistema
package com.dentalcare.api.repositories;

import com.dentalcare.api.models.RegistroAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// Con esta interfaz, spring jpa se encargará de generar automáticamente las consultas SQL necesarias para realizar operaciones CRUD sobre la tabla "registro_acceso" y también la consulta personalizada definida en el método filtrarAuditoria. Esto nos permite interactuar con la base de datos de manera sencilla y eficiente sin tener que escribir código SQL manualmente.

@Repository
public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso, Integer> {
    
    @Query("SELECT r FROM RegistroAcceso r WHERE " +
           "(:username IS NULL OR r.usuario.usernameUsuario LIKE %:username%) AND " +
           "(:fechaInicio IS NULL OR r.fechaAcceso >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR r.fechaAcceso <= :fechaFin) " +
           "ORDER BY r.fechaAcceso DESC")
    List<RegistroAcceso> filtrarAuditoria(
        @Param("username") String username, 
        @Param("fechaInicio") LocalDateTime fechaInicio, 
        @Param("fechaFin") LocalDateTime fechaFin
    );
}