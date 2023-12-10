package hu.webarticum.holodb.benchmark.micronaut.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import hu.webarticum.holodb.benchmark.micronaut.model.Student;
import hu.webarticum.holodb.benchmark.micronaut.repository.StudentRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class BenchmarkService {
    
    private static final int REPEATS = 10000;
    
    private static final String[] SQLS = {
            "SELECT firstname FROM students WHERE id = 30",
            "SELECT * FROM students WHERE id = 30",
            "SELECT s.lastname FROM students s WHERE s.firstname='Ian' LIMIT 20",
            "SELECT * FROM students",
            "SELECT s.lastname FROM students s " +
                    "LEFT JOIN course_student cs ON cs.student_id = s.id "+
                    "LEFT JOIN courses c ON c.id = cs.course_id " +
                    "LEFT JOIN subjects su ON su.id = c.subject_id " +
                    "WHERE s.firstname='Ian' AND su.id=2",
            "SELECT s.id, s.code, s.firstname, s.lastname FROM students s WHERE s.id = 30 LIMIT 1",
    };

    private final DataSource dataSource;

    private final StudentRepository studentRepository;
    
    public BenchmarkService(DataSource dataSource, StudentRepository studentRepository) {
        this.dataSource = dataSource;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public List<Map<String, Object>> runBenchmark() {
        List<Map<String, Object>> result = new ArrayList<>();
        int n = 1;
        for (String sql : SQLS) {
            long avgNanos = benchmarkQuery(sql, REPEATS);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("n", n);
            item.put("sql", sql);
            item.put("repeats", REPEATS);
            item.put("avgNanos", avgNanos);
            result.add(item);
            n++;
        }
        return result;
    }

    @Transactional
    public long runSingleBenchmark(int n) {
        return benchmarkQuery(SQLS[n - 1], REPEATS);
    }

    @Transactional
    public Map<String, Object> runSingleJpaBenchmark(long id) {
        Map<String, Object> result = new LinkedHashMap<>();
        long startTime = System.nanoTime();
        Student student = studentRepository.findById(id).orElseGet(null);
        long endTime = System.nanoTime();
        result.put("nanos", endTime - startTime);
        result.put("student", student);
        return result;
    }
    
    private long benchmarkQuery(String sql, int repeats) {
        long sum = 0;
        for (int i = 0; i < repeats; i++) {
            sum += benchmarkQuery(sql);
        }
        return sum / repeats;
    }
    
    private long benchmarkQuery(String sql) {
        try {
            return benchmarkQueryThrowing(sql);
        } catch (SQLException e) {
            throw new UncheckedIOException(new IOException(e));
        }
    }
    
    private long benchmarkQueryThrowing(String sql) throws SQLException {
        Connection connection = getConnection();
        long startTime = System.nanoTime();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resultSet.getString(1);
                }
            }
        }
        connection.commit();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
    
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
