package org.daw2.beatriz.CursoExpress.services;

import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.CourseCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.CourseDTO;
import org.daw2.beatriz.CursoExpress.entities.Course;
import org.daw2.beatriz.CursoExpress.entities.Teacher;
import org.daw2.beatriz.CursoExpress.mappers.CourseMapper;
import org.daw2.beatriz.CursoExpress.repositories.CourseRepository;
import org.daw2.beatriz.CursoExpress.repositories.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private MessageSource messageSource;

    public List<CourseDTO> getAllCourses() {
        try {
            logger.info("Obteniendo todos los cursos...");
            List<Course> courses = courseRepository.findAll();
            logger.info("Se encontraron {} cursos", courses.size());
            return courses.stream()
                    .map(courseMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            logger.error("Error al obtener todos los cursos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todos los cursos", e);
        }
    }

    public Optional<CourseDTO> getCourseById(Long id) {
        try {
            logger.info("Obteniendo curso con ID {}", id);
            return courseRepository.findById(id).map(courseMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al buscar curso con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar el curso.", e);
        }
    }

    public CourseDTO createCourse(CourseCreateDTO courseCreateDTO, Locale locale) {
        if (courseRepository.existsCourseByCode(courseCreateDTO.getCode())) {
            String errorMessage = messageSource.getMessage("Error al crear el curso.", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        if (courseRepository.existsCourseByCode(courseCreateDTO.getCode())) {
            throw new IllegalArgumentException("El código de ese curso existe");
        }

        // Se convierte a Entity para almacenar en la base de datos
        Course course = courseMapper.toEntity(courseCreateDTO);
        Course savedCourse = courseRepository.save(course);
        // Se devuelve el DTO
        return courseMapper.toDTO(savedCourse);
    }

    public CourseDTO updateCourse(Long id, @Valid CourseCreateDTO courseCreateDTO, Locale locale) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El curso no existe"));

        if (courseRepository.existsByCodeAndIdNot(courseCreateDTO.getCode(), id)) {
            throw new IllegalArgumentException("El código ya está en uso por otro curso.");
        }

        existingCourse.setCode(courseCreateDTO.getCode());
        existingCourse.setName(courseCreateDTO.getName());
        existingCourse.setDescription(courseCreateDTO.getDescription());
        existingCourse.setStartDate(courseCreateDTO.getStartDate());
        existingCourse.setEndDate(courseCreateDTO.getEndDate());

        if (courseCreateDTO.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(courseCreateDTO.getTeacherId())
                    .orElseThrow(() -> new IllegalArgumentException("El profesor no existe"));
            existingCourse.setTeacher(teacher);
        }

        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toDTO(updatedCourse);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("El curso no existe");
        }
        courseRepository.deleteById(id);
    }
}
