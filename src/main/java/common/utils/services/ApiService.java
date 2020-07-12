package common.utils.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ApiService {
    private static Logger log = Logger.getLogger(ApiService.class.getName());
    private OkHttpClient client;
    private String authHeaderName = null;
    private String authHeaderValue = null;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    private final Long READ_TIMEOUT = 60000L;

    public ApiService(String authHeaderName, String authHeaderValue) {
        this.authHeaderName = authHeaderName;
        this.authHeaderValue = authHeaderValue;
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
    }

    public ApiService() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
    }

    public Response post(String url, HashMap map) {
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);

        if (authHeaderName != null && authHeaderValue != null) {
            requestBuilder.addHeader(authHeaderName, authHeaderValue);
        }

        Request request = requestBuilder.build();

        log.info("Request details: \n\n" +
                "url: " + request.url() + "\n\n" +
                "headers: " + request.headers().toMultimap() + "\n\n" +
                "body: " + json);

        Response response = null;
        try {
            response = this.client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResponseBody responseBody = null;
        try {
            responseBody = response.peekBody(Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String responseData = null;
        try {
            responseData = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Response details: \n\n" +
                "code: " + response.code() + " \n\n" +
                "body text: " + responseData + " \n\n" +
                "headers: " + response.headers().toMultimap() + " \n\n" +
                "timestamp: " + new Date());

        return response;
    }

    public Response get(String url) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get();

        if (authHeaderName != null && authHeaderValue != null) {
            requestBuilder.addHeader(authHeaderName, authHeaderValue);
        }

        Request request = requestBuilder.build();

        log.info("Request details: \n\n" +
                "url: " + request.url() + "\n\n" +
                "headers: " + request.headers().toMultimap());

        Response response = null;
        try {
            response = this.client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResponseBody responseBody = null;//response.body();
        try {
            responseBody = response.peekBody(Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String responseData = null;
        try {
            responseData = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Response details: \n\n" +
                "code: " + response.code() + " \n\n" +
                "body text: " + responseData + " \n\n" +
                "headers: " + response.headers().toMultimap() + " \n\n" +
                "timestamp: " + new Date());

        return response;
    }

    public Response delete(String url) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .delete();

        if (authHeaderName != null && authHeaderValue != null) {
            requestBuilder.addHeader(authHeaderName, authHeaderValue);
        }

        Request request = requestBuilder.build();

        log.info("Request details: \n\n" +
                "url: " + request.url() + "\n\n" +
                "headers: " + request.headers().toMultimap());

        Response response = null;
        try {
            response = this.client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResponseBody responseBody = null;//response.body();
        try {
            responseBody = response.peekBody(Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String responseData = null;
        try {
            responseData = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Response details: \n\n" +
                "code: " + response.code() + " \n\n" +
                "body text: " + responseData + " \n\n" +
                "headers: " + response.headers().toMultimap() + " \n\n" +
                "timestamp: " + new Date());

        return response;
    }

    public static <T> T formatResult(String response, Class<T> aClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper.readValue(response, aClass);
    }
}
