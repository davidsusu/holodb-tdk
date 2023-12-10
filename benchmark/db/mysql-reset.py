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
    "DROP DATABASE IF EXISTS tdk_benchmark",
    "CREATE DATABASE tdk_benchmark",
    "CREATE TABLE tdk_benchmark.subjects LIkE tdk_benchmark_start.subjects",
    "INSERT INTO tdk_benchmark.subjects SELECT * from tdk_benchmark_start.subjects",
    "CREATE TABLE tdk_benchmark.courses LIkE tdk_benchmark_start.courses",
    "INSERT INTO tdk_benchmark.courses SELECT * from tdk_benchmark_start.courses",
    "CREATE TABLE tdk_benchmark.students LIkE tdk_benchmark_start.students",
    "INSERT INTO tdk_benchmark.students SELECT * from tdk_benchmark_start.students",
    "CREATE TABLE tdk_benchmark.course_student LIkE tdk_benchmark_start.course_student",
    "INSERT INTO tdk_benchmark.course_student SELECT * from tdk_benchmark_start.course_student",
]

for sql in sqls:
    remove_cursor = conn.cursor()
    remove_cursor.execute(sql)

conn.commit()

conn.close()
