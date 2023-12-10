package hu.webarticum.holodb.benchmark.micronaut.controller;

import java.util.List;
import java.util.Map;

import hu.webarticum.holodb.benchmark.micronaut.service.BenchmarkService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.transaction.annotation.Transactional;

@Controller("/benchmark")
public class BenchmarkController {
    
    private final BenchmarkService benchmarkService;
    
    public BenchmarkController(BenchmarkService benchmarkService) {
        this.benchmarkService = benchmarkService;
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Map<String, Object>> benchmark() {
        return benchmarkService.runBenchmark();
    }

    @Get("/single/{n}")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public long benchmarkSingle(int n) {
        return benchmarkService.runSingleBenchmark(n);
    }

    @Get("/single/jpa/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Map<String, Object> benchmarkSingleJpa(long id) {
        return benchmarkService.runSingleJpaBenchmark(id);
    }
    
}
