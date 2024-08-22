package com.cube.cubetest.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;
@Component
public class CurrencyUtils {
    private static final Map<String, String> CURRENCY_CHINESE_NAMES = new HashMap<>();

    static {
//        可在此繼續擴充
        CURRENCY_CHINESE_NAMES.put("USD", "美元");
        CURRENCY_CHINESE_NAMES.put("EUR", "歐元");
        CURRENCY_CHINESE_NAMES.put("JPY", "日元");
        CURRENCY_CHINESE_NAMES.put("GBP", "英鎊");

    }

    public static String getChineseName(String code) {
        return CURRENCY_CHINESE_NAMES.getOrDefault(code, code);
    }

    public static double getRateToTWD(String currencyCode, Map<String, ExchangeRateInfo> exchangeRates) {
//            因為我找到的api格式都是usdtwd/usdrmb等對美金的匯率 所以以下以美金做中介轉換

        String key = "USD" + currencyCode;
        if (exchangeRates.containsKey(key)) {
            return 1 / exchangeRates.get(key).getExrate() * exchangeRates.get("USDTWD").getExrate();
        } else {

            return exchangeRates.get("USDTWD").getExrate() / exchangeRates.get("USD" + currencyCode).getExrate();
        }
    }
    @Data
    public static class ExchangeRateInfo {
        @JsonProperty("Exrate")
        private double exrate;

        @JsonProperty("UTC")
        private String utc;


    }
}