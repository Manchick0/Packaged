package com.manchick.packaged;

import java.util.Arrays;

public enum RequestMethod {

    GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE, PATCH, CONNECT;

    /**
     * Determines if the HTTP request method relies on having a body.
     *
     * @return true if the HTTP request method is POST, PUT, or PATCH; otherwise, false.
     */
    boolean reliesOnBody() {
        return this == POST || this == PUT || this == PATCH;
    }

    public static RequestMethod fromString(String name) {
        return Arrays.stream(RequestMethod.values())
                .filter(method -> method.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown request method «%s».", name)));
    }

    @Override
    public String toString() {
        return this.name();
    }
}
