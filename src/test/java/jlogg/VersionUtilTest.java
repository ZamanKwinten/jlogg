package jlogg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jlogg.build.BuildDetailsUtil;

public class VersionUtilTest {

	@Test
	public void f1() {
		assertFalse(BuildDetailsUtil.isLaterVersion("0.0.1", "0.0.0"));
	}

	@Test
	public void f2() {
		assertFalse(BuildDetailsUtil.isLaterVersion("0.1.0", "0.0.1"));
	}

	@Test
	public void f3() {
		assertFalse(BuildDetailsUtil.isLaterVersion("1.0.0", "0.1.2"));
	}

	@Test
	public void f4() {
		assertFalse(BuildDetailsUtil.isLaterVersion("2.0.0", "1.2.3"));
	}

	@Test
	public void f5() {
		assertFalse(BuildDetailsUtil.isLaterVersion("1.2.3", "1.2.3"));
	}

	@Test
	public void t1() {
		assertTrue(BuildDetailsUtil.isLaterVersion("0.0.1", "0.0.2"));
	}

	@Test
	public void t2() {
		assertTrue(BuildDetailsUtil.isLaterVersion("0.1.2", "0.2.0"));
	}

	@Test
	public void t3() {
		assertTrue(BuildDetailsUtil.isLaterVersion("1.2.3", "2.0.0"));
	}
}
