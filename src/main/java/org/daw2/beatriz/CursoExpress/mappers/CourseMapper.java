package org.daw2.beatriz.CursoExpress.mappers;

import org.daw2.beatriz.CursoExpress.dtos.CourseCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.CourseDTO;
import org.daw2.beatriz.CursoExpress.entities.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseDTO toDTO(Course course){
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setStartDate(course.getStartDate());
        dto.setEndDate(course.getEndDate());
        if (course.getTeacher() != null){
            dto.setTeacherId(course.getTeacher().getId());
        }
        return dto;
    }

    public Course toEntity(CourseDTO dto){
        Course course = new Course();
        course.setId(dto.getId());
        course.setCode(dto.getCode());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setStartDate(dto.getStartDate());
        course.setEndDate(dto.getEndDate());
        return course;
    }

    public Course toEntity(CourseCreateDTO createDTO){
        Course course = new Course();
        course.setCode(createDTO.getCode());
        course.setName(createDTO.getName());
        course.setDescription(createDTO.getDescription());
        course.setStartDate(createDTO.getStartDate());
        course.setEndDate(createDTO.getEndDate());
        return course;
    }
}
