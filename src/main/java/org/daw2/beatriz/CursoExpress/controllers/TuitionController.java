package org.daw2.beatriz.CursoExpress.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.TuitionCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.TuitionDTO;
import org.daw2.beatriz.CursoExpress.services.TuitionService;
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
@RequestMapping("/api/tuitions")
public class TuitionController {
    private static final Logger logger = LoggerFactory.getLogger(TuitionController.class);

    @Autowired
    private TuitionService tuitionService;

    @Operation(summary = "Obtener todas las Matrículas", description = "Devuelve una lista de todas las Matrículas " +
            "disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Matrículas recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TuitionDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<TuitionDTO>> getAllTuitions(@PageableDefault(size = 5, sort = "name") Pageable pageable) {
        logger.info("Solicitando la lista de todos los Matriculas con paginación: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<TuitionDTO> tuitionDTOs = tuitionService.getAllTuitions(pageable);
            logger.info("Se han encontrado {} Matrículas.", tuitionDTOs.getTotalElements());
            return ResponseEntity.ok(tuitionDTOs);
        } catch (Exception e) {
            logger.error("Error al listar las Matrículas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Obtener una Matrícula por ID", description = "Recupera una Matrícula " +
            "específica según su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matrícula encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TuitionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Matrícula no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getTuitionById(@PathVariable Long id) {
        logger.info("Insertando nueva Matrícula con id {}", id);
        try {
            Optional<TuitionDTO> tuitionDTO = tuitionService.getTuitionById(id);
            if (tuitionDTO.isPresent()) {
                logger.info("Matrícula con ID {} encontrada", id);
                return ResponseEntity.ok(tuitionDTO.get());
            } else {
                logger.warn("No se encontró ninguna Matrícula con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La Matrícula no existe.");
            }
        } catch (Exception e) {
            logger.error("Error al obtener la Matrícula con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la Matrícula.");
        }
    }

    @Operation(summary = "Crear una nueva Matrícula", description = "Permite registrar una nueva Matrícula en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Matrícula creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TuitionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> createTuition(@Valid @ModelAttribute TuitionCreateDTO tuitionCreateDTO, Locale locale) {
        logger.info("Insertando nueva Matrícula con código {}", tuitionCreateDTO.getCode());
        try {
            TuitionDTO createdTuition = tuitionService.createTuition(tuitionCreateDTO, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTuition);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear la Matrícula: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear la Matrícula: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la Matrícula.");
        }
    }

    @Operation(summary = "Actualizar una Matrícula", description = "Permite actualizar los datos de una Matrícula existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matrícula actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TuitionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTuition(@PathVariable Long id, @Valid @ModelAttribute TuitionCreateDTO tuitionCreateDTO, Locale locale) {
        logger.info("Actualizando Matrícula con ID {}", id);
        try {
            TuitionDTO updatedTuition = tuitionService.updateTuition(id, tuitionCreateDTO, locale);
            return ResponseEntity.ok(updatedTuition);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar la Matrícula: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("No se pudo actualizar la Matrícula con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la Matrícula.");
        }
    }

    @Operation(summary = "Eliminar una Matrícula", description = "Permite eliminar una Matrícula específica de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matrícula eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Matrícula no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTuition(@PathVariable Long id) {
        logger.info("Eliminando Matrícula con ID {}", id);
        try {
            tuitionService.deleteTuition(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar la Matrícula: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar la Matrícula con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la Matrícula.");
        }
    }
}
