package hu.webarticum.holodb.benchmark.micronaut.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main {
    
    private static final String BACKEND_URL = "http://localhost:8080";
    
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("No test type CLI argument given");
        }
        if (args[0].equals("simple-readonly")) {
            runSimpleReadonly();
        } else if (args[0].equals("complex-readonly")) {
            runComplexReadonly();
        } else if (args[0].equals("complex-writeable")) {
            runComplexWriteable();
        } else if (args[0].equals("benchmarks")) {
            runBenchmarks();
        } else {
            throw new IllegalArgumentException("Unkown test type CLI argument XXX: " + args[0]);
        }
    }
    
    public static void runSimpleReadonly() throws IOException {
        OkHttpClient client = createClient();
        display(getSubject(client, 1));
        for (int i = 1; i <= 100; i++) {
            getSubject(client, i % 3);
        }
        display(getCourse(client, 1));
        for (int i = 1; i <= 100; i++) {    
            getCourse(client, i % 30);
        }
        display(getStudent(client, 1));
        for (int i = 1; i <= 10000; i++) {
            getStudent(client, i);
        }
        displayLine();
    }
    
    public static void runComplexReadonly() throws IOException {
        OkHttpClient client = createClient();
        display(getStats(client));
        for (int i = 1; i <= 100; i++) {
            getStats(client);
        }
        display(getStats(client));
        display(getSubject(client, 1));
        for (int i = 1; i <= 100; i++) {
            getSubject(client, i % 3);
        }
        display(getStats(client));
        display(getCourse(client, 1));
        for (int i = 1; i <= 100; i++) {
            getCourse(client, i % 30);
        }
        display(getStats(client));
        display(getStudent(client, 1));
        for (int i = 1; i <= 10000; i++) {
            getStudent(client, i);
        }
        display(getStats(client));
        displayLine();
    }
    
    public static void runComplexWriteable() throws IOException {
        OkHttpClient client = createClient();
        display(getStats(client));
        for (int i = 1; i <= 100; i++) {
            getStats(client);
        }
        display(getStats(client));
        display(getSubject(client, 1));
        for (int i = 1; i <= 100; i++) {
            getSubject(client, i % 3);
        }
        display(getStats(client));
        display(getCourse(client, 1));
        for (int i = 1; i <= 100; i++) {
            getCourse(client, i % 30);
        }
        display(getStats(client));
        display(getStudent(client, 1));
        for (int i = 1; i <= 10000; i++) {
            getStudent(client, i);
        }
        display(getStats(client));
        for (int i = 1; i <= 50; i++) {
            updateStudent(client, i, "firstname", "David");
        }
        display(getStats(client));
        display(getStudent(client, 1));
        for (int i = 1; i <= 10000; i++) {
            getStudent(client, i);
        }
        display(getStats(client));
        displayLine();
    }
    
    private static void runBenchmarks() throws IOException {
        OkHttpClient client = createClient();
        print(getBenchmarks(client));
    }
    
    private static OkHttpClient createClient() {
        return  new OkHttpClient.Builder()
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .build();
    }

    private static void display(Object content) {
        displayLine();
        print(content);
    }

    private static void print(Object content) {
        System.out.println(content);
    }

    private static void displayLine() {
        System.out.println("-------------------------------------------------");
    }
    
    private static String getStats(OkHttpClient client) throws IOException {
        return fetchUrl(client, BACKEND_URL + "/stats");
    }

    private static String getBenchmarks(OkHttpClient client) throws IOException {
        return fetchUrl(client, BACKEND_URL + "/benchmark");
    }

    private static String getSubject(OkHttpClient client, long id) throws IOException {
        return fetchUrl(client, BACKEND_URL + "/subjects/" + id);
    }

    private static String getCourse(OkHttpClient client, long id) throws IOException {
        return fetchUrl(client, BACKEND_URL + "/courses/" + id);
    }

    private static String getStudent(OkHttpClient client, long id) throws IOException {
        return fetchUrl(client, BACKEND_URL + "/students/" + id);
    }

    private static String fetchUrl(OkHttpClient client, String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static String updateStudent(OkHttpClient client, long studentId, String key, Object value) throws IOException {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(key, value);
        String bodyString = OBJECT_MAPPER.writeValueAsString(valueMap);
        RequestBody body = RequestBody.create(bodyString, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "/students/" + studentId)
                .patch(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
