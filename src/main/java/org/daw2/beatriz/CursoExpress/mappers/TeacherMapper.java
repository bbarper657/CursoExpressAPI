package org.daw2.beatriz.CursoExpress.mappers;

import org.daw2.beatriz.CursoExpress.dtos.TeacherCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.TeacherDTO;
import org.daw2.beatriz.CursoExpress.entities.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {
    public TeacherDTO toDTO(Teacher teacher){
        TeacherDTO dto = new TeacherDTO();
        dto.setId(teacher.getId());
        dto.setName(teacher.getName());
        dto.setPhone(teacher.getPhone());
        dto.setEmail(teacher.getEmail());
        dto.setSpecialty(teacher.getSpecialty());
        return dto;
    }

    public Teacher toEntity(TeacherDTO dto){
        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setName(dto.getName());
        teacher.setPhone(dto.getPhone());
        teacher.setEmail(dto.getEmail());
        teacher.setSpecialty(dto.getSpecialty());
        return teacher;
    }

    public Teacher toEntity(TeacherCreateDTO createDTO){
        Teacher teacher = new Teacher();
        teacher.setName(createDTO.getName());
        teacher.setPhone(createDTO.getPhone());
        teacher.setEmail(createDTO.getEmail());
        teacher.setSpecialty(createDTO.getSpecialty());
        return teacher;
    }
}
