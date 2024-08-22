package com.cube.cubetest.controller;

import com.cube.cubetest.dto.CoinDeskDTO;
import com.cube.cubetest.dto.Result;
import com.cube.cubetest.entity.Currency;
import com.cube.cubetest.service.CurrencyService;
import com.cube.cubetest.util.TimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    void testGetAllCurrency() {
        // 1. 測試呼叫查詢幣別對應表資料 API,並顯示其內容
        List<Currency> currencies = Arrays.asList(
                new Currency(1L, "USD", "美元", 1.0, LocalDateTime.now()),
                new Currency(2L, "EUR", "歐元", 0.85, LocalDateTime.now())
        );
        when(currencyService.getAllCurrency()).thenReturn(currencies);

        List<Currency> result = currencyController.getAllCurrency();
        assertEquals(currencies, result);
        System.out.println("查詢幣別對應表資料結果：" + result);

    }

    @Test
    void testSaveCurrency() {
        // 2. 測試呼叫新增幣別對應表資料 API
        Currency newCurrency = new Currency(null, "JPY", "日圓", 110.0, LocalDateTime.now());
        when(currencyService.saveCurrency(any(Currency.class))).thenReturn(newCurrency);

        Currency result = currencyController.saveCurrency(newCurrency);
        assertEquals(newCurrency, result);
        verify(currencyService).saveCurrency(argThat(c -> c.getCode().equals("JPY")));
    }

    @Test
    void testUpdateCurrency() {
        // 3. 測試呼叫更新幣別對應表資料 API,並顯示其內容
        Currency updatedCurrency = new Currency(1L, "USD", "美元", 1.1, LocalDateTime.now());
        Result updateResult = new Result(200, "更新成功");
        when(currencyService.updateCurrency(updatedCurrency)).thenReturn(updateResult);

        Result result = currencyController.updateCurrency(updatedCurrency);
        assertEquals(updateResult, result);
        System.out.println("更新幣別對應表資料結果：" + result);
    }

    @Test
    void testDeleteCurrency() {
        // 4. 測試呼叫刪除幣別對應表資料 API
        String currencyCode = "GBP";
        Result deleteResult = new Result(200, "刪除成功");
        when(currencyService.deleteCurrency(currencyCode)).thenReturn(deleteResult);

        Result result = currencyController.deleteCurrency(currencyCode);
        assertEquals(deleteResult, result);
    }

    @Test
    void testGetCoinDesk() {
        // 5. 測試呼叫 coindesk API,並顯示其內容
        CoinDeskDTO coinDeskDTO = new CoinDeskDTO();
        CoinDeskDTO.TimeInfo timeInfo = new CoinDeskDTO.TimeInfo("May 20, 2023 12:00:00 UTC", "2023-05-20T12:00:00+00:00", "May 20, 2023 at 12:00 BST");
        coinDeskDTO.setTime(timeInfo);
        coinDeskDTO.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD).");
        coinDeskDTO.setChartName("Bitcoin");

        Map<String, CoinDeskDTO.CurrencyInfo> bpi = new HashMap<>();
        bpi.put("USD", new CoinDeskDTO.CurrencyInfo("USD", "&#36;", "30,000.0000", "United States Dollar", new BigDecimal("30000.0000")));
        coinDeskDTO.setBpi(bpi);

        when(currencyService.getCoinDesk()).thenReturn(Mono.just(coinDeskDTO));

        StepVerifier.create(currencyController.getCoinDesk())
                .expectNextMatches(result -> {
                    System.out.println("Coindesk API 結果：" + result);
                    return result.getTime().getUpdated().equals("May 20, 2023 12:00:00 UTC") &&
                            result.getBpi().get("USD").getCode().equals("USD") &&
                            result.getBpi().get("USD").getRate_float().compareTo(new BigDecimal("30000.0000")) == 0;
                })
                .verifyComplete();
    }

    @Test
    void testGetConvertedCoinDesk() {
        // 6. 測試呼叫資料轉換的 API,並顯示其內容
        List<Currency> convertedCurrencies = Arrays.asList(
                new Currency(1L, "USD", "美元", 30000.0, LocalDateTime.now()),
                new Currency(2L, "EUR", "歐元", 25500.0, LocalDateTime.now())
        );
        when(currencyService.getConvertedCoinDesk()).thenReturn(Mono.just(convertedCurrencies));

        StepVerifier.create(currencyController.getConvertedCoinDesk())
                .expectNextMatches(result -> {
                    System.out.println("資料轉換 API 結果：" + result);
                    return result.size() == 2 &&
                            result.get(0).getCode().equals("USD") &&
                            result.get(1).getCode().equals("EUR");
                })
                .verifyComplete();
    }
}