-- ===============================
-- TEACHERS
-- ===============================
INSERT INTO teachers (name, phone, email, specialty)
VALUES ('Laura García', '600123456', 'laura.garcia@ies.com', 'Desarrollo Web'),
       ('Carlos Ruiz', '600654321', 'carlos.ruiz@ies.com', 'Ciberseguridad');

-- ===============================
-- STUDENTS
-- ===============================
INSERT INTO students (name, email, phone, image)
VALUES ('Beatriz López', 'beatriz@example.com', '644111222', NULL),
       ('Javier Torres', 'javier@example.com', '644333444', NULL),
       ('Ana Pérez', 'ana@example.com', '644555666', NULL);

-- ===============================
-- COURSES
-- ===============================
INSERT INTO courses (code, name, description, start_date, end_date, teacher_id)
VALUES ('DW', 'Desarrollo Web', 'Curso completo de desarrollo web moderno', '2025-01-10', '2025-06-30', 1),
       ('CS', 'Introducción a la Ciberseguridad', 'Fundamentos esenciales de seguridad informática', '2025-02-01',
        '2025-07-15', 2);

-- ===============================
-- MODULES_COURSES
-- ===============================
INSERT INTO modules_courses (code, name, description, duration, publication_date, course_id)
VALUES ('M1', 'HTML y CSS', 'Fundamentos del diseño web', '02:00:00', '2025-01-12', 1),
       ('M2', 'JavaScript Avanzado', 'Programación avanzada del lado del cliente', '03:00:00', '2025-01-20', 1),
       ('M3', 'Introducción a hacking ético', 'Bases de pentesting', '02:30:00', '2025-02-10', 2);

-- ===============================
-- TUITIONS
-- ===============================
INSERT INTO tuitions (code, date, observation, student_id, course_id)
VALUES ('T1', '2025-01-15', 'Matriculación sin incidencias', 1, 1),
       ('T2', '2025-01-20', 'Pago fraccionado', 2, 1),
       ('T3', '2025-02-05', 'Todo correcto', 3, 2);

-- Insertar datos de ejemplo para 'roles'
INSERT IGNORE INTO roles (id, name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_MANAGER'),
(3, 'ROLE_USER');

-- Insertar datos de ejemplo para 'users'. La contraseña de cada usuario es password
INSERT IGNORE INTO users (id, username, password, enabled, first_name, last_name, image, created_date, last_modified_date, last_password_change_date) VALUES
(1, 'admin', '$2b$12$FVRijCavVZ7Qt15.CQssHe9m/6eLAdjAv0PiOKFIjMU161wApxzye', true, 'Admin', 'User', '/images/admin.jpg', NOW(), NOW(), NOW()),
(2, 'manager', '$2b$12$FVRijCavVZ7Qt15.CQssHe9m/6eLAdjAv0PiOKFIjMU161wApxzye', true, 'Manager', 'User', '/images/manager.jpg', NOW(), NOW(), NOW()),
(3, 'normal', '$2b$12$FVRijCavVZ7Qt15.CQssHe9m/6eLAdjAv0PiOKFIjMU161wApxzye', true, 'Regular', 'User', '/images/user.jpg', NOW(), NOW(), NOW());

-- Asignar el rol de administrador al usuario con id 1
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(1, 1);

-- Asignar el rol de gestor al usuario con id 2
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(2, 2);

-- Asignar el rol de usuario normal al usuario con id 3
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(3, 3);