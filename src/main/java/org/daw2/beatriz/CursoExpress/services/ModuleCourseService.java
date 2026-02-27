package org.daw2.beatriz.CursoExpress.services;

import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.ModuleCourseCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.ModuleCourseDTO;
import org.daw2.beatriz.CursoExpress.entities.Course;
import org.daw2.beatriz.CursoExpress.entities.ModuleCourse;
import org.daw2.beatriz.CursoExpress.mappers.ModuleCourseMapper;
import org.daw2.beatriz.CursoExpress.repositories.CourseRepository;
import org.daw2.beatriz.CursoExpress.repositories.ModuleCourseRepository;
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
public class ModuleCourseService {
    private static final Logger logger = LoggerFactory.getLogger(ModuleCourseService.class);

    @Autowired
    private ModuleCourseRepository moduleCourseRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleCourseMapper moduleCourseMapper;

    @Autowired
    private MessageSource messageSource;

    public Page<ModuleCourseDTO> getAllModulesCourses(Pageable pageable) {
        logger.info("Solicitando todos los Módulos con paginación: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<ModuleCourse> moduleCourses = moduleCourseRepository.findAll(pageable);
            logger.info("Se encontraron {} Módulos en la página actual.", moduleCourses.getNumberOfElements());
            return moduleCourses.map(moduleCourseMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al obtener todos los módulos de los cursos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todos los módulos de los cursos", e);
        }
    }

    public Optional<ModuleCourseDTO> getModuleCourseById(Long id) {
        try {
            logger.info("Obteniendo módulo del curso con ID {}", id);
            return moduleCourseRepository.findById(id).map(moduleCourseMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al buscar módulo con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar el módulo del curso.", e);
        }
    }

    public ModuleCourseDTO createModuleCourse(ModuleCourseCreateDTO moduleCourseCreateDTO, Locale locale) {
        if (moduleCourseRepository.existsModuleCourseByCode(moduleCourseCreateDTO.getCode())) {
            String errorMessage = messageSource.getMessage("msg.module.code.exists", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        Course course = courseRepository.findById(moduleCourseCreateDTO.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));

        // Se convierte a Entity para almacenar en la base de datos
        ModuleCourse moduleCourse = moduleCourseMapper.toEntity(moduleCourseCreateDTO);
        moduleCourse.setCourse(course);
        ModuleCourse savedModuleCourse = moduleCourseRepository.save(moduleCourse);
        // Se devuelve el DTO
        return moduleCourseMapper.toDTO(savedModuleCourse);
    }

    public ModuleCourseDTO updateModuleCourse(Long id, @Valid ModuleCourseCreateDTO moduleCourseCreateDTO, Locale locale) {
        ModuleCourse existingModuleCourse = moduleCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El módulo no existe"));

        if (moduleCourseRepository.existsByCodeAndIdNot(moduleCourseCreateDTO.getCode(), id)) {
            throw new IllegalArgumentException("El código ya está en uso por otro módulo.");
        }

        existingModuleCourse.setCode(moduleCourseCreateDTO.getCode());
        existingModuleCourse.setName(moduleCourseCreateDTO.getName());
        existingModuleCourse.setDescription(moduleCourseCreateDTO.getDescription());
        existingModuleCourse.setDuration(moduleCourseCreateDTO.getDuration());
        existingModuleCourse.setPublicationDate(moduleCourseCreateDTO.getPublicationDate());

        if (moduleCourseCreateDTO.getCourseId() != null) {
            Course course = courseRepository.findById(moduleCourseCreateDTO.getCourseId())
                    .orElseThrow(() -> new IllegalArgumentException("El curso no existe"));
            existingModuleCourse.setCourse(course);
        }

        ModuleCourse updatedModuleCourse = moduleCourseRepository.save(existingModuleCourse);
        return moduleCourseMapper.toDTO(updatedModuleCourse);
    }

    public void deleteModuleCourse(Long id) {
        if (!moduleCourseRepository.existsById(id)) {
            throw new IllegalArgumentException("El módulo no existe");
        }
        moduleCourseRepository.deleteById(id);
    }
}
