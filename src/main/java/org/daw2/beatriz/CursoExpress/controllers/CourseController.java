package org.daw2.beatriz.CursoExpress.controllers;

import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.CourseCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.CourseDTO;
import org.daw2.beatriz.CursoExpress.services.CourseService;
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
@RequestMapping("/api/courses")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        logger.info("Solicitando la lista de todos los Cursos...");
        try {
            List<CourseDTO> courseDTOs = courseService.getAllCourses();
            logger.info("Se han encontrado {} Cursos.", courseDTOs.size());
            return ResponseEntity.ok(courseDTOs);
        } catch (Exception e) {
            logger.error("Error al listar los Cursos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        logger.info("Insertando nuevo Curso con id {}", id);
        try {
            Optional<CourseDTO> courseDTO = courseService.getCourseById(id);
            if (courseDTO.isPresent()) {
                logger.info("Curso con ID {} encontrado", id);
                return ResponseEntity.ok(courseDTO.get());
            } else {
                logger.warn("No se encontró ningún Curso con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Curso no existe.");
            }
        } catch (Exception e) {
            logger.error("Error al obtener al Curso con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el Curso");
        }
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody CourseCreateDTO courseCreateDTO, Locale locale) {
        logger.info("Insertando nuevo Curso con nombre {}", courseCreateDTO.getName());
        try {
            CourseDTO createdCourse = courseService.createCourse(courseCreateDTO, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear el Curso: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear el Curso: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el Curso.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseCreateDTO courseCreateDTO, Locale locale) {
        logger.info("Actualizando Curso con ID {}", id);
        try {
            CourseDTO updatedCourse = courseService.updateCourse(id, courseCreateDTO, locale);
            return ResponseEntity.ok(updatedCourse);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar el Curso: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("No se pudo actualizar el Curso con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el Curso.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        logger.info("Eliminando Curso con ID {}", id);
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar el Curso: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar el Curso con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el Curso.");
        }
    }
}
