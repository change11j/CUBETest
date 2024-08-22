package com.cube.cubetest.controller;

import com.cube.cubetest.dto.CoinDeskDTO;
import com.cube.cubetest.dto.Result;
import com.cube.cubetest.entity.Currency;
import com.cube.cubetest.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/coindesk")
    public Mono<CoinDeskDTO> getCoinDesk(){
        return currencyService.getCoinDesk();
    }

    @GetMapping("/convertedcoindesk")
    public Mono<List<Currency>> getConvertedCoinDesk(){
        return  currencyService.getConvertedCoinDesk();
    }

    @PostMapping("/convertedcoindesk")
    public Mono<ResponseEntity<List<Currency>>> saveConvertedCoinDesk() {
        return currencyService.saveConvertedCoinDesk()
                .map(savedCurrencies -> ResponseEntity.ok(savedCurrencies))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
    @GetMapping("/allcurrencies")
    public List<Currency> getAllCurrency(){
        return currencyService.getAllCurrency();
    }
    @PostMapping("/currency")
    public Currency saveCurrency(@RequestBody Currency currency){
        currency.setUpdateTime(LocalDateTime.now());
        return currencyService.saveCurrency(currency);
    }
    @PutMapping("/currency")
    public Result updateCurrency(@RequestBody Currency currency){
        return currencyService.updateCurrency(currency);
    }
    @DeleteMapping("/currency/{code}")
    public Result deleteCurrency(@PathVariable String code){
        return currencyService.deleteCurrency(code);
    }

}
