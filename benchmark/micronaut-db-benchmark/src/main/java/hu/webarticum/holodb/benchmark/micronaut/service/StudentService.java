package hu.webarticum.holodb.benchmark.micronaut.service;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.webarticum.holodb.benchmark.micronaut.model.Student;
import hu.webarticum.holodb.benchmark.micronaut.repository.StudentRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class StudentService {

    private final StudentRepository studentRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Transactional
    public Optional<Student> get(long id) {
        return studentRepository.findById(id);
    }

    @Transactional
    public void delete(long id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public Student patch(long id, Map<String, Object> data) {
        Student course = studentRepository.findById(id).get(); // NOSONAR
        try {
            objectMapper.updateValue(course, data);
        } catch (JsonMappingException e) {
            throw new UncheckedIOException(e);
        }
        return studentRepository.update(course);
    }

}
