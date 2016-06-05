package org.coinvent.haiku;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.winterwell.utils.io.FileUtils;

import creole.data.XId;
import winterwell.jtwitter.Status;
import winterwell.jtwitter.TwitterTest;
import winterwell.maths.stats.distributions.cond.Cntxt;
import winterwell.maths.stats.distributions.cond.ICondDistribution;
import winterwell.maths.stats.distributions.cond.UnConditional;
import winterwell.maths.stats.distributions.cond.WWModel;
import winterwell.maths.stats.distributions.discrete.IDiscreteDistribution;
import winterwell.maths.stats.distributions.discrete.ObjectDistribution;
import winterwell.nlp.docmodels.IDocModel;
import winterwell.nlp.io.Tkn;

public class PoemGeneratorTest {

	@Test
	public void testGenerateWord() {
		List<Haiku> haikus = HaikuMain.loadHaikus();
		int constraint[] = {5,7,5};
		PoemGenerator pg = new PoemGenerator(LanguageModel.get(), haikus, constraint);
		ObjectDistribution<Tkn> dist = new ObjectDistribution<>();
		dist.train1(new Tkn("bloody"));
		dist.train1(new Tkn("damned"));
		dist.train1(new Tkn("damned"));
		dist.train1(new Tkn("sodding"));
		dist.train1(new Tkn("lovely"));
		pg.wordGen = new UnConditional<Tkn>(dist);
		
		Line line = new Line(5);
		WordInfo wi = new WordInfo("thingy", 2).setPOS("JJ");
		line.words.add(new WordInfo("the", 1).setPOS("DT"));
		line.words.add(wi);
		line.words.add(new WordInfo("cat", 1).setPOS("NN"));
		
		for(int i=0; i<5; i++) {
			Object gen = pg.generateWord(wi, line);
			System.out.println(gen);
		}
	}

	@Test
	public void testGenerateSmokeTest() {
		List<Haiku> haikus = HaikuMain.loadHaikus();
		LanguageModel languageModel = LanguageModel.get();
		int constraint[] = {5,7,5};
		PoemGenerator generator = new PoemGenerator(languageModel, haikus, constraint);
		PoemVocab vocab = languageModel.allVocab;
		generator.setVocab(vocab);
		assert vocab.getAllWords().size() > 100;
		
		// TODO score the Haiku
		IDocModel model = null;
		generator.setDocModel(model);
		
		ObjectDistribution dist = new ObjectDistribution<>();
		dist.train1(new Tkn("bloody"));
		dist.train1(new Tkn("a"));
		ICondDistribution<Tkn, Cntxt> wordGen = new UnConditional(dist);
		
		generator.setWordGen(wordGen);
		
		Poem haiku = generator.generate("love", "food");
		System.out.println("Love Food");
		System.out.println(haiku);
	}

	
	@Test
	public void testGenerateFromTweets() {
		List<Haiku> haikus = HaikuMain.loadHaikus();
		LanguageModel languageModel = LanguageModel.get();
		int constraint[] = {5,7,5};
		PoemGenerator generator = new PoemGenerator(languageModel, haikus, constraint);

		VocabFromTwitterProfile vftp = new VocabFromTwitterProfile(TwitterTest.newTestTwitter(), new XId("winterstein@twitter"));
		List<Status> tweets = FileUtils.load(VocabFromTwitterProfileTest.TWEET_FILE);
		vftp.train(tweets);		
		
		PoemVocab vocab = vftp.getVocab();
		generator.setVocab(vocab);
		assert vocab.getAllWords().size() > 100;
		
		// TODO score the Haiku
		IDocModel model = null;
		generator.setDocModel(model);
		
		WWModel<Tkn> wordGen = vftp.getWordModel();
		generator.setWordGen(wordGen);
		
		Poem haiku = generator.generate("love", "food");
		System.out.println("Love Food");
		System.out.println(haiku);
	}

}
