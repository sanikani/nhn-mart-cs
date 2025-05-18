package com.nhnacademy.mart.answer.repository;

import com.nhnacademy.mart.answer.domain.Answer;
import com.nhnacademy.mart.answer.domain.Content;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerRepositoryImplTest {

    private AnswerRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new AnswerRepositoryImpl();
    }

    @Test
    @DisplayName("저장된 답변이 없을 때 nextOrderId()는 1을 반환한다")
    void nextOrderId_WhenEmpty_ShouldReturnOne() {
        assertThat(repository.nextOrderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("save() 후 findById()로 같은 객체를 조회할 수 있다")
    void saveAndFindById_ShouldStoreAndRetrieveAnswer() {
        // 도메인에 맞는 생성자/빌더 사용
        Answer answer = new Answer(1L, new Content("content"), "admin");

        Answer saved = repository.save(answer);

        assertThat(saved).isSameAs(answer);
        assertThat(repository.findById(1L)).isEqualTo(answer);
    }

    @Test
    @DisplayName("save() 후 nextOrderId()는 기존 최대 ID+1을 반환한다")
    void nextOrderId_AfterSave_ShouldReturnMaxPlusOne() {
        // 초기 상태
        assertThat(repository.nextOrderId()).isEqualTo(1L);

        // ID 5인 답변 저장
        Long id = repository.nextOrderId();
        Answer answer = new Answer(id, new Content("test"), "admin");
        repository.save(answer);

        assertThat(repository.nextOrderId())
                .isEqualTo(id + 1);
    }

    @Test
    @DisplayName("findById()에 없는 ID를 넘기면 null을 반환한다")
    void findById_UnknownId_ShouldReturnNull() {
        assertThat(repository.findById(999L)).isNull();
    }
}
