package org.hf;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class CommonLanguages {

	enum Lang {ENGLISH, FRENCH, URDU, JAPANESE}

	/** Find the languages in common between two people. */
	public static void main(String... aArgs){
		EnumSet<Lang> ariane = EnumSet.of(Lang.FRENCH, Lang.ENGLISH);
		EnumSet<Lang> noriaki = EnumSet.of(Lang.JAPANESE, Lang.ENGLISH);
		log( "Languages in common: " + commonLangsFor(ariane, noriaki) );
	}

	private static Set<Lang> commonLangsFor(Set<Lang> aThisSet, Set<Lang> aThatSet){
		Set<Lang> result = new LinkedHashSet<Lang>();
		for(Lang lang: aThisSet){
			if( aThatSet.contains(lang) ) {
				result.add(lang);
			}
		}
		return result;
	}

	private static void log(Object aMessage){
		System.out.println(String.valueOf(aMessage));
	}
} 
