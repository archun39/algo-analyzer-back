package com.algoanalyzer.domain.analysis.problem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.algoanalyzer.domain.analysis.problem.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;
import com.algoanalyzer.domain.analysis.problem.model.AnalysisLevel1Document;
import com.algoanalyzer.domain.analysis.problem.repository.AnalysisRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemAnalysisService {
    
    @Value("${python.api.base-url}")
    private String pythonApiBaseUrl;
    
    private final RestTemplate restTemplate;
    private final AnalysisRepository analysisRepository;

    public ProblemAnalysisResponseDto analyzeProblem(ProblemAnalysisRequestDto request) {
        try {
            log.info("문제 분석 요청: problemId={}", request.getProblemId());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<ProblemAnalysisRequestDto> entity = new HttpEntity<>(request, headers);
            
            String url = pythonApiBaseUrl + "/api/analyze/problem";
            
            log.info("FastAPI 요청 URL: {}", url);
            log.debug("요청 데이터: {}", request);
            
            // FastAPI로부터 분석 결과를 받아옴
            ProblemAnalysisResponseDto analysisResult = restTemplate.postForObject(url, entity, ProblemAnalysisResponseDto.class);
            
            log.info("분석 결과 수신: {}", analysisResult);

            AnalysisLevel1Document analysisLevel1Document = AnalysisLevel1Document.builder()
                    .problemId(request.getProblemId())
                    .timeComplexity(analysisResult.getTimeComplexity() != null ? analysisResult.getTimeComplexity() : "기본값")
                    .timeComplexityReasoning(analysisResult.getTimeComplexityReasoning() != null ? analysisResult.getTimeComplexityReasoning() : "기본값")
                    .spaceComplexity(analysisResult.getSpaceComplexity() != null ? analysisResult.getSpaceComplexity() : "기본값")
                    .spaceComplexityReasoning(analysisResult.getSpaceComplexityReasoning() != null ? analysisResult.getSpaceComplexityReasoning() : "기본값")
                    .algorithmType(analysisResult.getAlgorithmType() != null ? analysisResult.getAlgorithmType() : "기본값")
                    .algorithmTypeReasoning(analysisResult.getAlgorithmTypeReasoning() != null ? analysisResult.getAlgorithmTypeReasoning() : "기본값")
                    .dataStructures(analysisResult.getDataStructures() != null ? analysisResult.getDataStructures() : "기본값")
                    .dataStructuresReasoning(analysisResult.getDataStructuresReasoning() != null ? analysisResult.getDataStructuresReasoning() : "기본값")
                    .solutionImplementation(analysisResult.getSolutionImplementation() != null ? analysisResult.getSolutionImplementation() : "기본값")
                    .solutionImplementationReasoning(analysisResult.getSolutionImplementationReasoning() != null ? analysisResult.getSolutionImplementationReasoning() : "기본값")
                    .build();
            
            analysisRepository.save(analysisLevel1Document);
            
            return analysisResult;
            
        } catch (Exception e) {
            log.error("문제 분석 실패", e);
            throw new RuntimeException("문제 분석에 실패했습니다: " + e.getMessage());
        }
    }
}


