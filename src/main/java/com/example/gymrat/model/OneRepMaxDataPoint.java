package com.example.gymrat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class OneRepMaxDataPoint {
    private LocalDate date;
    private double oneRepMax;
}

