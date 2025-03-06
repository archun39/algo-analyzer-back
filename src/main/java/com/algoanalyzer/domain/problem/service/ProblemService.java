package com.algoanalyzer.domain.problem.service;

import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.algoanalyzer.domain.problem.api.dto.SolvedAcProblemResponse;
import com.algoanalyzer.domain.problem.dto.response.ProblemResponseDto;
import com.algoanalyzer.domain.problem.exception.ProblemNotFoundException;
import com.algoanalyzer.domain.problem.model.ProblemDocument;
import com.algoanalyzer.domain.problem.repository.ProblemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemService {
    private static final String SOLVED_AC_API_URL = "https://solved.ac/api/v3/problem/show?problemId=";
    private static final String BOJ_PROBLEM_URL = "https://www.acmicpc.net/problem/";
    private final RestTemplate restTemplate;
    private final ProblemRepository problemRepository;

    public ProblemResponseDto getProblem(Long problemId) {
        try {
            log.info("문제 정보 조회 시작: {}", problemId);

            // DB에서 문제 정보 조회
            ProblemDocument problemDocument = problemRepository.findByProblemId(problemId);
            if(problemDocument != null) {
                log.info("DB에서 문제 정보 조회 완료: {}", problemId);
                return convertToResponseDto(problemDocument);
            }

            // Solved.ac API에서 문제 제목과 태그 정보 가져오기
            String url = SOLVED_AC_API_URL + problemId;
            SolvedAcProblemResponse response = restTemplate.getForObject(url, SolvedAcProblemResponse.class);

            if (response == null) {
                throw new ProblemNotFoundException("문제를 찾을 수 없습니다: " + problemId);
            }

            // 백준 웹사이트에서 문제 상세 정보 크롤링
            fetchProblemDetails(problemId, response);

            log.info("문제 정보 조회 완료: {}", problemId);

            // DB에 문제 정보 저장
            saveProblemToDb(response);
            log.info("문제 정보 저장 완료: {}", problemId);

            // 문제 정보를 응답 DTO로 변환
            return convertToResponseDto(response);

        } catch (Exception e) {
            log.error("문제 정보 조회 실패: {}", problemId, e);
            throw new ProblemNotFoundException("문제 정보를 가져오는데 실패했습니다: " + problemId);
        }
    }

    private void fetchProblemDetails(Long problemId, SolvedAcProblemResponse response) {
        try {
            String bojUrl = BOJ_PROBLEM_URL + problemId;
            Document doc = Jsoup.connect(bojUrl)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36")
                                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                                .header("Connection", "keep-alive")
                                .timeout(5000)
                                .get();

            // 문제 설명
            Elements descriptionElement = doc.select("#problem_description");
            if (!descriptionElement.isEmpty()) {
                // 텍스트뿐만 아니라 이미지 태그 등 HTML 콘텐츠를 모두 포함하여 가져옵니다.
                String descriptionHtml = descriptionElement.html();
                response.setDescription(descriptionHtml);
                System.out.println(descriptionHtml);
            }

            // 입력 설명
            Elements inputElement = doc.select("#problem_input");
            if (!inputElement.isEmpty()) {
                response.setInput(inputElement.text());
            }

            // 출력 설명
            Elements outputElement = doc.select("#problem_output");
            if (!outputElement.isEmpty()) {
                response.setOutput(outputElement.text());
            }

            // 시간 제한과 메모리 제한
            Elements limitElement = doc.select("table#problem-info tbody tr");
            if (!limitElement.isEmpty()) {
                Elements tds = limitElement.select("td");
                if (tds.size() >= 2) {
                    response.setTimeLimit(tds.get(0).text());
                    response.setMemoryLimit(tds.get(1).text());
                }
            } else {
                log.warn("시간 제한과 메모리 제한을 찾을 수 없습니다: {}", problemId);
            }

        } catch (Exception e) {
            log.error("문제 상세 정보 크롤링 실패: {}", problemId, e);
        }
    }

    private ProblemResponseDto convertToResponseDto(SolvedAcProblemResponse response) {
        return ProblemResponseDto.builder()
                .problemId(response.getProblemId())
                .title(response.getTitleKo())
                .description(response.getDescription())
                .input(response.getInput())
                .output(response.getOutput())
                .timeLimit(response.getTimeLimit())
                .memoryLimit(response.getMemoryLimit())
                .tags(response.getTags().stream()
                        .map(tag -> {
                            if (tag.getDisplayNames() != null && !tag.getDisplayNames().isEmpty()) {
                                return tag.getDisplayNames().get(0).getName();
                            } else {
                                return "Unknown";
                            }
                        })
                        .collect(Collectors.toList()))
                .build();
    }
    
    private ProblemResponseDto convertToResponseDto(ProblemDocument problemDocument) {
        return ProblemResponseDto.builder()
                .problemId(problemDocument.getProblemId())
                .title(problemDocument.getTitle())
                .description(problemDocument.getDescription())
                .input(problemDocument.getInput())
                .output(problemDocument.getOutput())
                .timeLimit(problemDocument.getTimeLimit())
                .memoryLimit(problemDocument.getMemoryLimit())
                .tags(problemDocument.getTags())
                .build();
    }

    // DB에 문제 정보 저장
    private void saveProblemToDb(SolvedAcProblemResponse response) {
        ProblemDocument problemDocument = ProblemDocument.builder()
                .problemId(response.getProblemId())
                .title(response.getTitleKo())
                .description(response.getDescription())
                .input(response.getInput())
                .output(response.getOutput())
                .timeLimit(response.getTimeLimit())
                .memoryLimit(response.getMemoryLimit())
                .tags(response.getTags().stream()
                        .map(tag -> tag.getDisplayNames().get(0).getName())
                        .collect(Collectors.toList()))
                .build();
        problemRepository.save(problemDocument);
    }
} 