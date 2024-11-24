package com.manchick.packaged;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class Packaged {

    public static CompletableFuture<Response> fetch(String URL) {
        return Packaged.fetch(URL, Request.GET);
    }

    public static CompletableFuture<Response> fetch(URI uri) {
        return Packaged.fetch(uri, Request.GET);
    }

    public static CompletableFuture<Response> fetch(URL url) {
        return Packaged.fetch(url, Request.GET);
    }

    public static CompletableFuture<Response> fetch(String URL, Request.Builder builder) {
        return Packaged.fetch(URL, builder.build());
    }

    public static CompletableFuture<Response> fetch(URI uri, Request.Builder builder) {
        return Packaged.fetch(uri, builder.build());
    }

    public static CompletableFuture<Response> fetch(URL url, Request.Builder builder) {
        return Packaged.fetch(url, builder.build());
    }

    public static CompletableFuture<Response> fetch(String URL, Request request) {
        return Packaged.fetch(URI.create(URL), request);
    }

    public static CompletableFuture<Response> fetch(URI uri, Request request) {
        try {
            return Packaged.fetch(uri.toURL(), request);
        } catch (MalformedURLException e) {
            throw new RuntimeException(String.format("Couldn't parse «%s» to a URL.", uri), e);
        }
    }

    /**
     * Asynchronously fetches an HTTP response from the given URL with the specified request.
     *
     * @param url  The URL to fetch the HTTP response from.
     * @param request The body containing the request method and other configurations.
     * @return A {@code CompletableFuture} holding the HTTP {@link Response}.
     */
    public static CompletableFuture<Response> fetch(URL url, Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(request.method.toString());
                request.headers.forEach((name, values) -> {
                    connection.setRequestProperty(name, String.join(", ", values));
                });
                if (request.method.reliesOnBody()) {
                    connection.setDoOutput(true);
                    try(var writer = new PrintWriter(connection.getOutputStream())) {
                        writer.write(request.body);
                    }
                }
                return Response.fromConnection(connection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
