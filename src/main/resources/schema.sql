DROP TABLE IF EXISTS tuitions;
DROP TABLE IF EXISTS modules_courses;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teachers;

-- Tabla: TEACHERS
CREATE TABLE teachers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(25) NOT NULL,
    email VARCHAR(100) NOT NULL,
    specialty VARCHAR(300) NOT NULL
);

-- Tabla: STUDENTS
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(25) NOT NULL
);

-- Tabla: COURSES (Depende de teachers)
CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(2) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    teacher_id BIGINT NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);

-- Tabla: MODULES_COURSES
CREATE TABLE modules_courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(2) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    duration TIME NOT NULL,
    publication_date DATE NOT NULL,
    course_id BIGINT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Tabla: TUITIONS
CREATE TABLE tuitions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(2) NOT NULL,
    date DATE NOT NULL,
    observation VARCHAR(500) NOT NULL,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);