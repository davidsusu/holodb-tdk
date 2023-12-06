package hu.webarticum.holodb.benchmark.micronaut.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import hu.webarticum.holodb.benchmark.micronaut.model.Student;
import hu.webarticum.holodb.benchmark.micronaut.service.StudentService;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.exceptions.HttpStatusException;

@Controller("/students")
public class StudentController {

    private final StudentService studentService;
    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    
    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> index() {
        return studentService.getAll();
    }

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<Student> get(@Parameter("id") long id) {
        return studentService.get(id);
    }

    @Patch("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Student patch(@Parameter("id") long id, @Body Map<String, Object> data) {
        try {
            return studentService.patch(id, data);
        } catch (NoSuchElementException e) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
    }

}
