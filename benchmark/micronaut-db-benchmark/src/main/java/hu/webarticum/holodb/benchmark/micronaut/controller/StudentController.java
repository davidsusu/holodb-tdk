package hu.webarticum.holodb.benchmark.micronaut.controller;

import java.util.List;

import hu.webarticum.holodb.benchmark.micronaut.model.Student;
import hu.webarticum.holodb.benchmark.micronaut.repository.StudentRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import jakarta.transaction.Transactional;

@Controller("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Student> index() {
        return studentRepository.findAll();
    }

}
