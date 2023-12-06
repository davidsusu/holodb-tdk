package hu.webarticum.holodb.benchmark.micronaut.controller;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import jakarta.persistence.EntityManager;

@Controller("/metrics")
public class MetricsController {

    private final EntityManager entityManager;
    
    public MetricsController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> metrics() {
        Session session = entityManager.unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        
        Map<String, Object> result = new HashMap<>();
        result.put("studentCount", statistics.getQueryExecutionMaxTime());
        return result;
    }
    
}
