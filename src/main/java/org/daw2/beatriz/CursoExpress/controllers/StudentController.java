package org.daw2.beatriz.CursoExpress.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.StudentCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.StudentDTO;
import org.daw2.beatriz.CursoExpress.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @Operation(summary = "Obtener todos los Alumnos", description = "Devuelve una lista de todos los Alumnos " +
            "disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Alumnos recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudentDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<StudentDTO>> getAllStudents(@PageableDefault(size = 5, sort = "name") Pageable pageable) {
        logger.info("Solicitando la lista de todos los Alumnos con paginación: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<StudentDTO> studentDTOs = studentService.getAllStudents(pageable);
            logger.info("Se han encontrado {} Alumnos.", studentDTOs.getTotalElements());
            return ResponseEntity.ok(studentDTOs);
        } catch (Exception e) {
            logger.error("Error al listar los Alumnos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Obtener un Alumno por ID", description = "Recupera un Alumno " +
            "específico según su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alumno encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        logger.info("Insertando nuevo Alumno con id {}", id);
        try {
            Optional<StudentDTO> studentDTO = studentService.getStudentById(id);
            if (studentDTO.isPresent()) {
                logger.info("Alumno con ID {} encontrado", id);
                return ResponseEntity.ok(studentDTO.get());
            } else {
                logger.warn("No se encontró ningún Alumno con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Alumno no existe.");
            }
        } catch (Exception e) {
            logger.error("Error al obtener al Alumno con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el Alumno");
        }
    }

    @Operation(summary = "Crear un nuevo Alumno", description = "Permite registrar un nuevo Alumno en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alumno creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createStudent(@Valid @ModelAttribute StudentCreateDTO studentCreateDTO, Locale locale) {
        logger.info("Insertando nuevo Alumno con nombre {}", studentCreateDTO.getName());
        try {
            StudentDTO createdStudent = studentService.createStudent(studentCreateDTO, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear el Alumno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error al guardar la imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen.");
        } catch (Exception e) {
            logger.error("Error inesperado al crear el Alumno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el Alumno.");
        }
    }

    @Operation(summary = "Actualizar un Alumno", description = "Permite actualizar los datos de un Alumno existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alumno actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @ModelAttribute StudentCreateDTO studentCreateDTO, Locale locale) {
        logger.info("Actualizando Alumno con ID {}", id);
        try {
            StudentDTO updatedStudent = studentService.updateStudent(id, studentCreateDTO, locale);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar el Alumno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error al guardar la imagen para el Alumno con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen.");
        } catch (Exception e) {
            logger.error("No se pudo actualizar el Alumno con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el Alumno.");
        }
    }

    @Operation(summary = "Eliminar un Alumno", description = "Permite eliminar un Alumno específica de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alumno eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        logger.info("Eliminando Alumno con ID {}", id);
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar el Alumno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar el Alumno con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el Alumno.");
        }
    }
}
