/*Se crea este archivo para implementar la funcionalidad del pbi Revisar accesos, que permite a 
a los administradores revisar los intentos de acceso al sistema */
package com.dentalcare.api.controllers;

import com.dentalcare.api.models.RegistroAcceso;
import com.dentalcare.api.services.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/seguridad/accesos")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    public String verPanelAccesos(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            Model model) {
        
        List<RegistroAcceso> registros = auditoriaService.obtenerAuditoriaFiltrada(username, inicio, fin);
        
        model.addAttribute("listaAccesos", registros);
        model.addAttribute("usernameAnterior", username);
        model.addAttribute("inicioAnterior", inicio);
        model.addAttribute("finAnterior", fin);
        
        return "revisar-accesos"; // Apunta a src/main/resources/templates/revisar-accesos.html 
    }
}