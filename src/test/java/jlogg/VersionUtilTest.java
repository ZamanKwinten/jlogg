package jlogg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jlogg.version.VersionUtil;

public class VersionUtilTest {

	@Test
	public void f1() {
		assertFalse(VersionUtil.isLaterVersion("0.0.1", "0.0.0"));
	}

	@Test
	public void f2() {
		assertFalse(VersionUtil.isLaterVersion("0.1.0", "0.0.1"));
	}

	@Test
	public void f3() {
		assertFalse(VersionUtil.isLaterVersion("1.0.0", "0.1.2"));
	}

	@Test
	public void f4() {
		assertFalse(VersionUtil.isLaterVersion("2.0.0", "1.2.3"));
	}

	@Test
	public void f5() {
		assertFalse(VersionUtil.isLaterVersion("1.2.3", "1.2.3"));
	}

	@Test
	public void t1() {
		assertTrue(VersionUtil.isLaterVersion("0.0.1", "0.0.2"));
	}

	@Test
	public void t2() {
		assertTrue(VersionUtil.isLaterVersion("0.1.2", "0.2.0"));
	}

	@Test
	public void t3() {
		assertTrue(VersionUtil.isLaterVersion("1.2.3", "2.0.0"));
	}
}
