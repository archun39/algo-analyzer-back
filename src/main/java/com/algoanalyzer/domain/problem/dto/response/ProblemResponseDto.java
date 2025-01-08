package com.algoanalyzer.domain.problem.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class ProblemResponseDto {
    private Long problem_id;
    private String title;
    private String description;
    private String input;
    private String output;
    private String time_limit;
    private String memory_limit;
    private List<String> tags;
} 