package hu.webarticum.holodb.benchmark.micronaut.repository;

import hu.webarticum.holodb.benchmark.micronaut.model.Student;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

    public long countByFirstname(String firstname);
    
}
