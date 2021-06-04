package be.pxl.auctions.model;

import be.pxl.auctions.model.builders.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserGetAgeTest {

	private static final int YEARS = 15;
	private User user;

	@BeforeEach
	public void init() {
		user = UserBuilder.anUser().build();
	}

	@Test
	public void returnsCorrectAgeWhenHavingBirthdayToday() {
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS));

		assertEquals(YEARS, user.getAge());
	}

	@Test
	public void returnsCorrectAgeWhenHavingBirthdayTomorrow() {
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS).plusDays(1));

		assertEquals(YEARS - 1, user.getAge());
	}

	@Test
	public void returnsCorrectAgeWhenBirthdayWasYesterday() {
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS).minusDays(1));

		assertEquals(YEARS, user.getAge());
	}

}
