package be.pxl.auctions.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserGetAgeTest {
	private static final int YEARS = 15;
	private User user;

	@BeforeEach
	public void setup(){
		user = UserBuilder.anUser().build();
	}

	@Test
	public void returnsCorrectAgeWhenHavingBirthdayToday() {
		user.setDateOfBirth(LocalDate.now());
		assertEquals(YEARS, user.getAge());
	}

	@Test
	public void returnsCorrectAgeWhenHavingBirthdayTomorrow() {
		user.setDateOfBirth(LocalDate.now().plusYears(YEARS));
		assertEquals(YEARS, user.getAge());
	}

	@Test
	public void returnsCorrectAgeWhenBirthdayWasYesterday() {
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS));
		assertEquals(YEARS, user.getAge());
	}

}
