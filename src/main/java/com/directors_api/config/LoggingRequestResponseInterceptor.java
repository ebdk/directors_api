package com.directors_api.config;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import java.io.IOException;

public class LoggingRequestResponseInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        try {
            // Log the request details
            System.out.println("Request: " + request.getMethod() + " " + request.getURI());

            // Execute the request
            ClientHttpResponse response = execution.execute(request, body);

            // Log the response details
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Content-Type: " + response.getHeaders().getContentType());

            return response;
        } catch (IOException e) {
            System.err.println("IOException while processing request/response: " + e.getMessage());
            throw e;  // Re-throw the exception
        }
    }
}
