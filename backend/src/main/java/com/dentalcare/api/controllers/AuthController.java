package com.dentalcare.api.controllers;

import com.dentalcare.api.dtos.Login.LoginRequestDto;
import com.dentalcare.api.dtos.Login.LoginResponseDto;
import com.dentalcare.api.models.Usuario;
import com.dentalcare.api.repositories.UsuarioRepository;
import com.dentalcare.api.services.AuthService;
import com.dentalcare.api.services.AuditoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para autenticacion de usuarios.
 * Expone un unico endpoint publico: POST /api/auth/login
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    // Inyección de depedencias a traves del constructor PBI Revisar Accesos.
    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;
    // Inyección de AuditoriaService para registrar intentos de login 
    @Autowired
    private AuditoriaService auditoriaService;
    //Constructor para inyección de depedencias 
    public AuthController(AuthService authService, UsuarioRepository usuarioRepository) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Autentica al usuario y retorna un JWT.
     *
     * POST /api/auth/login
     * Body: { "username": "...", "password": "..." }
     *
     * @param request DTO validado por @Valid con las credenciales
     * @return 200 OK con el token, 401 si las credenciales son incorrectas
     */
    // Se agrega la lógica para registrar intentos de acceso exitosos y fallidos en el sistema,
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
          
        // se agrega la logica para buscar el usuario pro username o email PBI Revisar accesos
        Optional<Usuario> oUsuario = usuarioRepository.findByUsernameUsuario(request.getUsername());
        // si no lo encuentra por username, intentar con email
        if (oUsuario.isEmpty()){
            oUsuario = usuarioRepository.findByEmailUsuario(request.getUsername());
        }
          
        try {
            LoginResponseDto response = authService.login(request);

            // =========================================================
            // INTERCEPCIÓN 1: LOGIN EXITOSO (Entra al try sin errores)
            // =========================================================
            if (oUsuario.isPresent()) {
                auditoriaService.registrarIntento(oUsuario.get(), true);
            }

            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {

            // =========================================================
            // INTERCEPCIÓN 2: LOGIN FALLIDO (Lanza RuntimeException)
            // =========================================================
            if (oUsuario.isPresent()) {
                // El usuario existe en la BD pero puso mal la contraseña
                auditoriaService.registrarIntento(oUsuario.get(), false);
            }
            // Si el usuario no existe, no se puede registrar auditoría
            // por la restricción de clave foránea @ManyToOne

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}