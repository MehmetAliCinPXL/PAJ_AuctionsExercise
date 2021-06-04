package be.pxl.auctions.service;

import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.User;
import be.pxl.auctions.model.builders.UserBuilder;
import be.pxl.auctions.model.builders.UserCreateResourceBuilder;
import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.rest.resource.UserDTO;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.InvalidEmailException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceCreateUserTest {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final String DATE_IN_FUTURE = LocalDate.now().plusDays(1).format(FORMATTER);

	@Mock
	private UserDao userDao;
	@InjectMocks
	private UserService userService;
	private UserCreateResource user;
	@Captor
	private ArgumentCaptor<User> userCaptor;

	@BeforeEach
	public void init() {
		user = UserCreateResourceBuilder.anUserCreateResource().build();
	}

	@Test
	public void throwsRequiredFieldExceptionWhenFirstNameNotGiven() {
		user.setFirstName("");
		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsRequiredFieldExceptionWhenLastNameNotGiven() {
		user.setLastName(null);
		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsRequiredFieldExceptionWhenEmailNotGiven() {
		user.setEmail("    ");
		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsRequiredFieldExceptionWhenDateOfBirthNotGiven() {
		user.setDateOfBirth(null);
		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));

	}

	@Test
	public void throwsInvalidEmailExceptionWhenInvalidEmailGiven() {
		user.setEmail("test");
		assertThrows(InvalidEmailException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsInvalidDateExceptionWhenDateOfBirthInFuture() {
		user.setDateOfBirth(DATE_IN_FUTURE);
		assertThrows(InvalidDateException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsDuplicateEmailExceptionWhenEmailNotUnique() {
		when(userDao.findUserByEmail(UserCreateResourceBuilder.EMAIL)).
				thenReturn(Optional.of(UserBuilder.anUser().build()));

		assertThrows(DuplicateEmailException.class, () -> userService.createUser(user));
	}

	@Test
	public void validUserIsSavedCorrectly() throws InvalidDateException, RequiredFieldException, InvalidEmailException, DuplicateEmailException {
		when(userDao.findUserByEmail(UserCreateResourceBuilder.EMAIL)).thenReturn(Optional.empty());
		when(userDao.saveUser(any())).thenAnswer(returnsFirstArg());

		UserDTO createdUser = userService.createUser(user);

		assertNotNull(createdUser);

		verify(userDao).saveUser(userCaptor.capture());
		User userSaved = userCaptor.getValue();
		assertEquals(UserCreateResourceBuilder.FIRST_NAME, userSaved.getFirstName());
		assertEquals(UserCreateResourceBuilder.LAST_NAME, userSaved.getLastName());
		assertEquals(UserCreateResourceBuilder.EMAIL, userSaved.getEmail());
		assertEquals(UserCreateResourceBuilder.DATE_OF_BIRTH, userSaved.getDateOfBirth().format(FORMATTER));
	}
}
