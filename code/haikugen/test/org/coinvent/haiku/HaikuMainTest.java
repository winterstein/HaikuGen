package org.coinvent.haiku;
import java.util.ArrayList;
import java.util.List;

import org.coinvent.haiku.Haiku;
import org.coinvent.haiku.HaikuMain;
import org.coinvent.haiku.LanguageModel;
import org.junit.Test;


public class HaikuMainTest {

	@Test
	public void testLove() {
		List<Haiku> haikus = HaikuMain.loadHaikus();
		LanguageModel languageModel = LanguageModel.get();
		int constraint[] = {5,7,5};
		PoemGenerator generator = new PoemGenerator(languageModel, haikus, constraint);
		String idea = "love";
		System.out.println("Creating Haiku...");
		Object res = generator.generate(idea, null);
		System.out.println(res);		
	}
	
}
