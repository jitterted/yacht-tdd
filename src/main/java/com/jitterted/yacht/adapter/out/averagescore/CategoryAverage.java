package com.jitterted.yacht.adapter.out.averagescore;

public class CategoryAverage {
    private String category;
    private double average;

    public CategoryAverage() {
    }

    public CategoryAverage(String category, double average) {
        this.category = category;
        this.average = average;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getCategory() {
        return category;
    }

    public double getAverage() {
        return average;
    }
}