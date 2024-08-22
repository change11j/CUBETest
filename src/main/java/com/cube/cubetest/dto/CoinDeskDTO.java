package com.cube.cubetest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinDeskDTO {
    private TimeInfo time;
    private String disclaimer;
    private String chartName;
    private Map<String, CurrencyInfo> bpi;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeInfo {
        private String updated;
        private String updatedISO;
        private String updateduk;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrencyInfo {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        private BigDecimal rate_float;
    }
}