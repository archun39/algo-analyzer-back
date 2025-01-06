package com.algoanalyzer.service;

import com.algoanalyzer.dto.ProblemResponseDto;
import com.algoanalyzer.dto.SolvedAcProblemResponse;
import com.algoanalyzer.exception.ProblemNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemService {
    private static final String SOLVED_AC_API_URL = "https://solved.ac/api/v3/problem/show?problemId=";
    private static final String BOJ_PROBLEM_URL = "https://www.acmicpc.net/problem/";
    private final RestTemplate restTemplate;

    public ProblemResponseDto getProblem(Long problemId) {
        try {
            log.info("문제 정보 조회 시작: {}", problemId);
            
            // solved.ac API 호출
            String url = SOLVED_AC_API_URL + problemId;
            SolvedAcProblemResponse response = restTemplate.getForObject(url, SolvedAcProblemResponse.class);
            
            if (response == null) {
                throw new ProblemNotFoundException("문제를 찾을 수 없습니다: " + problemId);
            }
            
            // 백준 웹사이트에서 문제 설명, 입력, 출력 정보 가져오기
            fetchProblemDetails(problemId, response);
            
            // 응답 변환
            ProblemResponseDto result = convertToResponseDto(response);
            
            log.info("문제 정보 조회 완료: {}", problemId);
            return result;
            
        } catch (Exception e) {
            log.error("문제 정보 조회 실패: {}", problemId, e);
            throw new ProblemNotFoundException("문제 정보를 가져오는데 실패했습니다: " + problemId);
        }
    }

    private void fetchProblemDetails(Long problemId, SolvedAcProblemResponse response) {
        try {
            String bojUrl = BOJ_PROBLEM_URL + problemId;
            Document doc = Jsoup.connect(bojUrl).get();
            
            // 문제 설명
            Elements descriptionElement = doc.select("#problem_description");
            response.setDescription(descriptionElement.text());
            
            // 입력 설명
            Elements inputElement = doc.select("#problem_input");
            response.setInput(inputElement.text());
            
            // 출력 설명
            Elements outputElement = doc.select("#problem_output");
            response.setOutput(outputElement.text());
            
        } catch (Exception e) {
            log.warn("문제 상세 정보 조회 실패: {}", problemId, e);
        }
    }

    private ProblemResponseDto convertToResponseDto(SolvedAcProblemResponse response) {
        String tags = response.getTags().stream()
                .map(tag -> tag.getDisplayNames().stream()
                        .filter(display -> "ko".equals(display.getLanguage()))
                        .findFirst()
                        .map(DisplayName::getName)
                        .orElse(tag.getKey()))
                .collect(Collectors.joining(", "));

        return ProblemResponseDto.builder()
                .problemId(response.getProblemId())
                .title(response.getTitleKo())
                .level(String.valueOf(response.getLevel()))
                .tags(tags)
                .averageTries(response.getAverageTries())
                .description(response.getDescription())
                .input(response.getInput())
                .output(response.getOutput())
                .build();
    }
} 