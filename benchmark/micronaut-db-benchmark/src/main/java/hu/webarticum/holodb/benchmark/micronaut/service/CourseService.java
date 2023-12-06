package hu.webarticum.holodb.benchmark.micronaut.service;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.webarticum.holodb.benchmark.micronaut.model.Course;
import hu.webarticum.holodb.benchmark.micronaut.repository.CourseRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class CourseService {

    private final CourseRepository courseRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Transactional
    public Optional<Course> get(long id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public Course patch(long id, Map<String, Object> data) {
        Course course = courseRepository.findById(id).get(); // NOSONAR
        try {
            objectMapper.updateValue(course, data);
        } catch (JsonMappingException e) {
            throw new UncheckedIOException(e);
        }
        return courseRepository.update(course);
    }

}
