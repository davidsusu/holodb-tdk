package hu.webarticum.holodb.benchmark.micronaut.repository;

import hu.webarticum.holodb.benchmark.micronaut.model.Subject;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
    
}
