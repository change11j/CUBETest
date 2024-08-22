package com.cube.cubetest.controller;

import com.cube.cubetest.dto.CoinDeskDTO;
import com.cube.cubetest.dto.Result;
import com.cube.cubetest.entity.Currency;
import com.cube.cubetest.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CurrencyControllerTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCoinDesk() {
        CoinDeskDTO coinDeskDTO = new CoinDeskDTO();
        when(currencyService.getCoinDesk()).thenReturn(Mono.just(coinDeskDTO));

        StepVerifier.create(currencyController.getCoinDesk())
                .expectNext(coinDeskDTO)
                .verifyComplete();
    }

    @Test
    void getConvertedCoinDesk() {
        List<Currency> currencies = Arrays.asList(new Currency(), new Currency());
        when(currencyService.getConvertedCoinDesk()).thenReturn(Mono.just(currencies));

        StepVerifier.create(currencyController.getConvertedCoinDesk())
                .expectNext(currencies)
                .verifyComplete();
    }

    @Test
    void saveConvertedCoinDesk() {
        List<Currency> savedCurrencies = Arrays.asList(new Currency(), new Currency());
        when(currencyService.saveConvertedCoinDesk()).thenReturn(Mono.just(savedCurrencies));

        StepVerifier.create(currencyController.saveConvertedCoinDesk())
                .expectNext(ResponseEntity.ok(savedCurrencies))
                .verifyComplete();
    }

    @Test
    void getAllCurrency() {
        List<Currency> currencies = Arrays.asList(new Currency(), new Currency());
        when(currencyService.getAllCurrency()).thenReturn(currencies);

        assertEquals(currencies, currencyController.getAllCurrency());
    }

    @Test
    void saveCurrency() {
        Currency currency = new Currency();
        when(currencyService.saveCurrency(any(Currency.class))).thenReturn(currency);

        Currency result = currencyController.saveCurrency(currency);
        assertEquals(currency, result);
        verify(currencyService).saveCurrency(argThat(c -> c.getUpdateTime() != null));
    }

    @Test
    void updateCurrency() {
        Currency currency = new Currency();
        Result expectedResult = new Result();
        when(currencyService.updateCurrency(currency)).thenReturn(expectedResult);

        Result result = currencyController.updateCurrency(currency);
        assertEquals(expectedResult, result);
    }

    @Test
    void deleteCurrency() {
        String code = "USD";
        Result expectedResult = new Result();
        when(currencyService.deleteCurrency(code)).thenReturn(expectedResult);

        Result result = currencyController.deleteCurrency(code);
        assertEquals(expectedResult, result);
    }
}