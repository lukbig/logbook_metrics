package com.bigos.logbookmetricsdemo;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * curl localhost:8080
 * curl -X POST localhost:8080 -d '{"xd": "xd"}' -H "content-type: application/json"
 * curl localhost:8080/actuator/info
 */

@RestController
@RequestMapping("/")
public class DemoController {

    @GetMapping
    public Map<String, String> getMethod() {
        return Collections.singletonMap("hello", "there from get");
    }

    @PostMapping
    public Map<String, String> postMethod(@RequestBody Map<String, Object> body) {
        return Collections.singletonMap("hello", "there from post");
    }
}
