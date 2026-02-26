package org.daw2.beatriz.CursoExpress.controllers;

import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.TeacherCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.TeacherDTO;
import org.daw2.beatriz.CursoExpress.services.TeacherService;
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
@RequestMapping("/api/teachers")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        logger.info("Solicitando la lista de todos los Profesores...");
        try {
            List<TeacherDTO> teacherDTOs = teacherService.getAllTeachers();
            logger.info("Se han encontrado {} Profesores.", teacherDTOs.size());
            return ResponseEntity.ok(teacherDTOs);
        } catch (Exception e) {
            logger.error("Error al listar los Profesores: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        logger.info("Insertando nuevo Profesor con id {}", id);
        try {
            Optional<TeacherDTO> teacherDTO = teacherService.getTeacherById(id);
            if (teacherDTO.isPresent()) {
                logger.info("Profesor con ID {} encontrado", id);
                return ResponseEntity.ok(teacherDTO.get());
            } else {
                logger.warn("No se encontró ningún Profesor con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Profesor no existe.");
            }
        } catch (Exception e) {
            logger.error("Error al obtener al Profesor con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el Profesor");
        }
    }

    @PostMapping
    public ResponseEntity<?> createTeacher(@Valid @RequestBody TeacherCreateDTO teacherCreateDTO, Locale locale) {
        logger.info("Insertando nuevo Profesor con nombre {}", teacherCreateDTO.getName());
        try {
            TeacherDTO createdTeacher = teacherService.createTeacher(teacherCreateDTO, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear el Profesor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear el Profesor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el Profesor.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherCreateDTO teacherCreateDTO, Locale locale) {
        logger.info("Actualizando Profesor con ID {}", id);
        try {
            TeacherDTO updatedTeacher = teacherService.updateTeacher(id, teacherCreateDTO, locale);
            return ResponseEntity.ok(updatedTeacher);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar el Profesor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("No se pudo actualizar el Profesor con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el Profesor.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        logger.info("Eliminando Profesor con ID {}", id);
        try {
            teacherService.deleteTeacher(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar el Profesor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar el Profesor con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el Profesor.");
        }
    }
}
