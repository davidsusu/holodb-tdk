package hu.webarticum.holodb.benchmark.micronaut.controller;

import java.util.HashMap;
import java.util.Map;

import hu.webarticum.holodb.benchmark.micronaut.repository.StudentRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/stats")
public class StatsController {

    private final StudentRepository studentRepository;
    
    public StatsController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> index() {
        Map<String, Object> result = new HashMap<>();
        result.put("studentCount", studentRepository.count());
        result.put("sampleStudent", studentRepository.findById(32L).orElse(null));
        return result;
    }

}
