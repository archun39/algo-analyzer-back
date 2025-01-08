package com.algoanalyzer.domain.analysis.problem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.algoanalyzer.domain.analysis.problem.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemAnalysisService {
    
    @Value("${python.api.base-url}")
    private String pythonApiBaseUrl;
    
    private final RestTemplate restTemplate;
    
    public ProblemAnalysisResponseDto analyzeProblem(ProblemAnalysisRequestDto request) {
        try {
            log.info("문제 분석 요청: problemId={}", request.getProblemId());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<ProblemAnalysisRequestDto> entity = new HttpEntity<>(request, headers);
            
            String url = pythonApiBaseUrl + "/api/analyze/problem";
            
            return restTemplate.postForObject(url, entity, ProblemAnalysisResponseDto.class);
            
        } catch (Exception e) {
            log.error("문제 분석 실패", e);
            throw new RuntimeException("문제 분석에 실패했습니다: " + e.getMessage());
        }
    }
} 