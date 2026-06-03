//Se creo este modelo para implementar la funcionalidad del pbi revisar accesos, que permite a los administradores revisar los intentos de acceso al sistema

package com.dentalcare.api.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_acceso")
public class RegistroAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro_acceso")
    private Integer idRegistroAcceso;

    @Column(name = "fecha_acceso", nullable = false)
    private LocalDateTime fechaAcceso;

    @Column(name = "es_exitoso", nullable = false)
    private boolean esExitoso;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Constructor Vacío (Requerido por JPA)
    public RegistroAcceso() {
    }

    // Constructor Confortable para registrar logs rápidamente
    public RegistroAcceso(LocalDateTime fechaAcceso, boolean esExitoso, Usuario usuario) {
        this.fechaAcceso = fechaAcceso;
        this.esExitoso = esExitoso;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Integer getIdRegistroAcceso() {
        return idRegistroAcceso;
    }

    public void setIdRegistroAcceso(Integer idRegistroAcceso) {
        this.idRegistroAcceso = idRegistroAcceso;
    }

    public LocalDateTime getFechaAcceso() {
        return fechaAcceso;
    }

    public void setFechaAcceso(LocalDateTime fechaAcceso) {
        this.fechaAcceso = fechaAcceso;
    }

    public boolean isEsExitoso() {
        return esExitoso;
    }

    public void setEsExitoso(boolean esExitoso) {
        this.esExitoso = esExitoso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}