package org.daw2.beatriz.CursoExpress.controllers;


import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.CourseCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.CourseDTO;
import org.daw2.beatriz.CursoExpress.dtos.ModuleCourseCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.ModuleCourseDTO;
import org.daw2.beatriz.CursoExpress.services.ModuleCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/api/modulesCourses")
public class ModuleCourseController {
    private static final Logger logger = LoggerFactory.getLogger(ModuleCourseController.class);

    @Autowired
    private ModuleCourseService moduleCourseService;

    @GetMapping
    public ResponseEntity<List<ModuleCourseDTO>> getAllModulesCourses() {
        logger.info("Solicitando la lista de todos los Módulos...");
        try {
            List<ModuleCourseDTO> moduleCourseDTOs = moduleCourseService.getAllModulesCourses();
            logger.info("Se han encontrado {} Módulos.", moduleCourseDTOs.size());
            return ResponseEntity.ok(moduleCourseDTOs);
        } catch (Exception e) {
            logger.error("Error al listar los Módulos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getModuleCourseById(@PathVariable Long id) {
        logger.info("Insertando nuevo Módulo con id {}", id);
        try {
            Optional<ModuleCourseDTO> moduleCourseDTO = moduleCourseService.getModuleCourseById(id);
            if (moduleCourseDTO.isPresent()) {
                logger.info("Módulo con ID {} encontrado", id);
                return ResponseEntity.ok(moduleCourseDTO.get());
            } else {
                logger.warn("No se encontró ningún Módulo con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Módulo no existe.");
            }
        } catch (Exception e) {
            logger.error("Error al obtener al Módulo con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el Módulo");
        }
    }

    @PostMapping
    public ResponseEntity<?> createModuleCourse(@Valid @RequestBody ModuleCourseCreateDTO moduleCourseCreateDTO, Locale locale) {
        logger.info("Insertando nuevo Módulo con nombre {}", moduleCourseCreateDTO.getName());
        try {
            ModuleCourseDTO createdModuleCourse = moduleCourseService.createModuleCourse(moduleCourseCreateDTO, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdModuleCourse);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear el Módulo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear el Módulo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el Módulo.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateModuleCourse(@PathVariable Long id, @Valid @RequestBody ModuleCourseCreateDTO moduleCourseCreateDTO, Locale locale) {
        logger.info("Actualizando Módulo con ID {}", id);
        try {
            ModuleCourseDTO updatedModuleCourse = moduleCourseService.updateModuleCourse(id, moduleCourseCreateDTO, locale);
            return ResponseEntity.ok(updatedModuleCourse);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar el Módulo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("No se pudo actualizar el Módulo con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el Módulo.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModuleCourse(@PathVariable Long id) {
        logger.info("Eliminando Módulo con ID {}", id);
        try {
            moduleCourseService.deleteModuleCourse(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar el Módulo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar el Módulo con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el Módulo.");
        }
    }
}
