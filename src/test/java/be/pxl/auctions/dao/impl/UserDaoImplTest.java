package be.pxl.auctions.dao.impl;

import be.pxl.auctions.model.User;
import be.pxl.auctions.model.builders.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class UserDaoImplTest {

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private UserDaoImpl userDao;

	@Test
	public void userCanBeSavedAndRetrievedById() {
		User user = UserBuilder.anUser().build();
		long newUserId = userDao.saveUser(user).getId();
		entityManager.flush();
		entityManager.clear();

		Optional<User> retrievedUser = userDao.findUserById(newUserId);
		assertTrue(retrievedUser.isPresent());

		assertEquals(UserBuilder.FIRST_NAME, retrievedUser.get().getFirstName());
		assertEquals(UserBuilder.LAST_NAME, retrievedUser.get().getLastName());
		assertEquals(UserBuilder.EMAIL, retrievedUser.get().getEmail());
		assertEquals(UserBuilder.DATE_OF_BIRTH, retrievedUser.get().getDateOfBirth());
	}
	@Test
	public void userCanBeSavedAndRetrievedByEmail() {
		User user = UserBuilder.anUser().withEmail("elly@facebook.com").build();
		userDao.saveUser(user);
		entityManager.flush();
		entityManager.clear();

		Optional<User> retrievedUser = userDao.findUserByEmail("elly@facebook.com");
		assertTrue(retrievedUser.isPresent());

		assertEquals(user.getEmail(), retrievedUser.get().getEmail());
	}

	@Test
	public void returnsNullWhenNoUserFoundWithGivenEmail() {
		Optional<User> retrievedUser = userDao.findUserByEmail("bruno@facebook.com");
		assertFalse(retrievedUser.isPresent());
	}

	@Test
	public void allUsersCanBeRetrieved() {
		int initialNumberOfUsers = userDao.findAllUsers().size();

		User user = new User();
		user.setFirstName("Emma");
		user.setLastName("Young");
		user.setDateOfBirth(LocalDate.of(2000, 5, 3));
		user.setEmail("emma@facebook.com");
		userDao.saveUser(user);
		entityManager.flush();
		entityManager.clear();

		List<User> allUsers = userDao.findAllUsers();
		assertEquals(initialNumberOfUsers + 1, allUsers.size());
		assertEquals(1, allUsers.stream().filter(u -> u.getEmail().equals("emma@facebook.com")).count());
	}

	@Test
	public void emptyOptionalWhenNoUserFoundWithGivenEmail() {
		Optional<User> user = userDao.findUserByEmail("mickey@disney.com");

		assertTrue(user.isEmpty());
	}


}
