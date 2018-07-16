package com.bigos.logbookmetricsdemo;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;

import java.util.List;
import java.util.function.Predicate;

@Configuration
public class LogbookConfiguration {

    @Bean
    public Logbook logbook(
            final Predicate<RawHttpRequest> condition,
            final List<RawRequestFilter> rawRequestFilters,
            final List<RawResponseFilter> rawResponseFilters,
            final List<HeaderFilter> headerFilters,
            final List<QueryFilter> queryFilters,
            final List<BodyFilter> bodyFilters,
            final List<RequestFilter> requestFilters,
            final List<ResponseFilter> responseFilters) {
        return Logbook.builder()
                .condition(condition)
                .rawRequestFilters(rawRequestFilters)
                .rawResponseFilters(rawResponseFilters)
                .headerFilters(headerFilters)
                .queryFilters(queryFilters)
                .bodyFilters(bodyFilters)
                .requestFilters(requestFilters)
                .responseFilters(responseFilters)
                .writer(new DefaultHttpLogWriter(LoggerFactory.getLogger(Logbook.class), DefaultHttpLogWriter.Level.INFO))
                .formatter(new DefaultHttpLogFormatter())
//                .formatter(new JsonHttpLogFormatter())
//                .formatter(new CurlHttpLogFormatter())
//                .formatter(new LogbookShortLogFormatter())
                .build();
    }
}
