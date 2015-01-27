package com.disclinc.netmanager.function.test;

import mLibrary.mContext;
import mLibrary.mFunction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DataFunctionTest {
	private mContext m$ = mContext.createSemAcesso();

	@Before
	public void init() {
		m$.var("x").set(1);
		m$.var("x", "11").set(null);
		m$.var("x", "12").set(12);
		m$.var("x", "3").set(null);
		m$.var("x", "3", "33").set(33);
		m$.var("x", "4").set(4);
		m$.var("y").set(null);
	}

	@Test
	public void testCheckingUndefinedVariableAndWhichWithoutChildren() {
		Assert.assertEquals("This variable is undefined and has no one child", 0, mFunction.$data(m$.var("y")));
		Assert.assertEquals("This variable is undefined and has no one child", 0, mFunction.$data(m$.var("x", "11")));
	}

	@Test
	public void testCheckingUndefinedVariableAndWithChildren() {
		Assert.assertEquals("This variable is undefined and has no one child", 10, mFunction.$data(m$.var("x", "3")));
	}

	@Test
	public void testCheckingVariableAndWithChildren() {
		Assert.assertEquals("This variable is undefined and has no one child", 11, mFunction.$data(m$.var("x")));
	}

	@Test
	public void testCheckingVariableAndWithoutChildren() {
		Assert.assertEquals("This variable is undefined and has no one child", 1, mFunction.$data(m$.var("x", "4")));
	}
}
