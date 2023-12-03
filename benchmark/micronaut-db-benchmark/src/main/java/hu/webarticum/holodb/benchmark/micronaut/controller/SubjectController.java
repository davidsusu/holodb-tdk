package hu.webarticum.holodb.benchmark.micronaut.controller;

import java.util.List;

import hu.webarticum.holodb.benchmark.micronaut.model.Subject;
import hu.webarticum.holodb.benchmark.micronaut.repository.SubjectRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import jakarta.transaction.Transactional;

@Controller("/subjects")
public class SubjectController {

    private final SubjectRepository subjectRepository;
    
    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }
    
    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Subject> index() {
        return subjectRepository.findAll();
    }

}
