package com.algoanalyzer.domain.analysis.problem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.algoanalyzer.domain.analysis.problem.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;
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

    // 문제 분석 요청
    public ProblemAnalysisResponseDto analyzeProblem(ProblemAnalysisRequestDto request) {
        try {
            log.info("문제 분석 요청: problemId={}", request.getProblemId());

            // DB에서 문제 정보 조회
            ProblemAnalysisResponseDto analysisDocument = analysisRepository.findByProblemId(request.getProblemId());
            if(analysisDocument != null) {
                log.info("문제 분석 결과 DB 조회 완료: problemId={}", request.getProblemId());
                return analysisDocument;
            }
            else {
                log.info("문제 분석 요청: problemId={}", request.getProblemId());
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ProblemAnalysisRequestDto> entity = new HttpEntity<>(request, headers);
            
            String url = pythonApiBaseUrl + "/api/analyze/problem";
            
            log.info("FastAPI 요청 URL: {}", url);
            
            // FastAPI로부터 분석 결과를 받아옴
            ProblemAnalysisResponseDto analysisResult = restTemplate.postForObject(url, entity, ProblemAnalysisResponseDto.class);
            
            log.info("분석 결과 수신: {}", analysisResult);

            // 분석 결과를 DB에 저장
            analysisRepository.save(analysisResult);
            log.info("분석 결과 저장 완료: {}", analysisResult.getProblemId());

            // 분석 결과를 반환
            return analysisResult;
            
        } catch (Exception e) {
            log.error("문제 분석 실패", e);
            throw new RuntimeException("문제 분석에 실패했습니다: " + e.getMessage());
        }
    }
}


