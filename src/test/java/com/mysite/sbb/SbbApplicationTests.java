package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {
	@Autowired
	private QuestionRepository questionRepository;

	@Test
	void testJpa() {
		Question question1 = new Question();
		question1.setSubject("What is SBB?");
		question1.setContent("I want to know abut SBB!");
		question1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(question1); // 첫번째 질문 저장

		Question question2 = new Question();
		question2.setSubject("Question about SpringBoot model");
		question2.setContent("does ID automatically created?");
		question2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(question2); // 두번째 질문 저장
	}

	@Test
	void testJpa2() {
		List<Question> all = this.questionRepository.findAll(); // 전체 질문 찾기
		assertEquals(2, all.size());

		Question question = all.get(0); // 첫번째 질문 찾기
		assertEquals("What is SBB?", question.getSubject());
	}

	@Test
	void testJpa3() {
		Optional<Question> questionOptional = this.questionRepository.findById(1); // ID로 찾기

		if(questionOptional.isPresent()){
			Question question = questionOptional.get();
			assertEquals("What is SBB?", question.getSubject());
		}
	}

	@Test
	void testJpa4() {
		Question question = this.questionRepository.findBySubject("What is SBB?"); // Subject로 찾기
		assertEquals(1, question.getId());
	}

	@Test
	void testJpa5() {
		Optional<Question> questionOptional = this.questionRepository.findById(1);
		assertTrue(questionOptional.isPresent());

		Question question = questionOptional.get();
		question.setSubject("수정된 제목"); // 제목 수정하기
		this.questionRepository.save(question);
	}

	@Test
	void testJpa6() {
		// 질문 삭제하기
		assertEquals(2, this.questionRepository.count());
		Optional<Question> questionOptional = this.questionRepository.findById(1);
		assertTrue(questionOptional.isPresent());
		Question question = questionOptional.get();
		this.questionRepository.delete(question);
		assertEquals(1, this.questionRepository.count());
	}

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	void testJpa7() {
		Optional<Question> questionOptional = this.questionRepository.findById(2);
		assertTrue(questionOptional.isPresent());
		Question question = questionOptional.get(); // Id가 2번인 질문 찾기

		Answer answer = new Answer(); // 답변 작성
		answer.setContent("Yes. it created automatically");
		answer.setQuestion(question); // 2번 질문에 대한 답변
		answer.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(answer); // 저장
	}

	@Test
	void testJpa8() {
		Optional<Answer> answerOptional = this.answerRepository.findById(1); // 1번 답변 찾기
		assertTrue(answerOptional.isPresent());
		Answer answer = answerOptional.get();
		assertEquals(2, answer.getQuestion().getId()); // 1번 답변의 질문 Id 찾기
	}

	@Transactional // 메서드가 종료될 때까지 DB세션 유지
	@Test
	void testJpa9() {
		Optional<Question> questionOptional = this.questionRepository.findById(2);
		assertTrue(questionOptional.isPresent());
		Question question = questionOptional.get();

		List<Answer> answerList = question.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("Yes. it created automatically", answerList.get(0).getContent());
	}
}
