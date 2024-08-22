package com.cube.cubetest.service;

import com.cube.cubetest.dao.CurrencyDAO;
import com.cube.cubetest.dto.CoinDeskDTO;
import com.cube.cubetest.dto.Result;
import com.cube.cubetest.entity.Currency;
import com.cube.cubetest.util.TimeUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cube.cubetest.util.CurrencyUtils.getChineseName;
import static com.cube.cubetest.util.CurrencyUtils.getRateToTWD;
import static com.cube.cubetest.util.CurrencyUtils.ExchangeRateInfo;

@Service
public class CurrencyService {
    private final WebClient webClient;
    private final String coinDeskApi;
    private final String exchangeRateApi;
    private final ObjectMapper objectMapper;
    private final CurrencyDAO currencyDAO;
    @Autowired
    public CurrencyService(WebClient.Builder webClientBuilder,
                           @Value("${coindesk.api}") String coinDeskApi,
                           @Value("${exchange.rate.api}") String exchangeRateApi,
                           CurrencyDAO currencyDAO) {
        this.webClient = webClientBuilder.build();
        this.coinDeskApi = coinDeskApi;
        this.exchangeRateApi = exchangeRateApi;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.currencyDAO=currencyDAO;
    }

    public Mono<CoinDeskDTO> getCoinDesk(){
        return webClient.get()
                .uri(coinDeskApi)
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

    public Mono<List<Currency>> getConvertedCoinDesk() {
        return Mono.zip(getCoinDesk(), getExchangeRates())
                .flatMap(tuple -> {
                    CoinDeskDTO coinDeskDTO = tuple.getT1();
                    Map<String, ExchangeRateInfo> exchangeRates = tuple.getT2();

                    List<Currency> currencies = new ArrayList<>();
                    LocalDateTime updateTime = TimeUtils.parseUpdateTime(coinDeskDTO.getTime().getUpdatedISO());

                    for (Map.Entry<String, CoinDeskDTO.CurrencyInfo> entry : coinDeskDTO.getBpi().entrySet()) {
                        String currencyCode = entry.getKey();
                        CoinDeskDTO.CurrencyInfo currencyInfo = entry.getValue();

                        Currency currency = new Currency();
                        currency.setCode(currencyCode);
                        currency.setChineseName(getChineseName(currencyCode));


                        double rateToTWD = getRateToTWD(currencyCode, exchangeRates);


                        currency.setRate(rateToTWD);
                        currency.setUpdateTime(updateTime);

                        currencies.add(currency);
                    }

                    return Mono.just(currencies);
                });
    }

    public Mono<List<Currency>> saveConvertedCoinDesk() {
        return getConvertedCoinDesk()
                .flatMap(currencies -> {
                    System.out.println("Currencies to save: " + currencies);
                    return Mono.fromCallable(() -> currencyDAO.saveAll(currencies));
                })
                .map(savedCurrencies -> {
                    List<Currency> savedList = new ArrayList<>();
                    savedCurrencies.forEach(savedList::add);
                    return savedList;
                });
    }
    public Mono<Map<String, ExchangeRateInfo>> getExchangeRates() {
        return webClient.get()
                .uri(exchangeRateApi)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        return objectMapper.readValue(response, new TypeReference<Map<String, ExchangeRateInfo>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing exchange rate data", e);
                    }
                });
    }

    public List<Currency> getAllCurrency(){
        return currencyDAO.findAll();
    }

    public Currency saveCurrency(Currency currency){
        return currencyDAO.save(currency);
    }
    @Transactional
    public Result updateCurrency(Currency currency){
        if(currencyDAO.existsById(currency.getCid())){
            currency.setUpdateTime(LocalDateTime.now());
            return new Result(200,currencyDAO.save(currency));
        }
        return new Result(200,"幣別不存在");
    }
    @Transactional
    public Result deleteCurrency(String code){
         currencyDAO.deleteByCode(code);
         return new Result(200);
    }





}
