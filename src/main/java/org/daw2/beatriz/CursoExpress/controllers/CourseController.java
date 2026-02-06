package org.daw2.beatriz.CursoExpress.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.beatriz.CursoExpress.entities.Course;
import org.iesalixar.daw2.beatriz.CursoExpress.repositories.CourseRepository;
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
@RequestMapping("/courses")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listCourses(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(required = false) String search,
                                     @RequestParam(required = false) String sort,
                                     Model model) {
        logger.info("Solicitando la lista de todos los Cursos..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Course> courses;
        int totalPages = 0;
        if (search != null && !search.isBlank()) {
            courses = courseRepository.findByNameContainingIgnoreCase(search, pageable);
            totalPages = (int) Math.ceil((double) courseRepository.countByNameContainingIgnoreCase(search) / 5);
        } else {
            courses = courseRepository.findAll(pageable);
            totalPages = (int) Math.ceil((double) courseRepository.count() / 5);
        }
        logger.info("Se han cargado {} Cursos.", courses.toList().size());
        model.addAttribute("listCourses", courses.toList());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "course";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo Curso...");
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para el Curso con ID {}", id);
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (!courseOpt.isPresent()) {
            logger.warn("No se encontró el curso con ID {}", id);
        }
        model.addAttribute("course", courseOpt.get());
        return "course-form";
    }

    @PostMapping("/insert")
    public String insertCourse(@Valid @ModelAttribute("course") Course course, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nuevo curso con código {}", course.getCode());
        if (result.hasErrors()) {
            return "course-form";
        }
        if (courseRepository.existsCourseByCode(course.getCode())) {
            logger.warn("El código del curso {} ya existe.", course.getCode());
            String errorMessage = messageSource.getMessage("El código del Curso ya existe", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/courses/new";
        }

        courseRepository.save(course);
        logger.info("Cursos {} insertados con éxito.", course.getCode());
        return "redirect:/courses";
    }

    @PostMapping("/update")
    public String updateCourse(@Valid @ModelAttribute("course") Course course, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando Curso con ID {}", course.getId());
        if (result.hasErrors()) {
            return "course-form";
        }
        try {
            courseRepository.save(course);
            redirectAttributes.addFlashAttribute("successMessage", "Curso actualizado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo actualizar el curso");
        }
        return "redirect:/courses";
    }

    @PostMapping("/delete")
    public String deleteCourse(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando Curso con ID {}", id);
        try {
            courseRepository.deleteById(id);
            logger.info("Curso con ID {} eliminado con éxito.", id);
            redirectAttributes.addFlashAttribute("successMessage", "Curso eliminado con éxito");
        } catch (Exception e) {
            logger.error("No se pudo eliminar el Curso con ID {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar el curso");
        }
        return "redirect:/courses";
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
