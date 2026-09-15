package com.fazil.learn_spring.learnspring_jpa;

import com.fazil.learn_spring.learnspring_jpa.repository.StudentRepository;
import com.fazil.learn_spring.learnspring_jpa.service.StudentService;
import com.fazil.learn_spring.learnspring_jpa.test.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
@Nested
class LearnspringJpaApplicationTests {
	private StudentRepository studentRepository;
	private StudentService studentService;

//	@Test
//	void contextLoads() {
//	}

	private Calculator calculator;

	@BeforeEach
	void setUp() {
		calculator = new Calculator();
	}

	@Test
	void shouldAddTwoNumbers() {

		int result = calculator.add(7, 8);

		assertThat(result)
				.isEqualTo(15);
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 4})
	void shouldValidateAge(int age) {

		assertThat(age)
				.isGreaterThan(4)
				.isLessThan(6);
	}

	@Test
	void shouldValidateUsername() {
		String username = "john";

		assertThat(username)
				.isNotNull()
				.isNotBlank()
				.isEqualTo("john");
	}

	@ParameterizedTest
	@CsvSource({
			"4, 2, 2",
			"10, 2, 5",
			"49, 7, 7"
	})
	@DisplayName("denominator should divide numerator")
	void shouldDivideNumber(int a, int b, int expected) {
		int result = calculator.divide(a, b);
		assertThat(result)
				.isEqualTo(expected);
	}

	@Test
	void ShouldRejectDivisionByZero() {
		assertThatThrownBy(() -> calculator.divide(10, 0))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Cannot divide by zero");
	}


}

@Nested
class InvalidTests {

	private Calculator calculator;

	@BeforeEach
	void setUp() {
		calculator = new Calculator();
	}

	@Test
	void ShouldRejectDivisionByZeroThrows() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> calculator.divide(10, 0));

		assertThat(exception.getMessage())
				.isEqualTo("Cannot divide by zero");
	}
}
