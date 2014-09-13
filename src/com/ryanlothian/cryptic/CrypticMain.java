package com.ryanlothian.cryptic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

/**
 * Command-line program for solving cryptic crossword problems.
 */
public final class CrypticMain {

    private static Thesaurus loadThesaurus() throws IOException {
        return Thesaurus.loadFromInputStream(new FileInputStream("thesaurus"));
    }
    
    private static Grammar loadGrammar(Thesaurus thesaurus) throws IOException {
        List<Rule> rules = Lists.newArrayList();
        InputStream inputStream = new FileInputStream("rules");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                rules.add(Rule.fromString(line));
            }
        } finally {
            inputStream.close();
            Closeables.close(reader, true);
        }
        return new Grammar(rules, thesaurus);
    }
    
	public static void main(String[] args) throws Exception {
	    Thesaurus thesaurus = loadThesaurus();
	    Grammar grammar = loadGrammar(thesaurus);
		CrypticSolver crypticSolver = new CrypticSolver(thesaurus, grammar);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		for (;;) {
		    System.out.println("Syntax");
		    System.out.println("simple: <non-cryptic clue>");
		    System.out.println("cryptic: <cryptic clue>");
		    System.out.println("synonyms: <word or phrase>");
		    
		    line = reader.readLine();
		    if (line == null) {
		        break;
		    }
		    String[] splitLine = line.split(":");
		    if (splitLine[0].equals("simple")) {
		        System.out.println(crypticSolver.solveNonCryptic(Arrays.asList(splitLine[1].split(" "))));
		    } else if (splitLine[0].equals("cryptic")) {
                System.out.println(crypticSolver.solve(splitLine[1]));
            } else if (splitLine[0].equals("synonyms")) {
                System.out.println(thesaurus.getSynonyms(splitLine[1]));
            }
            
        }
	}

}
