package com.cube.cubetest.service;

import com.cube.cubetest.dto.CoinDeskDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CurrencyService {
    private final WebClient webClient;
    private String api;

    public CurrencyService(WebClient webClient, @Value("${coindesk.api}") String api) {
        this.webClient = webClient;
        this.api = api;
    }

    public Mono<CoinDeskDTO> getCoinDesk(){
        return webClient.get()
                .uri(api)
                .retrieve()
                .bodyToMono(CoinDeskDTO.class)
                .doOnNext(response->{
                    System.out.println("Response : "+response);
                })
                .onErrorResume(error -> {
                    System.err.println("Error occurred: " + error.getMessage());
                    return Mono.empty();
                });
    }

}
