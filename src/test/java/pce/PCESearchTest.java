package pce;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.Key;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import client.module.cac.PCESearchClientService;
import client.module.cac.RootKeyService;
import client.module.cac.impl.KeyServiceImpl;
import client.module.cac.impl.PCESearchClientServiceImpl;
import server.module.search.PCESearchServerService;
import server.module.search.impl.PCESearchServerServiceImpl;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class PCESearchTest {

	@Test
	public void baseTest() {
		PCESearchServerService sseS = new PCESearchServerServiceImpl();
		PCESearchClientService sse = new PCESearchClientServiceImpl();
		RootKeyService keys = new KeyServiceImpl();
		
		Key key = keys.generateRootKey();
		
		Set<byte[]> index1 = sse.generateIndex(key, "locator1", "B", "Erklärung");
		Set<byte[]> index2 = sse.generateIndex(key, "locator2", "EV", "A");
		
		Set<byte[]> trapdoor = sse.generateTrapdoor(key, "EV", new HashSet<String>(Arrays.asList("locator1", "locator2")));

		Assert.assertTrue(index1.size()==2);
		Assert.assertTrue(index2.size()==2);
		Assert.assertTrue(trapdoor.size()==2);
		
		assertFalse(sseS.match(index1, trapdoor));
		assertTrue(sseS.match(index2, trapdoor));
	
	}

}
