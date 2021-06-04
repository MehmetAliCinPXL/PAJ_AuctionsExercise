package be.pxl.auctions.service;

import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.User;
import be.pxl.auctions.model.builders.UserBuilder;
import be.pxl.auctions.rest.resource.UserDTO;
import be.pxl.auctions.util.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceGetUserByIdTest {

	private static final long USER_ID = 5L;

	@Mock
	private UserDao userDao;
	@InjectMocks
	private UserService userService;
	private User user;

	@BeforeEach
	public void init() {
		user = UserBuilder.anUser().withId(USER_ID).build();
	}

	@Test
	public void throwsUserNotFoundExceptionWhenNoUserWithGivenIdFound() {
		when(userDao.findUserById(USER_ID)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.getUserById(USER_ID));
	}

	@Test
	public void returnsUserDTOWhenUserFoundWithGivenId() {
		when(userDao.findUserById(USER_ID)).thenReturn(Optional.of(user));

		UserDTO userById = userService.getUserById(USER_ID);
		assertEquals(USER_ID, userById.getId());
		assertEquals(UserBuilder.FIRST_NAME, userById.getFirstName());
		assertEquals(UserBuilder.LAST_NAME, userById.getLastName());
		assertEquals(UserBuilder.EMAIL, userById.getEmail());
		assertEquals(UserBuilder.DATE_OF_BIRTH, userById.getDateOfBirth());

	}
}
