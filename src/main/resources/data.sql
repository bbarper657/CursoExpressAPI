--
-- PASO 1: Entidades Independientes (IDs 1, 2, 3...)
--

-- TEACHERS
INSERT INTO teachers (name, phone, email, specialty) VALUES
    ('Laura García', '600123456', 'laura.garcia@ies.com', 'Desarrollo Web'); -- ID = 1

INSERT INTO teachers (name, phone, email, specialty) VALUES
    ('Carlos Ruiz', '600654321', 'carlos.ruiz@ies.com', 'Ciberseguridad'); -- ID = 2

-- STUDENTS
INSERT INTO students (name, email, phone) VALUES
    ('Beatriz López', 'beatriz@example.com', '644111222'); -- ID = 1

INSERT INTO students (name, email, phone) VALUES
    ('Javier Torres', 'javier@example.com', '644333444'); -- ID = 2

INSERT INTO students (name, email, phone) VALUES
    ('Ana Pérez', 'ana@example.com', '644555666'); -- ID = 3


--
-- PASO 2: Entidades Dependientes (usando los IDs fijos del paso 1)
--

-- COURSES (teacher_id: 1 para Laura García, 2 para Carlos Ruiz)
INSERT INTO courses (code, name, description, start_date, end_date, teacher_id) VALUES
    ('DW', 'Desarrollo Web', 'Curso completo de desarrollo web moderno', '2025-01-10', '2025-06-30', 1); -- ID = 1 (FK teacher_id = 1)

INSERT INTO courses (code, name, description, start_date, end_date, teacher_id) VALUES
    ('CS', 'Introducción a la Ciberseguridad', 'Fundamentos esenciales de seguridad informática', '2025-02-01', '2025-07-15', 2); -- ID = 2 (FK teacher_id = 2)


--
-- PASO 3: Entidades Relacionadas (usando los IDs fijos de los pasos 1 y 2)
--

-- MODULES_COURSES (course_id: 1 para DW, 2 para CS)
INSERT INTO modules_courses (code, name, description, duration, publication_date, course_id) VALUES
    ('M1', 'HTML y CSS', 'Fundamentos del diseño web', '02:00:00', '2025-01-12', 1); -- ID = 1 (FK course_id = 1)

INSERT INTO modules_courses (code, name, description, duration, publication_date, course_id) VALUES
    ('M2', 'JavaScript Avanzado', 'Programación avanzada del lado del cliente', '03:00:00', '2025-01-20', 1); -- ID = 2 (FK course_id = 1)

INSERT INTO modules_courses (code, name, description, duration, publication_date, course_id) VALUES
    ('M3', 'Introducción a hacking ético', 'Bases de pentesting', '02:30:00', '2025-02-10', 2); -- ID = 3 (FK course_id = 2)


-- TUITIONS (student_id: 1, 2, 3... | course_id: 1, 2)
INSERT INTO tuitions (code, date, observation, student_id, course_id) VALUES
    ('T1', '2025-01-15', 'Matriculación sin incidencias', 1, 1); -- (FK student_id = 1, FK course_id = 1)

INSERT INTO tuitions (code, date, observation, student_id, course_id) VALUES
    ('T2', '2025-01-20', 'Pago fraccionado', 2, 1); -- (FK student_id = 2, FK course_id = 1)

INSERT INTO tuitions (code, date, observation, student_id, course_id) VALUES
    ('T3', '2025-02-05', 'Todo correcto', 3, 2); -- (FK student_id = 3, FK course_id = 2)