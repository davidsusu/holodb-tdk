package hu.webarticum.holodb.benchmark.micronaut.client;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main {
    
    private static final String BACKEND_URL = "http://localhost:8080";
    
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        //RequestBody body = RequestBody.create(requestBody, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "/stats")
                .get()
                //.addHeader(URLConstants.API_KEY_NAME, URLConstants.API_KEY_VALUE)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
    
}
