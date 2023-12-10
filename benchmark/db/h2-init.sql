CREATE TABLE course_student(course_id BIGINT, student_id BIGINT)
  AS SELECT * FROM CSVREAD('../db/csv/course_student.csv', NULL, 'charset=UTF-8 caseSensitiveColumnNames=true')
;
CREATE INDEX course_student_course_id_idx ON course_student(course_id);
CREATE INDEX course_student_student_id_idx ON course_student(student_id);

CREATE TABLE subjects(id BIGINT PRIMARY KEY, name VARCHAR(255), credit INT)
  AS SELECT * FROM CSVREAD('../db/csv/subjects.csv', NULL, 'charset=UTF-8 caseSensitiveColumnNames=true')
;

CREATE TABLE courses(id BIGINT PRIMARY KEY, subject_id BIGINT, school_year INT, term INT)
  AS SELECT * FROM CSVREAD('../db/csv/courses.csv', NULL, 'charset=UTF-8 caseSensitiveColumnNames=true')
;
CREATE INDEX courses_subject_id_idx ON courses(subject_id);

CREATE TABLE students(id BIGINT PRIMARY KEY, code VARCHAR(255), firstname VARCHAR(255), lastname VARCHAR(255))
  AS SELECT * FROM CSVREAD('../db/csv/students.csv', NULL, 'charset=UTF-8 caseSensitiveColumnNames=true')
;
CREATE INDEX students_firstname_idx ON students(firstname);
CREATE INDEX students_lastname_idx ON students(lastname);
