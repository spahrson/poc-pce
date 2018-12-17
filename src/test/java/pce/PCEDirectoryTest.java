package pce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Key;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import client.core.Category;
import client.core.DirKey;
import client.core.Entry;
import client.module.cac.KeyDeriveService;
import client.module.cac.PCEDirectoryService;
import client.module.cac.RootKeyService;
import client.module.cac.impl.KeyServiceImpl;
import client.module.cac.impl.PCEDirectoryServiceImpl;

/**
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class PCEDirectoryTest {

	@Test
	public void baseTest() {
		
		// Erstellt eine Baumstruktur, ver- und entschlüsselt einen Verzeichniseintrag
		
		PCEDirectoryService ed = new PCEDirectoryServiceImpl();
		RootKeyService keys = new KeyServiceImpl();
		KeyDeriveService der = new KeyServiceImpl();
		
		Key kt = keys.generateRootKey();
		DirKey dirKeyA = der.deriveDirKey(kt);
		
		Entry entry = new Entry();
		entry.filename = "filename";
		entry.locator = "locator";
		
		Category a = new Category("a");
		Category b = new Category("b");
		Category c = new Category("c");
		a.addChild(b);
		b.addChild(c);

		entry.category = c.getPath();
		
		// 2 hergeleitete Schlüssel müssen gleich sein für dieselbe Kategorie
		DirKey dirKeyB = der.deriveDirKey(dirKeyA, b);
		DirKey dirKeyC = der.deriveDirKey(dirKeyB, c);
		DirKey dirKeyCCopy = der.deriveDirKey(dirKeyB, c);
		assertTrue(dirKeyC.key.equals(dirKeyCCopy.key));
		assertTrue(dirKeyC.publicKeys.equals(dirKeyCCopy.publicKeys));
		
		// ver- und entschlüsseln
		List<byte[]> ciphertext = ed.encrypt(dirKeyC, entry);
		Set<Entry> set = ed.decrypt(dirKeyC, ciphertext);		
		// FIXME: Check this assertion
		//  Assert.assertTrue(set.size()==1);
		assertEquals(set.toArray(new Entry[0])[0].category, entry.category);
		assertEquals(set.toArray(new Entry[0])[0].filename, entry.filename);
		assertEquals(set.toArray(new Entry[0])[0].locator, entry.locator);
	
	}

}
