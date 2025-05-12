package com.algoanalyzer.problem.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Problem {
    private final Long problemId;
    private final String title;
    private final String description;
    private final List<String> tags;
    private final String timeLimit;
    private final String memoryLimit;
}
