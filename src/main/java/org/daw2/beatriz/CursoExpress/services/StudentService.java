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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    @Autowired
    private FileStorageService fileStorageService;

    public Page<StudentDTO> getAllStudents(Pageable pageable) {
        logger.info("Solicitando todos los Alumnos con paginación: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Student> students = studentRepository.findAll(pageable);
            logger.info("Se encontraron {} alumnos en la página actual.", students.getNumberOfElements());
            return students.map(studentMapper::toDTO);
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
        Student student = studentMapper.toEntity(studentCreateDTO);

        if (studentCreateDTO.getImage() != null && !studentCreateDTO.getImage().isEmpty()) {
            String fileName = fileStorageService.saveFile(studentCreateDTO.getImage());
            if (fileName != null) {
                student.setImage(fileName);
            }
        } else {
            logger.warn("No se recibió ninguna imagen");
        }
        Student savedStudent = studentRepository.save(student);
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

        if (studentCreateDTO.getImage() != null && !studentCreateDTO.getImage().isEmpty()) {
            if (existingStudent.getImage() != null) {
                fileStorageService.deleteFile(existingStudent.getImage());
            }

            String fileName = fileStorageService.saveFile(studentCreateDTO.getImage());

            if (fileName != null) {
                existingStudent.setImage(fileName);
            }
        }

        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.toDTO(updatedStudent);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("El Alumno no existe");
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El Alumno no existe"));

        if (student.getImage() != null && !student.getImage().isEmpty()) {
            fileStorageService.deleteFile(student.getImage());
            logger.info("Imagen asociada al Alumno con ID {} eliminada.", id);
        }

        studentRepository.deleteById(id);
        logger.info("Alumno con ID {} eliminado exitosamente.", id);
    }
}
