package hu.webarticum.holodb.benchmark.micronaut.repository;

import hu.webarticum.holodb.benchmark.micronaut.model.Course;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    
    public long countBySubjectId(long subjectId);
    
}
