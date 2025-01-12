#!/usr/bin/python3

import os
import mysql.connector
from mysql.connector import FieldType

conn = mysql.connector.connect(
    host=os.environ["MYSQL_HOST"],
    port=os.environ["MYSQL_PORT"],
    user=os.environ["DB_USERNAME"],
    password=os.environ["DB_PASSWORD"],
    allow_local_infile=True,
)

directory = os.path.dirname(os.path.realpath(__file__)) + '/csv'

sqls = [
    "DROP DATABASE IF EXISTS tdk_benchmark_start",
    "CREATE DATABASE tdk_benchmark_start",
    "CREATE TABLE tdk_benchmark_start.subjects (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, credit INT NOT NULL, INDEX(name), INDEX(credit))",
    "LOAD DATA LOCAL INFILE '" + directory + "/subjects.csv' INTO TABLE tdk_benchmark_start.subjects FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 ROWS",
    "CREATE TABLE tdk_benchmark_start.courses (id BIGINT PRIMARY KEY AUTO_INCREMENT, subject_id INT NOT NULL, year INT NOT NULL, term INT NOT NULL, INDEX(subject_id), INDEX(year), INDEX(term))",
    "LOAD DATA LOCAL INFILE '" + directory + "/courses.csv' INTO TABLE tdk_benchmark_start.courses FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 ROWS",
    "CREATE TABLE tdk_benchmark_start.students (id BIGINT PRIMARY KEY AUTO_INCREMENT, code VARCHAR(255) NOT NULL, firstname VARCHAR(255) NOT NULL, lastname VARCHAR(255) NOT NULL, INDEX(code), INDEX(firstname), INDEX(lastname))",
    "LOAD DATA LOCAL INFILE '" + directory + "/students.csv' INTO TABLE tdk_benchmark_start.students FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 ROWS",
    "CREATE TABLE tdk_benchmark_start.course_student (course_id BIGINT NOT NULL, student_id BIGINT NOT NULL, INDEX(course_id), INDEX(student_id))",
    "LOAD DATA LOCAL INFILE '" + directory + "/course_student.csv' INTO TABLE tdk_benchmark_start.course_student FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 ROWS",
    "FLUSH TABLES",
]

with conn.cursor() as cursor:
    for sql in sqls:
        cursor.execute(sql)
        conn.commit()

conn.close()
