package com.algoanalyzer.analysis.infrastructure.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;
import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.presentation.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.analysis.presentation.dto.response.ProblemAnalysisResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import com.algoanalyzer.analysis.application.port.out.AnalyzeProblemClient;
import com.algoanalyzer.analysis.domain.exception.ProblemAnalysisException;
import com.algoanalyzer.analysis.application.mapper.ProblemAnalysisMapper;

@Component
@RequiredArgsConstructor
public class ProblemAnalysisClient implements AnalyzeProblemClient {

    private final RestTemplate restTemplate;
    private final ProblemAnalysisMapper problemAnalysisMapper;

    @Value("${python.api.base-url}")
    private String baseUrl;

    @Override
    public ProblemAnalysis callPythonApi(ProblemAnalysisRequestDto request) {
        //null값이 될 수 있음
        String url = baseUrl + "/api/internal/problems/" + request.getProblemId() + "/analysis";
         
        // HTTP 요청용 헤더 및 엔티티 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProblemAnalysisRequestDto> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<ProblemAnalysisResponseDto> resp = restTemplate
                .exchange(url, HttpMethod.POST, entity, ProblemAnalysisResponseDto.class);
    
            ProblemAnalysisResponseDto responseDto = resp.getBody();
            if (!resp.getStatusCode().is2xxSuccessful() || responseDto == null) {
                throw new ProblemAnalysisException("문제 분석 실패: HTTP " + resp.getStatusCodeValue());
            }
    
            ProblemAnalysis analysis = problemAnalysisMapper.toDomain(responseDto);
   
            return analysis;
    
        } catch (Exception e) {
            // 예외 스택트레이스 로깅
            System.err.println("[AI Client] 호출 중 예외 발생:");
            e.printStackTrace();
            throw e;
        }
    }
}
