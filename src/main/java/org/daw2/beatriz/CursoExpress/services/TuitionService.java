package org.daw2.beatriz.CursoExpress.services;

import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.TuitionCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.TuitionDTO;
import org.daw2.beatriz.CursoExpress.entities.Course;
import org.daw2.beatriz.CursoExpress.entities.Student;
import org.daw2.beatriz.CursoExpress.entities.Tuition;
import org.daw2.beatriz.CursoExpress.mappers.TuitionMapper;
import org.daw2.beatriz.CursoExpress.repositories.CourseRepository;
import org.daw2.beatriz.CursoExpress.repositories.StudentRepository;
import org.daw2.beatriz.CursoExpress.repositories.TuitionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TuitionService {
    private static final Logger logger = LoggerFactory.getLogger(TuitionService.class);

    @Autowired
    private TuitionRepository tuitionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TuitionMapper tuitionMapper;

    @Autowired
    private MessageSource messageSource;

    public List<TuitionDTO> getAllTuitions() {
        try {
            logger.info("Obteniendo todas las matrículas...");
            List<Tuition> tuitions = tuitionRepository.findAll();
            logger.info("Se encontraron {} matrículas", tuitions.size());
            return tuitions.stream()
                    .map(tuitionMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            logger.error("Error al obtener todas las matrículas: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todas las matrículas", e);
        }
    }

    public Optional<TuitionDTO> getTuitionById(Long id) {
        try {
            logger.info("Obteniendo matrícula con ID {}", id);
            return tuitionRepository.findById(id).map(tuitionMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al buscar matrícula con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar la matrícula.", e);
        }
    }

    public TuitionDTO createTuition(TuitionCreateDTO tuitionCreateDTO, Locale locale) {
        if (tuitionRepository.existsTuitionByCode(tuitionCreateDTO.getCode())) {
            String errorMessage = messageSource.getMessage("Error al crear la matrícula.", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        if (tuitionRepository.existsTuitionByCode(tuitionCreateDTO.getCode())) {
            throw new IllegalArgumentException("El código de esa matrícula existe");
        }

        // Se convierte a Entity para almacenar en la base de datos
        Tuition tuition = tuitionMapper.toEntity(tuitionCreateDTO);
        Tuition savedTuition = tuitionRepository.save(tuition);
        // Se devuelve el DTO
        return tuitionMapper.toDTO(savedTuition);
    }

    public TuitionDTO updateTuition(Long id, @Valid TuitionCreateDTO tuitionCreateDTO, Locale locale) {
        Tuition existingTuition = tuitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La matrícula no existe"));

        if (tuitionRepository.existsByCodeAndIdNot(tuitionCreateDTO.getCode(), id)) {
            throw new IllegalArgumentException("El código ya está en uso por otro profesor.");
        }

        existingTuition.setCode(tuitionCreateDTO.getCode());
        existingTuition.setDate(tuitionCreateDTO.getDate());
        existingTuition.setObservation(tuitionCreateDTO.getObservation());

        if (tuitionCreateDTO.getStudentId() != null) {
            Student student = studentRepository.findById(tuitionCreateDTO.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("El alumno no existe"));
            existingTuition.setStudent(student);
        }

        if (tuitionCreateDTO.getCourseId() != null) {
            Course course = courseRepository.findById(tuitionCreateDTO.getCourseId())
                    .orElseThrow(() -> new IllegalArgumentException("El curso no existe"));
            existingTuition.setCourse(course);
        }

        Tuition updatedTuition = tuitionRepository.save(existingTuition);
        return tuitionMapper.toDTO(updatedTuition);
    }

    public void deleteTuition(Long id) {
        if (!tuitionRepository.existsById(id)) {
            throw new IllegalArgumentException("La matrícula no existe");
        }
        tuitionRepository.deleteById(id);
    }
}
