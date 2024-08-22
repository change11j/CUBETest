package com.cube.cubetest.entity;

import com.cube.cubetest.util.TimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "chinese_name")
    private String chineseName;

    private Double rate;

    @JsonIgnore
    private LocalDateTime updateTime;

    @JsonProperty("updateTime")
    public String getFormattedUpdateTime() {
        return TimeUtils.formatLocalDateTime(updateTime);
    }

}
