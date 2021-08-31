package com.nekoimi.boot.modules.demo.controller;

import com.nekoimi.boot.framework.http.JsonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * nekoimi  2021/7/21 上午10:22
 */
@RestController
public class DemoController {

    @GetMapping("/")
    public Mono<JsonResponse> index() {
        return JsonResponse.ok("hello world");
    }

    @GetMapping("/ajax_test")
    public Mono<JsonResponse> ajaxTest() {
        return JsonResponse.ok(Mono.just(Map.of("a", "A", "b", "B", "c", "C")));
    }

}
