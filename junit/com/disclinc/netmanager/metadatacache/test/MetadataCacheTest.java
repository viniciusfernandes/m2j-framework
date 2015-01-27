package com.disclinc.netmanager.metadatacache.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.innovatium.mumps2java.datastructure.GlobalCache;

public class MetadataCacheTest {
	private final GlobalCache cache = GlobalCache.getCache();
	private final List<Object[]> subsList = new ArrayList<>();

	@Before
	public void init() {

		subsList.add(new Object[] { "^x", 1, 2, 3 });
		subsList.add(new Object[] { "^x", "a", 2, 3 });
		subsList.add(new Object[] { "^x", "b", 5 });

		subsList.add(new Object[] { "^y", 1, 2, 3 });
		subsList.add(new Object[] { "^y", "a", 2, 3 });

		subsList.add(new Object[] { "^z", 1, 2, 3 });
		subsList.add(new Object[] { "^z", "a", 2, 3 });
		for (Object[] subs : subsList) {
			cache.addQueried(subs, true);
		}
	}

	@Test
	public void testLimpezaDoCache() {
		for (Object[] subs : subsList) {
			Assert.assertNotNull("O subscrito " + Arrays.deepToString(subs)
					+ " foi removido da memoria e nao deve estar em cache", cache.get(subs));

		}
	}
}