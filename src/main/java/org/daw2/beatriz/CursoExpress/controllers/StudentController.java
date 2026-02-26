package org.daw2.beatriz.CursoExpress.controllers;

import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.StudentCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.StudentDTO;
import org.daw2.beatriz.CursoExpress.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        logger.info("Solicitando la lista de todos los Alumnos...");
        try {
            List<StudentDTO> studentDTOs = studentService.getAllStudents();
            logger.info("Se han encontrado {} Alumnos.", studentDTOs.size());
            return ResponseEntity.ok(studentDTOs);
        } catch (Exception e) {
            logger.error("Error al listar los Alumnos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentCreateDTO studentCreateDTO, Locale locale) {
        logger.info("Insertando nuevo Alumno con nombre {}", studentCreateDTO.getName());
        try {
            StudentDTO createdStudent = studentService.createStudent(studentCreateDTO, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear el Alumno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear el Alumno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el Alumno.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentCreateDTO studentCreateDTO, Locale locale) {
        logger.info("Actualizando Alumno con ID {}", id);
        try {
            StudentDTO updatedStudent = studentService.updateStudent(id, studentCreateDTO, locale);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar el Alumno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("No se pudo actualizar el Alumno con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el Alumno.");
        }
    }

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
