package com.algoanalyzer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Problem {
    @Id
    private Long problemId;
    private String title;
    private String level;
    private String tags;
    private Double averageTries;
} 