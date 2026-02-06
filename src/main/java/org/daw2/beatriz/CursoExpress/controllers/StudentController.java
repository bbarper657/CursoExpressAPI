package org.daw2.beatriz.CursoExpress.controllers;

import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.entities.Student;
import org.daw2.beatriz.CursoExpress.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        logger.info("Solicitando la lista de todos los Alumnos...");
        try {
            List<Student> students = studentRepository.findAll();
            logger.info("Se han encontrado {} alumnos.", students.size());
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            logger.error("Error al listar los alumnos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        logger.info("Insertando nuevo alumno con id {}", student.getId());
        if (result.hasErrors()) {
            return "student-form";
        }
        studentRepository.save(student);
        logger.info("Alumnos {} insertados con éxito.", student.getId());
        return "redirect:/students";
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody Student student, Locale locale) {
        logger.info("Actualizando Alumno con ID {}", student.getId());
        if (result.hasErrors()) {
            return "student-form";
        }
        try {
            studentRepository.save(student);
            redirectAttributes.addFlashAttribute("successMessage", "Alumno actualizado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo actualizar el alumno");
        }
        return "redirect:/students";
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student, Locale locale) {
        logger.info("Eliminando Alumno con ID {}", id);
        try {
            studentRepository.deleteById(id);
            logger.info("Alumno con ID {} eliminado con éxito.", id);
            redirectAttributes.addFlashAttribute("successMessage", "Alumno eliminado con éxito");
        } catch (Exception e) {
            logger.error("No se pudo eliminar el Alumno con ID {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar el alumno");
        }
        return "redirect:/students";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id) {
        logger.info("Eliminando Alumno con ID {}", id);
        try {
            studentRepository.deleteById(id);
            logger.info("Alumno con ID {} eliminado con éxito.", id);
            redirectAttributes.addFlashAttribute("successMessage", "Alumno eliminado con éxito");
        } catch (Exception e) {
            logger.error("No se pudo eliminar el Alumno con ID {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar el alumno");
        }
        return "redirect:/students";
    }
}
