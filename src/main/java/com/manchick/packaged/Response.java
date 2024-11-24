package com.manchick.packaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@code Response} class represents an HTTP response received from a server.
 * It contains the status code, status text, response body, headers, and the URL of the request.
 * It provides utility methods to access the response data and parse the body content.
 */
public class Response {

    public final short status;
    public final String statusText;
    public final boolean ok;
    public final URL url;

    private final String body;
    private final Map<String, Set<String>> headers;

    public Response(short status, String statusText, String body, URL url, Map<String, Set<String>> headers) {
        this.status = status;
        this.statusText = statusText;
        this.ok = status >= 200 && status < 300;
        this.headers = headers;

        this.body = body;
        this.url = url;
    }

    /**
     * Converts the response to an object of type T using the provided parser function.
     * <p>
     * It's designed to be used with a JSON library to turn the body of the response
     * into an object.
     *
     * @param <T> The type of the object to deserialize to.
     * @param parser A function that takes a string representation of the response and returns an object of type T.
     * @return The deserialized object of type T.
     */
    public <T> T parse(Function<String, T> parser) {
        return parser.apply(text());
    }

    /**
     * Retrieves the value of the specified header from the HTTP response.
     *
     * @param name The name of the header to retrieve.
     * @return The value of the specified header, or an empty string if the header is not present.
     */
    public Set<String> headers(String name) {
        return this.headers.getOrDefault(name, Set.of());
    }

    /**
     * Checks if the Content-Type of the HTTP response is "application/json".
     *
     * @return true if the Content-Type header contains "application/json", otherwise false.
     */
    public boolean isOfJson() {
        return this.headers("Content-Type").contains("application/json");
    }

    public boolean isOfXml() {
        return this.headers("Content-Type").contains("application/xml");
    }

    public boolean isOfText() {
        return this.headers("Content-Type").contains("text/plain");
    }

    public boolean isOfHtml() {
        return this.headers("Content-Type").contains("text/html");
    }

    public boolean isOfCss() {
        return this.headers("Content-Type").contains("text/css");
    }

    public boolean isOfForm() {
        return this.headers("Content-Type").contains("application/x-www-form-urlencoded");
    }

    /**
     * Returns the body content of the HTTP response as a string.
     *
     * @return The body content of the response.
     */
    public String text() {
        return this.body;
    }

    static Response fromConnection(HttpURLConnection connection) {
        try(var reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            var body = reader.lines().reduce("", (a, b) -> a + '\n' + b);
            return new Response((short) connection.getResponseCode(),
                    connection.getResponseMessage(),
                    body.startsWith("\n") ? body.substring(1) : body,
                    connection.getURL(),
                    connection.getHeaderFields().entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, e -> new HashSet<>(e.getValue()))));
        } catch (UnknownHostException e) {
            return new Response((short) 404, "Unknown host", "", connection.getURL(), Map.of());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("Response{status=%d, statusText=%s, ok=%b, url=%s, headers=%s}",
                status, statusText, ok, url, Arrays.toString(headers.entrySet().toArray()));
    }
}
