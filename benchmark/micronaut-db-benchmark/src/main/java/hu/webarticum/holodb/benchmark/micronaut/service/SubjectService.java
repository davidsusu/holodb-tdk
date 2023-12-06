package hu.webarticum.holodb.benchmark.micronaut.service;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.webarticum.holodb.benchmark.micronaut.model.Subject;
import hu.webarticum.holodb.benchmark.micronaut.repository.SubjectRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class SubjectService {

    private final SubjectRepository subjectRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    @Transactional
    public Optional<Subject> get(long id) {
        return subjectRepository.findById(id);
    }

    @Transactional
    public Subject patch(long id, Map<String, Object> data) {
        Subject course = subjectRepository.findById(id).get(); // NOSONAR
        try {
            objectMapper.updateValue(course, data);
        } catch (JsonMappingException e) {
            throw new UncheckedIOException(e);
        }
        return subjectRepository.update(course);
    }

}
