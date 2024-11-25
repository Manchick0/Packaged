package com.manchick.packaged;

import java.util.*;

/**
 * The {@code Request} class represents an HTTP request with a specific method, headers, and body.
 * This class provides a flexible builder pattern to construct HTTP requests with various configurations.
 */
public class Request {

    public static final Request GET = Request.method(RequestMethod.GET).build();

    public final RequestMethod method;
    public final String body;
    public final Map<String, Set<String>> headers;

    private Request(RequestMethod method, Map<String, Set<String>> headers, String body) {
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    public static Builder method(String method) {
        return new Builder().method(method);
    }

    public static Builder method(RequestMethod method) {
        return new Builder().method(method);
    }

    public static Builder header(String header) {
        return new Builder().header(header);
    }

    public static Builder header(String name, String value) {
        return new Builder().header(name, value);
    }

    public static Builder header(String name, String value, boolean overwrite) {
        return new Builder().header(name, value, overwrite);
    }

    public static Builder accept(String accept) {
        return new Builder().accept(accept);
    }

    public static Builder accept(String... accept) {
        return new Builder().accept(accept);
    }

    public static Builder acceptJson() {
        return new Builder().acceptJson();
    }

    public static Builder acceptXml() {
        return new Builder().acceptXml();
    }

    public static Builder acceptText() {
        return new Builder().acceptText();
    }

    public static Builder acceptHtml() {
        return new Builder().acceptHtml();
    }

    public static Builder acceptCss() {
        return new Builder().acceptCss();
    }

    public static Builder acceptForm() {
        return new Builder().acceptForm();
    }

    public static Builder json(String json) {
        return new Builder().json(json);
    }

    public static Builder xml(String xml) {
        return new Builder().xml(xml);
    }

    public static Builder text(String text) {
        return new Builder().text(text);
    }

    public static Builder html(String html) {
        return new Builder().html(html);
    }

    public static Builder css(String css) {
        return new Builder().css(css);
    }

    public static Builder form(String form) {
        return new Builder().form(form);
    }

    public static Builder body(String body) {
        return new Builder().body(body);
    }

    public static class Builder {

        public static final String DEFAULT_USER_AGENT = "Packaged/1.0";
        
        public static final String JSON = "application/json";
        public static final String XML = "application/xml";
        public static final String TEXT = "text/plain";
        public static final String HTML = "text/html";
        public static final String CSS = "text/css";
        public static final String FORM = "application/x-www-form-urlencoded";

        private RequestMethod method;
        private String body;
        private final Map<String, Set<String>> headers;

        private Builder() {
            this.method = RequestMethod.GET;
            this.headers = new HashMap<>();
            this.body = "";

            this.header("User-Agent", DEFAULT_USER_AGENT);
        }

        /**
         * Sets the HTTP request method for the builder using a string representation.
         *
         * @param method The HTTP request method to be set (e.g., GET, POST, PUT).
         * @return The builder instance with the specified method set.
         */
        public Builder method(String method) {
            return this.method(RequestMethod.fromString(method));
        }

        /**
         * Sets the HTTP request method for the builder.
         *
         * @param method The HTTP request method to be set (e.g., GET, POST, PUT).
         * @return The builder instance with the specified method set.
         */
        public Builder method(RequestMethod method) {
            this.method = method;
            return this;
        }

        /**
         * Adds a header to the request by parsing a header line in the format "Name: Value".
         * If the header line does not contain a colon, the header is ignored.
         * <p>
         * This method splits the header at the position of the first semicolon. If your header
         * contains a semicolon elsewhere, delegate to the {@link #header(String, String)} method instead.
         *
         * @param header The header line in the format "Name: Value".
         * @return The builder instance with the added header.
         */
        public Builder header(String header) {
            var in = header.indexOf(":");
            if (in == -1) {
                return this;
            }
            return this.header(header.substring(0, in).trim(), header.substring(in + 1).trim());
        }

        /**
         * Adds a header to the request with the specified name and value.
         *
         * @param name  The name of the header.
         * @param value The value of the header.
         * @return The builder instance with the added header.
         */
        public Builder header(String name, String value) {
            return this.header(name, value, false);
        }

        /**
         * Adds a header to the request. If the header already exists and overwrite is true, it replaces the existing value.
         * Otherwise, it appends the new value to the existing header.
         *
         * @param name The name of the header.
         * @param value The value of the header.
         * @param overwrite Whether to overwrite the existing header value if it already exists.
         * @return The builder instance with the added or updated header.
         */
        public Builder header(String name, String value, boolean overwrite) {
            if (overwrite) {
                this.headers.put(name, Set.of(value));
                return this;
            }
            this.headers.computeIfAbsent(name, k -> new HashSet<>()).add(value);
            return this;
        }

        /**
         * Sets the "Accept" header for the HTTP request.
         *
         * @param accept The value to set for the "Accept" header.
         * @return The builder instance with the added "Accept" header.
         */
        public Builder accept(String accept) {
            return this.header("Accept", accept);
        }

        /**
         * Adds multiple values to the "Accept" header for the HTTP request.
         *
         * @param accept The array of values to set for the "Accept" header.
         * @return The builder instance with the added "Accept" headers.
         */
        public Builder accept(String... accept) {
            Arrays.stream(accept).forEach(this::accept);
            return this;
        }

        /**
         * Configures the builder to accept JSON responses by setting the "Accept" header to "application/json".
         *
         * @return The builder instance with the "Accept" header set to "application/json".
         */
        public Builder acceptJson() {
            return this.accept(JSON);
        }

        /**
         * Configures the builder to accept XML responses by setting the "Accept" header to "application/xml".
         *
         * @return The builder instance with the "Accept" header set to "application/xml".
         */
        public Builder acceptXml() {
            return this.accept(XML);
        }

        /**
         * Configures the builder to accept text responses by setting the "Accept" header to "text/plain".
         *
         * @return The builder instance with the "Accept" header set to "text/xml"
         * */
        public Builder acceptText() {
            return this.accept(TEXT);
        }

        /**
         * Configures the builder to accept HTML responses by setting the "Accept" header to "text/html".
         *
         * @return The builder instance with the "Accept" header set to "text/html".
         */
        public Builder acceptHtml() {
            return this.accept(HTML);
        }

        /**
         * Configures the builder to accept CSS responses by setting the "Accept" header to "text/css".
         *
         * @return The builder instance with the "Accept" header set to "text/css".
         */
        public Builder acceptCss() {
            return this.accept(CSS);
        }

        /**
         * Configures the builder to accept form data responses by setting
         * the "Accept" header to "application/x-www-form-urlencoded".
         *
         * @return The builder instance with the "Accept" header set to "application/x-www-form-urlencoded".
         */
        public Builder acceptForm() {
            return this.accept(FORM);
        }

        /**
         * Sets the request body to the given JSON string and adds the "Content-Type" header as "application/json".
         *
         * @param json The JSON string to set as the request body.
         * @return The builder instance with the JSON body and the "Content-Type" header.
         */
        public Builder json(String json) {
            return this.body(json).header("Content-Type", JSON, true);
        }

        /**
         * Sets the request body to the given XML string and adds the "Content-Type" header as "application/xml".
         *
         * @param xml The XML string to set as the request body.
         * @return The builder instance with the XML body and the "Content-Type" header.
         */
        public Builder xml(String xml) {
            return this.body(xml).header("Content-Type", XML, true);
        }

        /**
         * Sets the request body to the given text string and adds the "Content-Type" header as "text/plain".
         *
         * @param text The text string to set as the request body.
         * @return The builder instance with the text body and the "Content-Type" header.
         */
        public Builder text(String text) {
            return this.body(text).header("Content-Type", TEXT, true);
        }

        /**
         * Sets the request body to the given HTML string and adds the "Content-Type" header as "text/html".
         *
         * @param html The HTML string to set as the request body.
         * @return The builder instance with the HTML body and the "Content-Type" header.
         */
        public Builder html(String html) {
            return this.body(html).header("Content-Type", HTML, true);
        }

        /**
         * Sets the request body to the given CSS string and adds the "Content-Type" header as "text/css".
         *
         * @param css The CSS string to set as the request body.
         * @return The builder instance with the CSS body and the "Content-Type" header.
         */
        public Builder css(String css) {
            return this.body(css).header("Content-Type", CSS, true);
        }

        /**
         * Sets the request body to the given form string and adds the "Content-Type" header as "application/x-www-form-urlencoded".
         *
         * @param form The form string to set as the request body.
         * @return The builder instance with the form body and the "Content-Type" header.
         */
        public Builder form(String form) {
            return this.body(form).header("Content-Type", FORM, true);
        }

        /**
         * Sets the request body and adds the "Content-Length" header.
         *
         * @param body The content to set as the request body.
         * @return The builder instance with the set body and added "Content-Length" header.
         */
        public Builder body(String body) {
            this.body = body;
            return this.header("Content-Length", String.valueOf(body.length()), true);
        }

        public Request build() {
            return new Request(method, headers, body);
        }
    }
}
