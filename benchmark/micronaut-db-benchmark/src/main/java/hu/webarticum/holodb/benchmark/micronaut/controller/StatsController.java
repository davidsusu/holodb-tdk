package hu.webarticum.holodb.benchmark.micronaut.controller;

import java.util.HashMap;
import java.util.Map;

import hu.webarticum.holodb.benchmark.micronaut.repository.CourseRepository;
import hu.webarticum.holodb.benchmark.micronaut.repository.StudentRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/stats")
public class StatsController {

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;
    
    public StatsController(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }
    
    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> stats() {
        Map<String, Object> result = new HashMap<>();
        result.put("studentCount", studentRepository.count());
        result.put("studentCountIan", studentRepository.countByFirstname("Ian"));
        result.put("sampleStudent", studentRepository.findById(32L).orElse(null));
        result.put("courseCountInSubjectOne", courseRepository.countBySubjectId(1));
        return result;
    }
    
}
