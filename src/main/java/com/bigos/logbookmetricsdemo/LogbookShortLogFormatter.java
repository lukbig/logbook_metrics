package com.bigos.logbookmetricsdemo;

import org.zalando.logbook.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static org.zalando.logbook.Origin.REMOTE;

public class LogbookShortLogFormatter implements HttpLogFormatter {

    @Override
    public String format(final Precorrelation<HttpRequest> precorrelation) throws IOException {
        return format(prepare(precorrelation));
    }

    /**
     * Produces an HTTP-like request in individual lines.
     *
     * @param precorrelation the request correlation
     * @return a line-separated HTTP request
     * @throws IOException
     * @see #prepare(Correlation)
     * @see #format(List)
     * @see JsonHttpLogFormatter#prepare(Precorrelation)
     */
    public List<String> prepare(final Precorrelation<HttpRequest> precorrelation) throws IOException {
        final HttpRequest request = precorrelation.getRequest();
        final String requestLine = prepareRequestLine(request.getOrigin(), request, precorrelation.getId());
        return prepare(request, requestLine);
    }

    private String prepareRequestLine(Origin origin, HttpRequest request, String id) {
        return String.format("%s %s %s %s", id.substring(0, 6), direction(origin), request.getMethod(), request.getRequestUri());
    }

    @Override
    public String format(final Correlation<HttpRequest, HttpResponse> correlation) throws IOException {
        return format(prepare(correlation));
    }

    /**
     * Produces an HTTP-like response in individual lines.
     *
     * Pr@param correlation the response correlation
     * @return a line-separated HTTP response
     * @throws IOException
     * @see #prepare(Precorrelation)
     * @see #format(List)
     * @see JsonHttpLogFormatter#prepare(Correlation)
     */
    public List<String> prepare(final Correlation<HttpRequest, HttpResponse> correlation) throws IOException {
        final HttpResponse response = correlation.getResponse();
        final int status = response.getStatus();
        final String requestLine = prepareRequestLine(response.getOrigin(), correlation.getRequest(), correlation.getId());

        final String statusLine = String.format("%s %d %s", requestLine, status, correlation.getDuration().toMillis() + "ms").trim();
        return prepare(response, statusLine);
    }

    private <H extends HttpMessage> List<String> prepare(final H message, final String firstLine) throws IOException {
        final List<String> lines = new ArrayList<>();

        lines.add(firstLine);

        final String body = message.getBodyAsString();

        if (!body.isEmpty()) {
            lines.add(body);
            lines.add("");
        }

        return lines;
    }

    private String direction(final Origin origin) {
        return origin == REMOTE ? ">>" : "<<";
    }

    /**
     * Renders an HTTP-like message into a printable string.
     *
     * @param lines lines of an HTTP message
     * @return the whole message as a single string, separated by new lines
     * @see #prepare(Precorrelation)
     * @see #prepare(Correlation)
     * @see JsonHttpLogFormatter#format(Map)
     */
    public String format(final List<String> lines) {
        return lines.stream().collect(joining("\n"));
    }
}
