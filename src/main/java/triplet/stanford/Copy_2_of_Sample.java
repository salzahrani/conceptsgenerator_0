package triplet.stanford;

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


public class Copy_2_of_Sample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    // read some text in the text variable
	    String text = "John helped Mary. He was kind.";
	    text="On Sunday morning, 18  Rabi Al-Awwal 1430, corresponding to 15 March 2009, supplies reached the  apostate militias, and they clashed with the mujahidin in a decisive battle on  two fronts. The residents of the area and our mujahidin brothers from the  Islamic Party took part in these clashes. The battles continued until the  afternoon, and victory was the ally of the mujahidin. The apostate militias  were defeated, and the mujahidin gained booty, which consisted of light and  heavy weapons and ammunition that were brought in from Ethiopia.";
	    
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
            List<CoreLabel> clustLabel = new ArrayList();
            List<CoreLabel> tks = document.get(SentencesAnnotation.class).get(cm.sentNum-1).get(TokensAnnotation.class);
            for(int i = cm.startIndex-1; i < cm.endIndex-1; i++){
                clust += tks.get(i).get(TextAnnotation.class) + " ";
                clustLabel.add(tks.get(i));
            }
            clust = clust.trim();
            System.out.println("representative mention: \"" + clust + "\" is mentioned by:");

            for(CorefMention m : c.getMentionsInTextualOrder()){
                String clust2 = "";
                tks = document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class);
                List<CoreLabel> tks2=new ArrayList<CoreLabel>(tks);
                for(int i = m.startIndex-1; i < m.endIndex-1; i++)
                    clust2 += tks.get(i).get(TextAnnotation.class) + " ";
                clust2 = clust2.trim();
                //don't need the self mention
                if(clust.equals(clust2)||!(PronounHashMap.getPronounHashmap().containsKey(clust2.toLowerCase())))
                    continue;

                System.out.println("\t" + clust2);
                System.out.println("NOW SENTENCE IS "+ tks2.toString());
                System.out.println("cluster label IS "+ clustLabel.toString());
                for(int i = m.startIndex-1; i < m.endIndex-1; i++){
                    tks2.remove(i);
                }
                System.out.println("AFTER REMOVING NOW SENTENCE IS "+ tks2.toString());
                //tks2.addAll(m.startIndex-1, clustLabel);
                CoreLabel cl=new CoreLabel();
                cl.setValue(clust);//akaranam see this method doc
                tks2.add(m.startIndex-1, cl);
                String newSentence =AnnotationUtils.tokensToString(tks2);
                CoreMap cmap = null;
                //document.get(SentencesAnnotation.class).set(m.sentNum-1, cmap);
                CoreLabel cl3=new CoreLabel();
                cl3.setOriginalText(clust2);
                
                document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class).set(m.startIndex-1, cl3);
                //cmap= (CoreMap) new Map();
               // cmap =tks2.
               // tks2. //convert this into map and store
                System.out.println("AFTER ADDING NOW SENTENCE IS "+ tks2.toString());
                
                
            }
        }

	}

}
