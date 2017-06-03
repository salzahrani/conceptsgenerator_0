package triplet.stanford;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ie.machinereading.structure.AnnotationUtils;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;


public class Sample1606_allWorksNeedToPrint {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		 // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    // read some text in the text variable
	    String text = "John helped Mary. He was kind.";
	    text = "On Wednesday,14 Rabi al-Awwal 1430,corresponding to 11 March 2009, the Detonations Brigade belonging to the Mujahidin Youth Movement [MYM] were able, with the grace of God, to target the private security official of Ali Muhammad Gedi and Nur Ade, the former prime ministers in the government of apostasy that was headed by the apostate Abdullahi Yusuf.   The blessed operation was carried out at 0900 hours Mogadishu local time.  It claimed the lives of the so-called Ubayd Mahmud Muhammad, the security official of the abovementioned, and a number of his bodyguards through the blowing up of their vehicle by a blessed explosive device planted by the mujahidin on the side of the road in the Shups area in the Islamic State of Banadir (Mogadishu).  Praise and thanks be to God, Lord of the Worlds.";
	    
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        // this is the text of the token
	        String word = token.get(TextAnnotation.class);
	        //System.out.println("Word is "+word);
	        // this is the POS tag of the token
	        String pos = token.get(PartOfSpeechAnnotation.class);
	        //System.out.println("Word is "+pos);
	        // this is the NER label of the token
	        String ne = token.get(NamedEntityTagAnnotation.class);
	        //System.out.println("Word is "+ne);
	        
	      }

	      // this is the parse tree of the current sentence
	      Tree tree = sentence.get(TreeAnnotation.class);

	      // this is the Stanford dependency graph of the current sentence
	      SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	    }

	    // This is the coreference link graph
	    // Each chain stores a set of mentions that link to each other,
	    // along with a method for getting the most representative mention
	    // Both sentence and token offsets start at 1!
	    Map<Integer, CorefChain> graph = 
	      document.get(CorefChainAnnotation.class);
	    
	    for (Map.Entry<Integer, CorefChain> entry : graph.entrySet()) {
	        if(entry.getValue().getMentionsInTextualOrder().size()>1){
	        	System.out.println(entry.getKey() + ", " + entry.getValue());
	        }   	
	    }
	    
	    for(Map.Entry<Integer, CorefChain> entry : graph.entrySet()) {
            CorefChain c = entry.getValue();

            //this is because it prints out a lot of self references which aren't that useful
            if(c.getMentionsInTextualOrder().size() <= 1)
                continue;

            CorefMention cm = c.getRepresentativeMention();
            String clust = "";
            List<CoreLabel> tks = document.get(SentencesAnnotation.class).get(cm.sentNum-1).get(TokensAnnotation.class);
            for(int i = cm.startIndex-1; i < cm.endIndex-1; i++){
                clust += tks.get(i).get(TextAnnotation.class) + " ";            
            }
            clust = clust.trim();
            System.out.println("representative mention: \"" + clust + "\" is mentioned by:");

            for(CorefMention m : c.getMentionsInTextualOrder()){
                String clust2 = "";
                tks = document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class);
                for(int i = m.startIndex-1; i < m.endIndex-1; i++)
                    clust2 += tks.get(i).get(TextAnnotation.class) + " ";
                clust2 = clust2.trim();
                //don't need the self mention
                if(clust.equals(clust2)||!(PronounHashMap.getPronounHashmap().containsKey(clust2.toLowerCase())))
                    continue;

                System.out.println("\t" + clust2);
                System.out.println("NOW SENTENCE IS "+ tks.toString());
                System.out.println("document SENTENCE IS "+ document.toString());
                
                CoreLabel cl3=new CoreLabel(document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class).get(m.startIndex-1));
                //CoreLabel cl3=new CoreLabel();
                cl3.setValue(clust);
                
                document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class).set(m.startIndex-1, cl3);
                System.out.println("document SENTENCE IS AFTER MODIFING"+ document.toString());
                tks = document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class);
                System.out.println("&&& AFTER RESETTING "+ tks.toString());
                System.out.println("document SENTENCE IS "+ document.toString());
                System.out.println("HIII");
                //new StanfordCoreNLP().prettyPrint(document, System.out);
                
                
                
            }
        }

	}

}
