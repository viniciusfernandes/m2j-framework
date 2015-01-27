package com.disclinc.netmanager.function.test;

import static mLibrary.mFunction.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterFunctionTest {

	@Test
	public void testAlphaAChar() {
		assertEquals("Fail to generate char from empty integer sequence", "A", $char("65"));
	}

	@Test
	public void testCharSequence() {
		int asteriscCode = 42;
		int plusCode = 43;
		int commaCode = 44;
		int minusCode = 45;
		assertEquals("Fail to generate char sequence from a integer sequence", "*+,-",
				$char(asteriscCode, plusCode, commaCode, minusCode));
	}

	@Test
	public void testCharSequenceAvoidingNegativeInteger() {
		int asteriscCode = 42;
		int plusCode = -43;
		int commaCode = 44;

		assertEquals("Fail to generate char sequence avoiding a negative integer", "*,",
				$char(asteriscCode, plusCode, commaCode));
	}

	@Test
	public void testConcatingEmptyChar() {
		assertEquals("Fail to generate char from empty integer sequence", "1" + $char(""), $char("49", ""));

	}

	@Test
	public void testEmptyCharSequence() {
		assertEquals("Fail to generate char from empty integer sequence", null, $char(new int[] {}));
	}

	@Test
	public void testNullCharSequence() {
		assertEquals("Fail to generate char from null integer sequence", null, $char((Object) null));
	}

	@Test
	public void testSingleChar() {
		Object commaCode = 44;
		assertEquals("Fail to generate char from a integer", ",", $char(commaCode));
	}
}
