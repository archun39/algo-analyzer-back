package com.algoanalyzer.domain.problem.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class SolvedAcProblemResponse {
    @JsonProperty("problemId")
    private int problemId;
    
    @JsonProperty("titleKo")
    private String titleKo;
    
    @JsonProperty("level")
    private Integer level;
    
    @JsonProperty("tags")
    private List<ProblemTag> tags;
    
    @JsonProperty("averageTries")
    private Double averageTries;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("input")
    private String input;
    
    @JsonProperty("output")
    private String output;
    
    @JsonProperty("timeLimit")
    private String timeLimit;
    
    @JsonProperty("memoryLimit")
    private String memoryLimit;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ProblemTag {
        @JsonProperty("key")
        private String key;
        
        @JsonProperty("displayNames")
        private List<DisplayName> displayNames;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DisplayName {
        @JsonProperty("language")
        private String language;
        
        @JsonProperty("name")
        private String name;
    }
} 