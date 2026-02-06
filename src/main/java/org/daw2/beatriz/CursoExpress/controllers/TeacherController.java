package org.daw2.beatriz.CursoExpress.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.beatriz.CursoExpress.entities.Teacher;
import org.iesalixar.daw2.beatriz.CursoExpress.repositories.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listTeachers(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(required = false) String search,
                              @RequestParam(required = false) String sort,
                              Model model) {
        logger.info("Solicitando la lista de todos los Profesores..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Teacher> teachers;
        int totalPages = 0;
        if (search != null && !search.isBlank()) {
            teachers = teacherRepository.findByNameContainingIgnoreCase(search, pageable);
            totalPages = (int) Math.ceil((double) teacherRepository.countByNameContainingIgnoreCase(search) / 5);
        } else {
            teachers = teacherRepository.findAll(pageable);
            totalPages = (int) Math.ceil((double) teacherRepository.count() / 5);
        }
        logger.info("Se han cargado {} Profesores.", teachers.toList().size());
        model.addAttribute("listTeachers", teachers.toList());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "teacher";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo Profesor...");
        model.addAttribute("teacher", new Teacher());
        return "teacher-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para el Profesor con ID {}", id);
        Optional<Teacher> teacherOpt = teacherRepository.findById(id);
        if (!teacherOpt.isPresent()) {
            logger.warn("No se encontró el profesor con ID {}", id);
        }
        model.addAttribute("teacher", teacherOpt.get());
        return "teacher-form";
    }

    @PostMapping("/insert")
    public String insertTeacher(@Valid @ModelAttribute("teacher") Teacher teacher, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nuevo profesor con id {}", teacher.getId());
        if (result.hasErrors()) {
            return "teacher-form";
        }

        teacherRepository.save(teacher);
        logger.info("Proferores {} insertados con éxito.", teacher.getId());
        return "redirect:/teachers";
    }

    @PostMapping("/update")
    public String updateTeacher(@Valid @ModelAttribute("teacher") Teacher teacher, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando profesor con ID {}", teacher.getId());
        if (result.hasErrors()) {
            return "teacher-form";
        }
        try {
            teacherRepository.save(teacher);
            redirectAttributes.addFlashAttribute("successMessage", "Profesor actualizado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo actualizar el profesor");
        }
        return "redirect:/teachers";
    }

    @PostMapping("/delete")
    public String deleteTeacher(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando Profesor con ID {}", id);
        try {
            teacherRepository.deleteById(id);
            logger.info("Profesor con ID {} eliminado con éxito.", id);
            redirectAttributes.addFlashAttribute("successMessage", "Profesor eliminado con éxito");
        } catch (Exception e) {
            logger.error("No se pudo eliminar el Profesor con ID {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar el profesor");
        }
        return "redirect:/teachers";
    }

    private Sort getSort(String sort) {
        if (sort == null) {
            return Sort.by("id").ascending();
        }
        return switch (sort) {
            case "nameAsc" -> Sort.by("name").ascending();
            case "nameDesc" -> Sort.by("name").descending();
            case "codeAsc" -> Sort.by("code").ascending();
            case "codeDesc" -> Sort.by("code").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}
