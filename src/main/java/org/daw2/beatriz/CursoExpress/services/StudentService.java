package org.daw2.beatriz.CursoExpress.services;

import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.StudentCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.StudentDTO;
import org.daw2.beatriz.CursoExpress.entities.Student;
import org.daw2.beatriz.CursoExpress.mappers.StudentMapper;
import org.daw2.beatriz.CursoExpress.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MessageSource messageSource;

    public List<StudentDTO> getAllStudents() {
        try {
            logger.info("Obteniendo todos los alumnos...");
            List<Student> students = studentRepository.findAll();
            logger.info("Se encontraron {} alumnos", students.size());
            return students.stream()
                    .map(studentMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            logger.error("Error al obtener todos los alumnos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todos los alumnos", e);
        }
    }

    public Optional<StudentDTO> getStudentById(Long id) {
        try {
            logger.info("Obteniendo alumno con ID {}", id);
            return studentRepository.findById(id).map(studentMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al buscar alumno con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar el alumno.", e);
        }
    }

    public StudentDTO createStudent(StudentCreateDTO studentCreateDTO, Locale locale) {
        if (studentRepository.existsByEmail(studentCreateDTO.getEmail())) {
            String errorMessage = messageSource.getMessage("Error al crear el alumno.", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        if (studentRepository.existsByEmail(studentCreateDTO.getEmail())) {
            throw new IllegalArgumentException("El correo de ese alumno existe");
        }

        // Se convierte a Entity para almacenar en la base de datos
        Student student = studentMapper.toEntity(studentCreateDTO);
        Student savedStudent = studentRepository.save(student);
        // Se devuelve el DTO
        return studentMapper.toDTO(savedStudent);
    }

    public StudentDTO updateStudent(Long id, @Valid StudentCreateDTO studentCreateDTO, Locale locale) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El alumno no existe"));

        if (studentRepository.existsByEmailAndIdNot(studentCreateDTO.getEmail(), id)) {
            throw new IllegalArgumentException("El email ya está en uso por otro alumno.");
        }

        existingStudent.setEmail(studentCreateDTO.getEmail());
        existingStudent.setName(studentCreateDTO.getName());
        existingStudent.setPhone(studentCreateDTO.getPhone());
        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.toDTO(updatedStudent);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("El alumno no existe");
        }
        studentRepository.deleteById(id);
    }
}
