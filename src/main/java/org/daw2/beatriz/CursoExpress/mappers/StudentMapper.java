package org.daw2.beatriz.CursoExpress.mappers;

import org.daw2.beatriz.CursoExpress.dtos.StudentCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.StudentDTO;
import org.daw2.beatriz.CursoExpress.entities.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student){
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setName(student.getName());
        return dto;
    }

    public Student toEntity(StudentDTO dto){
        Student student = new Student();
        student.setId(dto.getId());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setName(dto.getName());
        return student;
    }

    public Student toEntity(StudentCreateDTO createDTO){
        Student student = new Student();
        student.setName(createDTO.getName());
        student.setPhone(createDTO.getPhone());
        student.setEmail(createDTO.getEmail());
        return student;
    }
}
