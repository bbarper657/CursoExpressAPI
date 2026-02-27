package org.daw2.beatriz.CursoExpress.services;

import jakarta.validation.Valid;
import org.daw2.beatriz.CursoExpress.dtos.TeacherCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.TeacherDTO;
import org.daw2.beatriz.CursoExpress.entities.Teacher;
import org.daw2.beatriz.CursoExpress.mappers.TeacherMapper;
import org.daw2.beatriz.CursoExpress.repositories.TeacherRepository;
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
public class TeacherService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private MessageSource messageSource;

    public Page<TeacherDTO> getAllTeachers(Pageable pageable) {
        logger.info("Solicitando todos los Profesores con paginación: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Teacher> teachers = teacherRepository.findAll(pageable);
            logger.info("Se encontraron {} profesores en la página actual.", teachers.getNumberOfElements());
            return teachers.map(teacherMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al obtener todos los profesores: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todos los profesores", e);
        }
    }

    public Optional<TeacherDTO> getTeacherById(Long id) {
        try {
            logger.info("Obteniendo profesor con ID {}", id);
            return teacherRepository.findById(id).map(teacherMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al buscar profesor con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar el profesor.", e);
        }
    }

    public TeacherDTO createTeacher(TeacherCreateDTO teacherCreateDTO, Locale locale) {
        if (teacherRepository.existsByEmail(teacherCreateDTO.getEmail())) {
            String errorMessage = messageSource.getMessage("Error al crear el profesor.", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        // Se convierte a Entity para almacenar en la base de datos
        Teacher teacher = teacherMapper.toEntity(teacherCreateDTO);
        Teacher savedTeacher = teacherRepository.save(teacher);
        // Se devuelve el DTO
        return teacherMapper.toDTO(savedTeacher);
    }

    public TeacherDTO updateTeacher(Long id, @Valid TeacherCreateDTO teacherCreateDTO, Locale locale) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El profesor no existe"));

        if (teacherRepository.existsByEmailAndIdNot(teacherCreateDTO.getEmail(), id)) {
            throw new IllegalArgumentException("El email ya está en uso por otro profesor.");
        }

        existingTeacher.setName(teacherCreateDTO.getName());
        existingTeacher.setPhone(teacherCreateDTO.getPhone());
        existingTeacher.setEmail(teacherCreateDTO.getEmail());
        existingTeacher.setSpecialty(teacherCreateDTO.getSpecialty());
        Teacher updatedTeacher = teacherRepository.save(existingTeacher);
        return teacherMapper.toDTO(updatedTeacher);
    }

    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new IllegalArgumentException("El profesor no existe");
        }
        teacherRepository.deleteById(id);
    }
}
