package com.services;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Locale;

public class HTTPResponses {

    public static Response ok(Object entity) {
        return getResponse(entity, HttpServletResponse.SC_OK, MediaType.APPLICATION_JSON_TYPE);
    }
    public static Response ok(Object entity, MediaType contentType) {
        return getResponse(entity, HttpServletResponse.SC_OK, contentType);
    }

    public static Response notFound(String message) {
        return getResponse(createResponseMessageJson(message, "message"), HttpServletResponse.SC_NOT_FOUND, MediaType.APPLICATION_JSON_TYPE);
    }

    public static Response badRequest() {
        return badRequest("Invalid request");
    }

    public static Response badRequest(String message) {
        return getResponse(createResponseMessageJson("error", message), HttpServletResponse.SC_BAD_REQUEST, MediaType.APPLICATION_JSON_TYPE);
    }

    private static Response getResponse(final Object entity, final int status, final MediaType contentType) {
        return Response.status(status).entity(entity).language(Locale.UK).type(contentType).build();
    }

    private static String createResponseMessageJson(final String message, final String key) {
        return "{\"" + key + "\":\"" + message + "\"}";
    }
}
