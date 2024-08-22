package com.cube.cubetest.controller;

import com.cube.cubetest.dto.CoinDeskDTO;
import com.cube.cubetest.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
@AllArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/coindesk")
    public Mono<CoinDeskDTO> getCoindesk(){
        return currencyService.getCoinDesk();
    }

}
