package com.qianlima.reptile.statistics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PublishRateDto {

    private String date;

    private double rate;

    private Integer publishAmount;

    private Integer collectionAmount;

    public PublishRateDto() {
    }

    public PublishRateDto(String date, Integer publishAmount, Integer collectionAmount, double rate) {
        this.date = date;
        this.publishAmount = publishAmount;
        this.collectionAmount = collectionAmount;
        this.rate = rate;
    }
}
