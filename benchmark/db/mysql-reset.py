#!/usr/bin/python3

import os
import mysql.connector
from mysql.connector import FieldType

conn = mysql.connector.connect(
    host=os.environ["MYSQL_HOST"],
    port=os.environ["MYSQL_PORT"],
    user=os.environ["DB_USERNAME"],
    password=os.environ["DB_PASSWORD"],
)

sqls = [
    "CREATE DATABASE IF NOT EXISTS tdk_benchmark",
    "DROP TABLE IF EXISTS tdk_benchmark.subjects",
    "CREATE TABLE tdk_benchmark.subjects LIKE tdk_benchmark_start.subjects",
    "INSERT INTO tdk_benchmark.subjects SELECT * from tdk_benchmark_start.subjects",
    "DROP TABLE IF EXISTS tdk_benchmark.courses",
    "CREATE TABLE tdk_benchmark.courses LIKE tdk_benchmark_start.courses",
    "INSERT INTO tdk_benchmark.courses SELECT * from tdk_benchmark_start.courses",
    "DROP TABLE IF EXISTS tdk_benchmark.students",
    "CREATE TABLE tdk_benchmark.students LIKE tdk_benchmark_start.students",
    "INSERT INTO tdk_benchmark.students SELECT * from tdk_benchmark_start.students",
    "DROP TABLE IF EXISTS tdk_benchmark.course_student",
    "CREATE TABLE tdk_benchmark.course_student LIKE tdk_benchmark_start.course_student",
    "INSERT INTO tdk_benchmark.course_student SELECT * from tdk_benchmark_start.course_student",
]

with conn.cursor() as cursor:
    for sql in sqls:
        cursor.execute(sql)
        conn.commit()

conn.close()
