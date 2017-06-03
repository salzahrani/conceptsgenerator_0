package triplet.alchamy;

import com.alchemyapi.api.*;

import org.xml.sax.SAXException;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

class RelationsTestParagraphs {
	
	static ArrayList<String> classes = new ArrayList<String>();
	static ArrayList<String> categories = new ArrayList<String>();
	static ArrayList<String> sentences = new ArrayList<String>();
	static ArrayList<Integer> id_list = new ArrayList<Integer>();
	static int global_counter=0;
    public static void main(String[] args)
        throws IOException, SAXException,
               ParserConfigurationException, XPathExpressionException
    {
    	
        
    	// Create an AlchemyAPI object.
        AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile("api_key.txt");

        String path="C:/Users/salashri/Desktop/Projects/Co-clustering_stories_concepts/SplittedData/Data11/";
        File folder = new File(path); 
        
        FileOutputStream fos = new FileOutputStream("story_triplets_AlchemyAPI_11.csv"); //coded_triplets_AlchemyAPI.csv");
        Writer out = new OutputStreamWriter(fos, "UTF8");
        
        
        File[] listOfFiles = folder.listFiles();
		int counter=0;
		String last_sentence="";
		for (int j = 0; j < listOfFiles.length; j++) {
			  File file = listOfFiles[j];
			  
			  if (file.isFile() && file.getName().endsWith(".txt")) {
		
				// counter++;
				  String fileName,sentStr="", s;
				  fileName = file.getName();
				  String[] parts= fileName.split("\\.");
				  String id=parts[0];
				//  String[] id_labels=sentenceName[1].split("--");
				 // String[] labels=id_labels[1].split("-");
				 // String class_label=labels[0];
				 // String category_label=labels[1];
				  int paragraph_id = Integer.parseInt(id);//Integer.parseInt(id_labels[0]);
				 // System.out.println("+++++++ id: "+paragraphNo+" class: "+class_label+" category: "+category_label);
				  
				  InputStream in = new FileInputStream(path+"/"+file.getName());
					String line="";
					BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//				  line="";
				 // reader = new BufferedReader(new InputStreamReader(in));
				  while ((line = reader.readLine()) != null) 
					  sentStr = sentStr+line;
				  
				 
				 				  
				  if(!sentStr.contains("."))
					  sentStr=sentStr+". ";
				  
				//  System.out.println(sentStr);
//				  if(last_sentence.equals(sentStr))
//					  { 
//					  sentStr= sentStr.replaceAll(".", "");
//					  sentStr = sentStr+ " a.";
//					  }
//				  
//				  last_sentence=sentStr;
					  
				  reader.close();
				  
				 
				//  classes.add(class_label);
				//  categories.add(category_label);
				//  sentences.add(sentStr);
				 // id_list.add(docid);
				 // counter = StringUtils.countMatches(sentStr,".");
				  String[]sentencesHolder= sentStr.split("\\.");
				  String Sentences="";
				  
				  //// call the api every 20 sentences of a paragraph 
				  if(sentencesHolder.length>=20)
				  {
					  System.out.println("file: "+fileName+" long: "+sentencesHolder.length);
					  for(int i=0;i<sentencesHolder.length;i++){
						counter++;
						if(!sentencesHolder[i].contains("."))
							sentencesHolder[i]=sentencesHolder[i]+" .";
						Sentences +=sentencesHolder[i];
						if(counter==20 || i==sentencesHolder.length-1)
							{
							call_api(alchemyObj,out,Sentences,paragraph_id);
							counter=0;
							  Sentences="";
							}
					  }
					  //initialize all array lists
				//	  classes = new ArrayList<>();
				//	  categories = new ArrayList<>();
					 // sentences = new ArrayList<>();
					 // id_list = new ArrayList<>();
					  
				  }
				  else
				  {
					  System.out.println("file: "+fileName+" short: "+sentencesHolder.length);
					  call_api(alchemyObj,out,sentStr,paragraph_id);
				  }
				  
			  }
		}
		
		//cal the api for last group of sentences
		//if(sentences.size()>0)
			// call_api(alchemyObj,out);
		
		out.close();
    }
		
	public static void	call_api(AlchemyAPI alchemyObj,Writer out,String sentStr,int paragraph_id) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException{
		
		String all_sentences="";
		for(int i=0;i<sentences.size();i++){
			all_sentences = all_sentences+"  "+ sentences.get(i);
		}
        // Extract a ranked list of relations for a web URL.
      //  Document doc = alchemyObj.URLGetRelations("http://www.techcrunch.com/");
       // System.out.println(getStringFromDocument(doc));

        // Extract a ranked list of relations from a text string.
        Document  doc = alchemyObj.TextGetRelations(sentStr);//all_sentences);
           // "Hello there, my name was Bob Jones.  I lived in the U.S.A of America.  " );
        doc.getDocumentElement().normalize();
        
    	//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    	NodeList nList = doc.getElementsByTagName("relation");
    	
    	////////////////////////
    	//System.out.println(getStringFromDocument(doc));
    	 int list_counter=-1;
    	 String last_sentence="";
    	 
    	for (int temp = 0; temp < nList.getLength(); temp++) {
    		
    		Node nNode = nList.item(temp);
    		
    	//	System.out.println("--------------------------- "+list_counter);
    		
    		
    		
    		
    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    			
    			
    			
    			
    			Element eElement = (Element) nNode;
//    			String out_sentence= eElement.getElementsByTagName("sentence").item(0).getTextContent();
//    			if(!last_sentence.equals(out_sentence))
//    			{
//    				list_counter++;
//    				last_sentence=out_sentence;
//    				global_counter++;
//    				System.out.println("global counter : " + global_counter);
//    			}
//    			
    	//		System.out.println("Class: "+classes.get(list_counter)+ " category: "+categories.get(list_counter)+" id: "+id_list.get(list_counter));
//    			System.out.println("sentence : " + out_sentence);
    			
    			if(eElement.getElementsByTagName("object").getLength() >0 && eElement.getElementsByTagName("subject").getLength() >0){
    				
    			//System.out.println("sentence : " + out_sentence);
    			String sub=	((NodeList) eElement.getElementsByTagName("subject").item(0)).item(1).getTextContent();
    			sub=sub.replaceAll(",", "");
    			out.write(sub);
    			
    			String verb=	((NodeList) eElement.getElementsByTagName("action").item(0)).item(3).getTextContent();
    			verb=verb.replaceAll(",", "");
    			out.write("," + verb);
    			
    			String obj=	((NodeList) eElement.getElementsByTagName("object").item(0)).item(1).getTextContent();
    			obj=obj.replaceAll(",", "");
    			out.write("," + obj);
    			int index=-1;
    			
    		//	System.out.println("Size  "+sentences.size());
//    			for(int i=0;i<sentences.size();i++){
//    				String s1=sentences.get(i).replaceAll("\\s+","");
//    				String s2 = out_sentence.replaceAll("\\s+","");
//    				System.out.println(sentences.get(i));
//    				System.out.println(out_sentence);
//    				if(s1.equals(s2))
//    				{
//    					index=i;
//    					break;
//    				}
//    			}
    			out.write(","+paragraph_id+"\n");  //list_counter
    		//	out.write(","+classes.get(index));
    		//	out.write(","+categories.get(index)+"\n");
    			
    			}
    			
    		}
    		
    	}
		
	}		
        
		
    	
    	
    	////////////////////////
    	
        //////System.out.println(getStringFromDocument(doc));

        // Load a HTML document to analyze.
//        String htmlDoc = getFileContents("data/example.html");
//
//        // Extract a ranked list of relations from a HTML document.
//        doc = alchemyObj.HTMLGetRelations(htmlDoc, "http://www.test.com/");
//        System.out.println(getStringFromDocument(doc));
		
//		AlchemyAPI_RelationParams relationParams = new AlchemyAPI_RelationParams();
//		relationParams.setSentiment(true);
//		relationParams.setEntities(true);
//		relationParams.setDisambiguate(true);
//		relationParams.setSentimentExcludeEntities(true);
//		doc = alchemyObj.TextGetRelations("Madonna enjoys tasty Pepsi.  I love her style.", relationParams);
//        System.out.println(getStringFromDocument(doc));
//		
//		relationParams.setSentiment(true);
//		relationParams.setRequireEntities(true);
//		relationParams.setSentimentExcludeEntities(true);
//		doc = alchemyObj.TextGetRelations("Madonna enjoys tasty Pepsi.  I love her style.", relationParams);
//        System.out.println(getStringFromDocument(doc));
    

    // utility function
    private static String getFileContents(String filename)
        throws IOException {
        File file = new File(filename);
        StringBuilder contents = new StringBuilder();

        BufferedReader input = new BufferedReader(new FileReader(file));

        try {
            String line = null;

            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } finally {
            input.close();
        }

        return contents.toString();
    }

    // utility method
    private static String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            return writer.toString();
        } catch (TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
