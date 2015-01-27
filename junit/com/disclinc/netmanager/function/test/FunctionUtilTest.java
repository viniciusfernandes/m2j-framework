package com.disclinc.netmanager.function.test;


import org.junit.Assert;
import org.junit.Test;

import util.mFunctionUtil;

public class FunctionUtilTest {
	@Test
	public void testBigNumber() {
		double value = 875000953674.3164;
		String result = "875000953674.3164";
		Assert.assertEquals(result, mFunctionUtil.toString(value));
	}

	@Test
	public void testConverteDoublesmallerThanzeroToString() {
		double value = 00000.112;
		String result = "0.112";
		Assert.assertEquals(result, mFunctionUtil.toString(value));
	}

	@Test
	public void testConverteDoubleStartedWithZeroToString() {
		double value = 0001.251e2;
		String result = "125.1";
		Assert.assertEquals(result, mFunctionUtil.toString(value));
	}

	@Test
	public void testConverteDoubleToString() {
		double value = 12.51;
		String result = "12.51";
		Assert.assertEquals(result, mFunctionUtil.toString(value));
	}

	@Test
	public void testConverteDoubleToStringEndZero() {
		double value = 12.50;
		String result = "12.5";
		Assert.assertEquals(result, mFunctionUtil.toString(value));
	}

	@Test
	public void testConverteDoubleToStringEndZero2() {
		double value = 30.00;
		String result = "30";
		Assert.assertEquals(result, mFunctionUtil.toString(value));
	}

	@Test
	public void testConverteExponentialToString() {
		double value = 1.251e2;
		String result = "125.1";
		Assert.assertEquals(result, mFunctionUtil.toString(value));
	}

	@Test
	public void testConverteIntegerToString() {
		double value = 111.000;
		String result = "111";
		Assert.assertEquals(result, mFunctionUtil.toString(value));

		Integer value2 = 111;
		Assert.assertEquals(result, mFunctionUtil.toString(value2));
	}

	@Test
	public void testConverteNullToString() {
		Object value = null;
		String result = "";
		Assert.assertEquals(result, mFunctionUtil.toString(value));
	}

	@Test
	public void testZero() {
		double value = 0.0;
		String result = "0";
		Assert.assertEquals(result, mFunctionUtil.toString(value));
	}
}
