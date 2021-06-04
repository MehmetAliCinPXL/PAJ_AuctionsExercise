package be.pxl.auctions.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailValidatorIsValidTest {

	@Test
	public void returnsTrueWhenValidEmail() {
		assertTrue(EmailValidator.isValid("nele.custers@pxl.be"));
	}

	@Test
	public void returnsFalseWhenAtSignMissing() {
		assertFalse(EmailValidator.isValid("nele.custers$pxl.be"));
	}

}
