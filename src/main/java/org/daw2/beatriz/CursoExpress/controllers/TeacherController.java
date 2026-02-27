package org.daw2.beatriz.CursoExpress.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.TeacherCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.TeacherDTO;
import org.daw2.beatriz.CursoExpress.services.TeacherService;
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
@RequestMapping("/api/teachers")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private TeacherService teacherService;

    @Operation(summary = "Obtener todos los Profesores", description = "Devuelve una lista de todos los Profesores " +
            "disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Profesores recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TeacherDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<TeacherDTO>> getAllTeachers(@PageableDefault(size = 5, sort = "name") Pageable pageable) {
        logger.info("Solicitando la lista de todos los Profesores con paginación: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<TeacherDTO> teacherDTOs = teacherService.getAllTeachers(pageable);
            logger.info("Se han encontrado {} Profesores.", teacherDTOs.getTotalElements());
            return ResponseEntity.ok(teacherDTOs);
        } catch (Exception e) {
            logger.error("Error al listar los Profesores: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Obtener un Profesor por ID", description = "Recupera un Profesor " +
            "específico según su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesor encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDTO.class))),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Crear un nuevo Profesor", description = "Permite registrar un nuevo Profesor en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profesor creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> createTeacher(@Valid @ModelAttribute TeacherCreateDTO teacherCreateDTO, Locale locale) {
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

    @Operation(summary = "Actualizar un Profesor", description = "Permite actualizar los datos de un Profesor existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesor actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long id, @Valid @ModelAttribute TeacherCreateDTO teacherCreateDTO, Locale locale) {
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

    @Operation(summary = "Eliminar un Profesor", description = "Permite eliminar un Profesor específica de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesor eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
