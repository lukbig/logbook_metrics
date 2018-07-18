package com.bigos.logbookmetricsdemo;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;

import java.util.List;

import static org.zalando.logbook.Conditions.*;

@Configuration
public class LogbookConfiguration {

    @Bean
    public Logbook logbook(
            final List<RawRequestFilter> rawRequestFilters,
            final List<RawResponseFilter> rawResponseFilters,
            final List<HeaderFilter> headerFilters,
            final List<QueryFilter> queryFilters,
            final List<BodyFilter> bodyFilters,
            final List<RequestFilter> requestFilters,
            final List<ResponseFilter> responseFilters) {
        return Logbook.builder()
                .rawRequestFilters(rawRequestFilters)
                .headerFilters(headerFilters)
                .queryFilters(queryFilters)
                .bodyFilters(bodyFilters)
                .requestFilters(requestFilters)
                .responseFilters(responseFilters)
                .writer(new DefaultHttpLogWriter(LoggerFactory.getLogger(Logbook.class), DefaultHttpLogWriter.Level.INFO))
                .formatter(new LogbookShortLogFormatter())
                .condition(exclude(
                        requestTo("**/css/**"),
                        requestTo("**/js/**"),
                        requestTo("**/images/**"),
                        requestTo("**/font/**"),
                        requestTo("**/libsIE/**"),
                        requestTo("**/*.html"),
                        requestTo("**/*.png"),
                        requestTo("/actuator/info")))
                .rawResponseFilters(rawResponseFilters)
                .build();
    }
}
