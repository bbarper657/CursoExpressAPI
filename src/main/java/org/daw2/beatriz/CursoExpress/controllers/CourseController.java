package org.daw2.beatriz.CursoExpress.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.CourseCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.CourseDTO;
import org.daw2.beatriz.CursoExpress.services.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/api/courses")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Operation(summary = "Obtener todos los Cursos", description = "Devuelve una lista de todos los Cursos " +
            "disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Cursos recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<CourseDTO>> getAllCourses(@PageableDefault(size = 5, sort = "name") Pageable pageable) {
        logger.info("Solicitando la lista de todos los Cursos con paginación: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<CourseDTO> courseDTOs = courseService.getAllCourses(pageable);
            logger.info("Se han encontrado {} Cursos.", courseDTOs.getTotalElements());
            return ResponseEntity.ok(courseDTOs);
        } catch (Exception e) {
            logger.error("Error al listar los Cursos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Obtener un Curso por ID", description = "Recupera un Curso " +
            "específico según su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Crear un nuevo Curso", description = "Permite registrar un nuevo Curso en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @ModelAttribute CourseCreateDTO courseCreateDTO, Locale locale) {
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

    @Operation(summary = "Actualizar un Curso", description = "Permite actualizar los datos de un Curso existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @Valid @ModelAttribute CourseCreateDTO courseCreateDTO, Locale locale) {
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

    @Operation(summary = "Eliminar un Curso", description = "Permite eliminar un Curso específica de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
