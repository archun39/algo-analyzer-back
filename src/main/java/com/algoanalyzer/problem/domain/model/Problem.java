package com.algoanalyzer.problem.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    private Long problemId;
    private String title;
    private String description;
    private String input;
    private String output;
    private List<String> tags;
    private String timeLimit;
    private String memoryLimit;
}
