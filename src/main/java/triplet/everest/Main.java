/**
 * Copyright (C) 2015 Saud Alashri - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Email: salashri at asu.edu
 * * This program extract triplets using Everest
 */

package triplet.everest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;

public class Main {

	private static Logger log = Logger.getLogger(Main.class);

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		parseIt();
//		testExtraction();
//		testGetTaggedText();
//		testParseAndRemovePeriods();
//		testDependencyTree();
		//testParser();
	}
	
	private static void testExtraction() {
		ExtractionService extractor = new ExtractionService();
		//extractor.checkParserFeedback();
	}
	
	public static void parseIt() throws IOException {
		log.debug("Running test driver!");

		CoreNlpParser parser = new CoreNlpParser();

		String sentence = "My dog has fleas. Fleas bite the dog. Fleas make me sad.";
		
		//////////////////////////////////////////////////////////////
		 String path="./corpus/Docs_Coeref_resovled/";
		 
         File folder = new File(path); 
        // PrintWriter writer = new PrintWriter(new FileWriter("coded_triplets_Everest.csv"));
         String out_path="./corpus/triplets/";
         FileOutputStream fos = new FileOutputStream(out_path+"triplets_Everest.csv");
         Writer out = new OutputStreamWriter(fos, "UTF8");
         out.write("Subject,Verb,Object,id\n");
 		File[] listOfFiles = folder.listFiles();
 		int counter=1;
 		for (int j = 0; j < listOfFiles.length; j++) {
 			  File file = listOfFiles[j];
 			  
 			  if (file.isFile() && file.getName().endsWith(".txt")) {
		
 				 counter=1;
				  String fileName,text="", s;
				  fileName = file.getName();
				  String[] parts= fileName.split("\\.");
				  for(int i=0;i<parts.length;i++)
				  {
					  System.out.println("part ("+i+"): "+parts[i]);
				  }
				 // String[] sentenceName=parts[0].split("_");
				 // String[] id_labels=sentenceName[1].split("--");
				 // String[] labels=id_labels[1].split("-");
				 // String class_label=labels[0];
				 // String category_label=labels[1];
				 // int id = Integer.parseInt(parts[0]);//id_labels[0]);//sentenceName[1]);
				  String id =parts[0];
				 // System.out.println("+++++++ id: "+paragraphNo+" class: "+class_label+" category: "+category_label);
				  InputStream in = new FileInputStream(path+"/"+file.getName());
					String line="";
					BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//				  line="";
				  //reader = new BufferedReader(new InputStreamReader(in));
						
				  while ((line = reader.readLine()) != null) 
					  text = line+text;
				  
				  reader.close();
				  System.out.println("Here is the text: "+ text);
 				  ArrayList<Triplet> results = parser.parseText(text);
 				//  System.out.println(id+": "+results.size());
 				  
 					  
 				  for(Triplet res : results) {
 					 out.write(res.getEntity1String()); //subject
 					out.write("," +res.getRelationString()); //verb
 					out.write("," + res.getEntity2String()); //obj
 					out.write(","+id+"\n");
 					//out.write(","+class_label+","+category_label+"\n");
 					  //System.out.println(res.toString());
 				  	}
 				  

		
 			  }
 		}
 		
 		out.close();
////////////////////////////////////////////////////////////////////////		
//		results = parser.parseText(sentence2);
//		System.out.println(results.size());
//		for(Triplet res : results) {
//			System.out.println(res.toString());
//		}
//		
//		results = parser.parseText(sentence3);
//		System.out.println(results.size());
//		for(Triplet res : results) {
//			System.out.println(res.toString());
//		}
	}

	private static void testGetTaggedText() {
		String testSentence = "Now is the time for all good men to come to the aid of their country.";
		
		CoreNlpPOSTagger tagger = new CoreNlpPOSTagger();
		
	//	System.out.println(tagger.getTaggedText(testSentence));
	//	System.out.println("\n");
	}
	
	private static void testParseAndRemovePeriods() {
		String testSentence = "Now is the time for all good men to come to the aid of their country.";
		
		CoreNlpParser parser = new CoreNlpParser();
		List<Tree> results = parser.getTextAnnotatedTree(testSentence);
		for(Tree tree : results) {
			tree.pennPrint();
		}
		
	//	System.out.println("\n");
	}
	
	private static void testDependencyTree() {
		String testSentence = "Now is the time for all good men to come to the aid of their country.";
		
		CoreNlpParser parser = new CoreNlpParser();
		List<SemanticGraph> result = parser.getTextDependencyTree(testSentence);
		for(SemanticGraph graph : result) {
			graph.prettyPrint();
		}
	}
}
