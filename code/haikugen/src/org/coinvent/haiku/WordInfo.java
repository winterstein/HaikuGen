package org.coinvent.haiku;

import java.util.regex.Pattern;

import com.winterwell.utils.StrUtils;

import com.winterwell.nlp.corpus.brown.BrownCorpusTags;
import com.winterwell.nlp.io.Tkn;


public class WordInfo {
	
	public static final WordInfo UNKNOWN = new WordInfo("?", -1);
	public String word;
	private int syllables = -1;
	
	public int syllables() {
		return syllables;
	}
	
	public void setSyllables(int syllables) {
		this.syllables = syllables;
		assert syllables > 0 || ! StrUtils.isWord(pos) : pos+" "+this;
	}
	
	boolean fixed;
	boolean punctuation;
	public String pos;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WordInfo other = (WordInfo) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

	public WordInfo(String word, int syllables) {		
		this.word = word;
		this.syllables = syllables;
		// only punctuation has 0 syllables
		assert syllables!=0 || word==null || StrUtils.isPunctuation(word) || word.startsWith("<") || word.isEmpty() : word;
	}
	
	public WordInfo setFixed(boolean fixed) {
		this.fixed = fixed;
		return this;
	}
	
	public WordInfo() {
	}

	@Override
	public String toString() {
		return word+"/"+pos+":"+syllables;
	}

	public void setWord(String word) {
		this.word = word;		
		this.syllables = word==null? -1 : LanguageModel.get().getSyllable(word);
		// punctuation has 0 syllables
		if ( ! Pattern.compile("[a-zA-Z0-9]").matcher(word).find()) {
			assert syllables==0 : this;
		}
	}

	public WordInfo setPOS(String posTag) {
		this.pos = BrownCorpusTags.toCanon(posTag);
		return this;
	}

	public Tkn getTkn() {		
		Tkn tkn = new Tkn(word==null? Tkn.UNKNOWN : word);
		tkn.setPOS(pos);
		return tkn;
	}
}
