package hu.webarticum.holodb.benchmark.micronaut.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import hu.webarticum.holodb.benchmark.micronaut.model.Course;
import hu.webarticum.holodb.benchmark.micronaut.service.CourseService;
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

@Controller("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
    
    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> index() {
        return courseService.getAll();
    }

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<Course> get(@Parameter("id") long id) {
        return courseService.get(id);
    }

    @Patch("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Course patch(@Parameter("id") long id, @Body Map<String, Object> data) {
        try {
            return courseService.patch(id, data);
        } catch (NoSuchElementException e) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
    }

}
