package com.algoanalyzer.service;

import com.algoanalyzer.dto.ProblemResponseDto;
import com.algoanalyzer.dto.SolvedAcProblemResponse;
import com.algoanalyzer.exception.ProblemNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProblemServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProblemService problemService;

    @Test
    void getProblem_ValidProblemId_ReturnsProblemInfo() {
        // given
        Long problemId = 1000L;
        SolvedAcProblemResponse mockResponse = createMockResponse();
        when(restTemplate.getForObject(anyString(), eq(SolvedAcProblemResponse.class)))
                .thenReturn(mockResponse);

        // when
        ProblemResponseDto result = problemService.getProblem(problemId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getProblemId()).isEqualTo(problemId);
        assertThat(result.getTitle()).isEqualTo("A+B");
        assertThat(result.getLevel()).isEqualTo("1");
    }

    @Test
    void getProblem_ProblemNotFound_ThrowsException() {
        // given
        Long problemId = 999999L;
        when(restTemplate.getForObject(anyString(), eq(SolvedAcProblemResponse.class)))
                .thenReturn(null);

        // when & then
        assertThatThrownBy(() -> problemService.getProblem(problemId))
                .isInstanceOf(ProblemNotFoundException.class)
                .hasMessageContaining(String.valueOf(problemId));
    }

    private SolvedAcProblemResponse createMockResponse() {
        SolvedAcProblemResponse response = new SolvedAcProblemResponse();
        response.setProblemId(1000L);
        response.setTitleKo("A+B");
        response.setLevel(1);
        response.setAverageTries(1.5);
        
        SolvedAcProblemResponse.ProblemTag tag = new SolvedAcProblemResponse.ProblemTag();
        SolvedAcProblemResponse.DisplayName displayName = new SolvedAcProblemResponse.DisplayName();
        displayName.setLanguage("ko");
        displayName.setName("수학");
        tag.setDisplayNames(Collections.singletonList(displayName));
        response.setTags(Collections.singletonList(tag));
        
        return response;
    }
} 