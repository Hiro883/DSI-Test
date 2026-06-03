//Se crea este servicio para implementar la funcionalidad del pbi revisar accesos que 
// permite a los administradores revisar los intentos de acceso al sistema
package com.dentalcare.api.services;
import com.dentalcare.api.models.RegistroAcceso;
import com.dentalcare.api.models.Usuario;
import com.dentalcare.api.repositories.RegistroAccesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaService {

    // Inyección del repo para acceder a la bd de registros de acceso 
    @Autowired
    private RegistroAccesoRepository repo;

  
    @Transactional(readOnly = true)
    public List<RegistroAcceso> obtenerAuditoriaFiltrada(String username, LocalDateTime inicio, LocalDateTime fin) {
        // Si las cadenas de texto vienen vacías de los inputs HTML, las tratamos como null para la Query
        String userParam = (username != null && !username.trim().isEmpty()) ? username.trim() : null;
        return repo.filtrarAuditoria(userParam, inicio, fin);
    }

    @Transactional
    public void registrarIntento(Usuario usuario, boolean exitoso) {
        RegistroAcceso nuevoRegistro = new RegistroAcceso(LocalDateTime.now(), exitoso, usuario);
        repo.save(nuevoRegistro);
    }
}