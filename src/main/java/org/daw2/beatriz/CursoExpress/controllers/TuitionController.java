package org.daw2.beatriz.CursoExpress.controllers;

import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.TuitionCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.TuitionDTO;
import org.daw2.beatriz.CursoExpress.services.TuitionService;
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
@RequestMapping("/api/tuitions")
public class TuitionController {
    private static final Logger logger = LoggerFactory.getLogger(TuitionController.class);

    @Autowired
    private TuitionService tuitionService;

    @GetMapping
    public ResponseEntity<List<TuitionDTO>> getAllTuitions() {
        logger.info("Solicitando la lista de todos los Matriculas...");
        try {
            List<TuitionDTO> tuitionDTOs = tuitionService.getAllTuitions();
            logger.info("Se han encontrado {} Matrículas.", tuitionDTOs.size());
            return ResponseEntity.ok(tuitionDTOs);
        } catch (Exception e) {
            logger.error("Error al listar las Matrículas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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

    @PostMapping
    public ResponseEntity<?> createTuition(@Valid @RequestBody TuitionCreateDTO tuitionCreateDTO, Locale locale) {
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTuition(@PathVariable Long id, @Valid @RequestBody TuitionCreateDTO tuitionCreateDTO, Locale locale) {
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
