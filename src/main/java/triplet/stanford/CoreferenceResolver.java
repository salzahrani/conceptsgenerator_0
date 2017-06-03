package triplet.stanford;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


public class CoreferenceResolver {
	
	Properties props;
	StanfordCoreNLP pipeline;
	private static CoreferenceResolver instance;
	private CoreferenceResolver(){
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    pipeline = new StanfordCoreNLP(props);
	    
	}
	
	public static synchronized CoreferenceResolver getInstance(){
		if (instance == null)
			instance = new CoreferenceResolver();
		return instance;
	}
	
	public static void main(String[] args) {
		// read some text in the text variable
	    String text = "O God, Revealer of the book, Disperser of the clouds, Defeater of the  parties, defeat the Crusaders, and their apostate allies.� O God, make  them and their equipment easy booty for Muslims.� O God, destroy them and  shake them. O God, You are the one who helps us and the one who assists us,  with Your power we move and by Your power we fight.";
	    //text= "On 12 Jumada al-Awwal  1430, corresponding to 7 May 2009, and as a part of the movements of the  enemies of God aimed at assassinating the leaders of mujahidin in Somalia, a  gang belonging to the apostate Sharif government militias attempted to  assassinate a field commander of the Mujahidin Youth Movement while his car was  passing by one of the streets of Mogadishu. Their attempt was doomed to fail,  praise and gratitude be to God. This incident took place when a one of the cars  opened fire on the car that was driven by the commander. The lion replied by  carrying out a defensive maneuver that forced the enemy to flee. The commander  continued on his path, only to find another ambush set by the enemy. The  commander exchanged fire with the militias for few minutes. This led to the  eruption of an armed clash between the heroes of Mujahidin Youth Movement and  the apostate militias that lasted all day long. Praise and gratitude be to God,  victory was for the mujahidin. ,2012-05-23 16:14:43.341232,";
		//text = "On Wednesday,14 Rabi al-Awwal 1430,corresponding to 11 March 2009, the Detonations Brigade belonging to the Mujahidin Youth Movement [MYM] were able, with the grace of God, to target the private security official of Ali Muhammad Gedi and Nur Ade, the former prime ministers in the government of apostasy that was headed by the apostate Abdullahi Yusuf.   The blessed operation was carried out at 0900 hours Mogadishu local time.  It claimed the lives of the so-called Ubayd Mahmud Muhammad, the security official of the abovementioned, and a number of his bodyguards through the blowing up of their vehicle by a blessed explosive device planted by the mujahidin on the side of the road in the Shups area in the Islamic State of Banadir (Mogadishu).  Praise and thanks be to God, Lord of the Worlds.";
	    //text="On Sunday morning, 18  Rabi Al-Awwal 1430, corresponding to 15 March 2009, supplies reached the  apostate militias, and they clashed with the mujahidin in a decisive battle on  two fronts. The residents of the area and our mujahidin brothers from the  Islamic Party took part in these clashes. The battles continued until the  afternoon, and victory was the ally of the mujahidin. The apostate militias  were defeated, and the mujahidin gained booty, which consisted of light and  heavy weapons and ammunition that were brought in from Ethiopia.";
	    
	    //text="On Sunday morning, 18  Rabi Al-Awwal 1430, corresponding to 15 March 2009, supplies reached the  apostate militias, and they clashed with the mujahidin in a decisive battle on  two fronts. The residents of the area and our mujahidin brothers from the  Islamic Party took part in these clashes. The battles continued until the  afternoon, and victory was the ally of the mujahidin. The apostate militias  were defeated, and the mujahidin gained booty, which consisted of light and  heavy weapons and ammunition that were brought in from Ethiopia.";
	    
	    text="O God, Revealer of the book, Disperser of the clouds, Defeater of the  parties, defeat the Crusaders, and their apostate allies.� O God, make  them and their equipment easy booty for Muslims.� O God, destroy them and  shake them. O God, You are the one who helps us and the one who assists us,  with Your power we move and by Your power we fight.�";
	    String output=runCorefResolution(text);
		System.out.println("Input text is : "+text);
		System.out.println("Processed text is : "+output);
		
		/*text = "On Wednesday,14 Rabi al-Awwal 1430,corresponding to 11 March 2009, the Detonations Brigade belonging to the Mujahidin Youth Movement [MYM] were able, with the grace of God, to target the private security official of Ali Muhammad Gedi and Nur Ade, the former prime ministers in the government of apostasy that was headed by the apostate Abdullahi Yusuf.   The blessed operation was carried out at 0900 hours Mogadishu local time.  It claimed the lives of the so-called Ubayd Mahmud Muhammad, the security official of the abovementioned, and a number of his bodyguards through the blowing up of their vehicle by a blessed explosive device planted by the mujahidin on the side of the road in the Shups area in the Islamic State of Banadir (Mogadishu).  Praise and thanks be to God, Lord of the Worlds.";
		output=runCorefResolution(text);
		System.out.println("Input text is : "+text);
		System.out.println("Processed text is : "+output);
		
		text= "On 12 Jumada al-Awwal  1430, corresponding to 7 May 2009, and as a part of the movements of the  enemies of God aimed at assassinating the leaders of mujahidin in Somalia, a  gang belonging to the apostate Sharif government militias attempted to  assassinate a field commander of the Mujahidin Youth Movement while his car was  passing by one of the streets of Mogadishu. Their attempt was doomed to fail,  praise and gratitude be to God. This incident took place when a one of the cars  opened fire on the car that was driven by the commander. The lion replied by  carrying out a defensive maneuver that forced the enemy to flee. The commander  continued on his path, only to find another ambush set by the enemy. The  commander exchanged fire with the militias for few minutes. This led to the  eruption of an armed clash between the heroes of Mujahidin Youth Movement and  the apostate militias that lasted all day long. Praise and gratitude be to God,  victory was for the mujahidin. ,2012-05-23 16:14:43.341232,";
		output=runCorefResolution(text);
		System.out.println("Input text is : "+text);
		System.out.println("Processed text is : "+output);
		
		text="The heroes  carried out the attack after lengthy reconnaissance of enemy, counting it and  its equipment. When all of the required procedures were finished, the heroes  decided to raid them and teach them a lesson they will never forget. At the  start of the battle, God was with his followers, who were able by the grace of  God alone to defeat the quislings of the Cross. The enemy fled from the field,  leaving behind two rotting carcasses of the militia members lying on the  ground, while one of the lions of Islam was martyred while fasting. We ask God  to accept him and make heaven his abode. This battle comes within the Winds of  Victory Campaign, which thanks be to God is going according to the plans of the  brave heroes in the Al-Usra Army in Somalia. Praise and thanks be to  God.";							

	    output=runCorefResolution(text);
		System.out.println("Input text is : "+text);
		System.out.println("Processed text is : "+output);
		
		text = "John helped Mary. He was kind.";
		output=runCorefResolution(text);
		System.out.println("Input text is : "+text);
		System.out.println("Processed text is : "+output);
		
		text = "After Friday prayers, many of these militias attacked the Wabho [as  transliterated] region in Galguduud Province in central Somalia, The mujahidin  were ready for them and a fierce battle broke out there and it is still  raging.,2012-05-23 16:14:42.838848,";							
		output=runCorefResolution(text);
		System.out.println("Input text is : "+text);
		System.out.println("Processed text is : "+output);		
		
		text = "In this blessed operation, two crusader soldiers from these mercenary  troops were injured. Their colleagues then fired at random upon the area. None  of the Muslims were injured as this area is considered to be relatively  deserted. This operation comes within the Winds of Victory Campaign during this  blessed month. Praise and thanks be to God.";							
		output=runCorefResolution(text);
		System.out.println("Input text is : "+text);
		System.out.println("Processed text is : "+output);
		
		text = "A large number of people, journalists, and officials from the MYM led by Shaykh Ali Muhammad Husayn, governor of the Islamic State of Banadir, participated in the punishment implementation event.� Shaykh  Husayn delivered a brief speech congratulating them on the victory that God has achieved for the Muslim people of Somalia.� He also reminded them that,  not too long ago, this place was a military base for the Ethiopian Cross forces and it is in the hands of the mujahidin today.� He called on the people to stand  with the mujahidin for the application of Islamic law and security.,2012-05-23 16:14:51.997157,";									
		output=runCorefResolution(text);
		System.out.println("Input text is : "+text);
		System.out.println("Processed text is : "+output);
		
		text = "On Thursday  morning, 3 Rajab 1430, corresponding to 25 June 2009, a brigade belonging to  the Al-Usra Army in the Islamic Middle Shabelle State attacked a gang from the  apostate government militias who were hiding in the forests of that state and  were spreading corruption. God enabled the brigade to kill the men from the  government militia, and they fled the fighting arena leaving behind most of  their light and heavy weapons, especially three armored vehicles. The mujahidin  continued pursuing them until this evening. Praise and thanks be to  God.";							
		output=runCorefResolution(text);
		System.out.println("Input text is : "+text);
		System.out.println("Processed text is : "+output);
*/
	}
	

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static String runCorefResolution(String input)  {
		System.out.print("\n START TIME IS :");
		printCurrentTimeStamp();
		CoreferenceResolver CoreferenceResolverInstance =getInstance();
		// create an empty Annotation just with the given text
	    Annotation document = new Annotation(input);
	    // run all Annotators on this text
	    CoreferenceResolverInstance.pipeline.annotate(document);
	    String output = "";
	    CoreferenceResolverInstance.replacePronouns(document);
	    output=CoreferenceResolverInstance.extractSentence(document);
	    System.out.print("\n END TIME IS :");
		printCurrentTimeStamp();
		return output;
	}
	
	private  void replacePronouns(Annotation document) {
		// This is the coreference link graph
	    // Each chain stores a set of mentions that link to each other,
	    // along with a method for getting the most representative mention
	    // Both sentence and token offsets start at 1!
	    Map<Integer, CorefChain> graph =document.get(CorefChainAnnotation.class);
	    
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
            //System.out.println("representative mention: \"" + clust + "\" is mentioned by:");
            for(CorefMention m : c.getMentionsInTextualOrder()){
                String clust2 = "";
                tks = document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class);
                for(int i = m.startIndex-1; i < m.endIndex-1; i++)
                    clust2 += tks.get(i).get(TextAnnotation.class) + " ";
                clust2 = clust2.trim();
                //don't need the self mention
                if(clust.equals(clust2)||!(PronounHashMap.getPronounHashmap().containsKey(clust2.toLowerCase())))
                    continue;
                //System.out.println("\t" + clust2);
                document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class).get(m.startIndex-1).set(TextAnnotation.class,clust);
                for(int i = m.startIndex; i < m.endIndex-1; i++)
                    tks.get(i).set(TextAnnotation.class,"");
                              
            }
        }

	}
	
	private String extractSentence(Annotation document) {
		String result = "";
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// System.out.println("Word is "+word);
				result+=" "+word;
			}
		}
		return result;
	}
	
	public static void printCurrentTimeStamp() {
	    DateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
	    System.out.println(format.format(new Date()));
	}
}
