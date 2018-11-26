package com.appcnd.find.api.pojo.vo;

import lombok.Data;

/**
 * Created by nihao on 17/9/8.
 */
@Data
public class FaceVO {
    private String gender;
    private Double genderProbability;
    private Integer age;
    private String beauty;
    private String expression;
    private Double expressionProbablity;
    private String glasses;
    private Double glassesProbability;
    private String race;
    private Double raceProbability;
    private Double human;
    private Double cartoon;
    private Integer left;
    private Integer top;
    private Integer width;
    private Integer height;
    private Integer picW;
    private Integer picH;

    public FaceVO setPicW(Integer picW) {
        this.picW = picW;
        return this;
    }

    public FaceVO setPicH(Integer picH) {
        this.picH = picH;
        return this;
    }
}
