package org.daw2.beatriz.CursoExpress.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.ModuleCourseCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.ModuleCourseDTO;
import org.daw2.beatriz.CursoExpress.services.ModuleCourseService;
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
@RequestMapping("/api/modulesCourses")
public class ModuleCourseController {
    private static final Logger logger = LoggerFactory.getLogger(ModuleCourseController.class);

    @Autowired
    private ModuleCourseService moduleCourseService;

    @Operation(summary = "Obtener todos los Módulos", description = "Devuelve una lista de todos los Módulos " +
            "disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Módulos recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ModuleCourseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<ModuleCourseDTO>> getAllModulesCourses(@PageableDefault(size = 5, sort = "name") Pageable pageable) {
        logger.info("Solicitando la lista de todos los Módulos con paginación: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<ModuleCourseDTO> moduleCourseDTOs = moduleCourseService.getAllModulesCourses(pageable);
            logger.info("Se han encontrado {} Módulos.", moduleCourseDTOs.getTotalElements());
            return ResponseEntity.ok(moduleCourseDTOs);
        } catch (Exception e) {
            logger.error("Error al listar los Módulos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Obtener un Módulo por ID", description = "Recupera una Módulo " +
            "específico según su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Módulo encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModuleCourseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Módulo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Crear un nuevo Módulo", description = "Permite registrar un nuevo Módulo en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Módulo creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModuleCourseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> createModuleCourse(@Valid @ModelAttribute ModuleCourseCreateDTO moduleCourseCreateDTO, Locale locale) {
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

    @Operation(summary = "Actualizar un Módulo", description = "Permite actualizar los datos de un Módulo existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Módulo actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModuleCourseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateModuleCourse(@PathVariable Long id, @Valid @ModelAttribute ModuleCourseCreateDTO moduleCourseCreateDTO, Locale locale) {
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

    @Operation(summary = "Eliminar un Módulo", description = "Permite eliminar un Módulo específica de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Módulo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Módulo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
