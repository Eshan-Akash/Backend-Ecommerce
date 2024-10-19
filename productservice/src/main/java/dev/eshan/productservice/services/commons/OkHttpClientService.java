package dev.eshan.productservice.services.commons;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import dev.eshan.productservice.exceptions.HttpServiceException;
import dev.eshan.productservice.exceptions.RetryableHttpServiceException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static dev.eshan.productservice.utils.Utils.getMediaTypeFromFile;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class OkHttpClientService {
    private static final OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(10)).writeTimeout(Duration.ofSeconds(10)).build();

    private static final List<Integer> RETRYABLE_HTTP_CODES = Lists.newArrayList(408, 500, 502, 503, 504, 522, 524);
    private static final List<Integer> SUCCESS_HTTP_CODES = Lists.newArrayList(200, 201);

    @Retryable(retryFor = {RetryableHttpServiceException.class, IOException.class, HttpConnectTimeoutException.class,
            HttpTimeoutException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10))
    public String getCall(String url, String body, Map<String, String> headersMap) throws IOException {
        log.info("..calling url with GET method: {} ", url);
        return getMethodCall(url, body, headersMap);
    }

    @Async
    @Retryable(retryFor = {RetryableHttpServiceException.class, IOException.class, HttpConnectTimeoutException.class,
            HttpTimeoutException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10))
    public CompletableFuture<String> getCallAsync(String url, String body, Map<String, String> headersMap) throws IOException {
        log.info("..calling url asynchronously with GET method: {} ", url);
        try {
            String response = getMethodCall(url, body, headersMap);
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            log.error("Got exception for uri {}, exception cause={}, exception message={}. Retrying...",
                    url, e.getCause(), e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @NotNull
    private static String getMethodCall(String url, String body, Map<String, String> headersMap) throws IOException {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headersMap != null && !headersMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request.Builder request = new Request.Builder()
                .url(url)
                .get()
                .headers(headerBuilder.build());

        if (!Strings.isNullOrEmpty(body)) {
            request = request.put(RequestBody.create(MediaType.parse(APPLICATION_JSON.toString()), body));
        }
        try (Response response = client.newCall(request.build()).execute()) {
            log.info("Service gave {} for uri {} ", response.code(), url);
            if (RETRYABLE_HTTP_CODES.contains(response.code())) {
                throw new RetryableHttpServiceException(HttpStatus.valueOf(response.code()));
            } else if (!SUCCESS_HTTP_CODES.contains(response.code())) {
                throw new HttpServiceException(HttpStatus.valueOf(response.code()));
            }
            return response.body().string();
        } catch (IOException e) {
            log.error("IO Exception for uri {}, cause={}, message={}. Retrying...", url, e.getCause(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception for uri {}, cause={}, message={}. Retrying...", url, e.getCause(), e.getMessage());
            throw new RuntimeException("Failed to make call", e);
        }
    }

    @Retryable(retryFor = {RetryableHttpServiceException.class, IOException.class, HttpConnectTimeoutException.class,
            HttpTimeoutException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10))
    public byte[] postCallForByteArray(String url, RequestBody requestBody, Map<String, String> headersMap)
            throws IOException {
        log.info("..calling url with POST method {} ", url);
        Request request = getRequestForPostCall(url, requestBody, headersMap);
        try (Response response = client.newCall(request).execute()) {
            log.info("Service gave {} for uri {} ", response.code(), url);
            if (RETRYABLE_HTTP_CODES.contains(response.code())) {
                throw new RetryableHttpServiceException(HttpStatus.valueOf(response.code()));
            } else if (!SUCCESS_HTTP_CODES.contains(response.code())) {
                throw new HttpServiceException(HttpStatus.valueOf(response.code()));
            }
            return response.body().bytes();
        } catch (Exception e) {
            log.error("Got exception for uri {}, exception cause={}, exception message={}. Retrying... ",
                    url, e.getCause(), e.getMessage());
            throw e;
        }
    }

    @NotNull
    private static Request getRequestForPostCall(String url, RequestBody requestBody, Map<String, String> headersMap) {
        if (requestBody == null) {
            requestBody = RequestBody.create("{}", MediaType.parse(APPLICATION_JSON.toString()));
        }
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headersMap != null && !headersMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(headerBuilder.build())
                .build();
    }


    @Retryable(retryFor = {RetryableHttpServiceException.class, IOException.class, HttpConnectTimeoutException.class,
            HttpTimeoutException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10))
    public String deleteCall(String url, String body, Map<String, String> headersMap) throws IOException {
        log.info("..calling url with DELETE method: {} ", url);
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headersMap != null && !headersMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request.Builder request = new Request.Builder()
                .url(url)
                .delete()
                .headers(headerBuilder.build());

        if (!Strings.isNullOrEmpty(body)) {
            request = request.put(RequestBody.create(MediaType.parse(APPLICATION_JSON.toString()), body));
        }
        try (Response response = client.newCall(request.build()).execute()) {
            log.info("Service gave {} for uri {} ", response.code(), url);
            if (RETRYABLE_HTTP_CODES.contains(response.code())) {
                throw new RetryableHttpServiceException(HttpStatus.valueOf(response.code()));
            } else if (response.code() != 200) {
                throw new HttpServiceException(HttpStatus.valueOf(response.code()));
            }
            return response.body().string();
        } catch (Exception e) {
            log.error("Got exception for uri {}, exception cause={}, exception message={}. Retrying... ",
                    url, e.getCause(), e.getMessage());
            throw e;
        }
    }

    public String postCallWithFormData(String url, Map<String, String> headersMap, Map<String, Object> formDataParams) throws IOException {
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String, Object> entry : formDataParams.entrySet()) {
            if (entry.getValue() instanceof File) {
                File file = (File) entry.getValue();
                requestBodyBuilder.addFormDataPart(entry.getKey(), file.getName(),
                        RequestBody.create(MediaType.parse(getMediaTypeFromFile(file.getName())), file));
            } else if (entry.getValue() instanceof String) {
                requestBodyBuilder.addFormDataPart(entry.getKey(), (String) entry.getValue());
            }
        }
        RequestBody requestBody = requestBodyBuilder.build();
        return postCall(url, requestBody, headersMap);
    }

    @Retryable(retryFor = {RetryableHttpServiceException.class, IOException.class, HttpConnectTimeoutException.class,
            HttpTimeoutException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10))
    public String postCall(String url, RequestBody requestBody, Map<String, String> headersMap)
            throws IOException {
        log.info("..calling url with POST method {} ", url);
        Request request = getRequestForPostCall(url, requestBody, headersMap);
        try (Response response = client.newCall(request).execute()) {
            log.info("Service gave {} for uri {} ", response.code(), url);
            if (RETRYABLE_HTTP_CODES.contains(response.code())) {
                throw new RetryableHttpServiceException(HttpStatus.valueOf(response.code()));
            } else if (!SUCCESS_HTTP_CODES.contains(response.code())) {
                throw new HttpServiceException(HttpStatus.valueOf(response.code()));
            }
            return response.body().string();
        } catch (Exception e) {
            log.error("Got exception for uri {}, exception cause={}, exception message={}. Retrying... ",
                    url, e.getCause(), e.getMessage());
            throw e;
        }
    }

    public File downloadFile(String url, Map<String, String> headersMap, String fileName, String methodType) throws IOException {
        log.info("Downloading file from URL: {}", url);
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headersMap != null && !headersMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request;
        if ("GET".equalsIgnoreCase(methodType)) {
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .headers(headerBuilder.build())
                    .build();
        } else if ("POST".equalsIgnoreCase(methodType)) {
            request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(null, new byte[0]))
                    .headers(headerBuilder.build())
                    .build();
        } else {
            log.error("Method type {} not supported to download file", methodType);
            return null;
        }
        try (Response response = client.newCall(request).execute()) {
            log.info("Service responded with status code {} for URL {}", response.code(), url);
            if (response.code() != 200) {
                throw new HttpServiceException(HttpStatus.valueOf(response.code()));
            }
            okhttp3.ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Response body is null for URL " + url);
            }
            File file = new File(fileName);
            try (InputStream inputStream = responseBody.byteStream();
                 FileOutputStream fos = new FileOutputStream(file)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                return file;
            }
        } catch (Exception e) {
            log.error("Exception occurred while downloading file from URL {}: cause={}, message={}",
                    url, e.getCause(), e.getMessage(), e);
            throw e;
        }
    }

    public String putCallForFormData(String url, List<String> fileKeys, List<File> files, Map<String, String> formData,
                                     Map<String, String> headersMap) throws IOException {
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (files != null && fileKeys != null) {
            int minSize = Math.min(fileKeys.size(), files.size());
            for (int i = 0; i < minSize; i++) {
                if (fileKeys.get(i) != null && files.get(i) != null) {
                    requestBodyBuilder.addFormDataPart(fileKeys.get(i), files.get(i).getName(),
                            RequestBody.create(MediaType.parse(getMediaTypeFromFile(files.get(i).getName())), files.get(i)));
                }
            }
        }
        if (formData != null && !formData.isEmpty()) {
            for (var entry : formData.entrySet()) {
                requestBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue(),
                        RequestBody.create(MediaType.parse("application/json"), entry.getValue()));
            }
        }
        RequestBody requestBody = requestBodyBuilder.build();
        return putCall(url, requestBody, headersMap);
    }

    @Retryable(retryFor = {RetryableHttpServiceException.class, IOException.class, HttpConnectTimeoutException.class,
            HttpTimeoutException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10))
    public String putCall(String url, RequestBody requestBody, Map<String, String> headersMap)
            throws IOException {
        log.info("..calling url with PUT method {} ", url);
        if (requestBody == null) {
            requestBody = RequestBody.create(MediaType.parse("application/json"), "{}");
        }
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headersMap != null && !headersMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .headers(headerBuilder.build())
                .build();
        try (Response response = client.newCall(request).execute()) {
            log.info("Service gave {} for uri {} ", response.code(), url);
            if (RETRYABLE_HTTP_CODES.contains(response.code())) {
                throw new RetryableHttpServiceException(HttpStatus.valueOf(response.code()));
            } else if (!SUCCESS_HTTP_CODES.contains(response.code())) {
                throw new HttpServiceException(HttpStatus.valueOf(response.code()));
            }
            return response.body().string();
        } catch (Exception e) {
            log.error("Got exception for uri {}, exception cause={}, exception message={}. Retrying... ",
                    url, e.getCause(), e.getMessage());
            throw e;
        }
    }
}
