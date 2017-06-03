/**
 * Copyright (C) 2015 Saud Alashri - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Email: salashri at asu.edu
 * This program extract triplets using Alchemyapi
 */


package triplet.alchamy;

import com.alchemyapi.api.*;

import org.xml.sax.SAXException;
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

public class Main {

	static ArrayList<String> classes = new ArrayList<String>();
	static ArrayList<String> categories = new ArrayList<String>();
	static ArrayList<String> sentences = new ArrayList<String>();
	static ArrayList<Integer> id_list = new ArrayList<Integer>();
	static int global_counter=0;
	public static void main(String[] args)
			throws IOException, SAXException,
			ParserConfigurationException, XPathExpressionException
	{

        parseIt();
	}
    public static void parseIt() throws IOException, SAXException,
    ParserConfigurationException, XPathExpressionException
    {

        // Create an AlchemyAPI object.
        AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile("./src/main/java/triplet/alchamy/keyts/api_key.txt");

        String path="./corpus/Docs_Coeref_resovled/";
        File folder = new File(path);

        FileOutputStream fos = new FileOutputStream("./corpus/triplets/triplets_alchamy.csv");
        Writer out = new OutputStreamWriter(fos, "UTF8");

        int counter=0;

        File[] listOfFiles = folder.listFiles();
        for (int j = 0; j < listOfFiles.length; j++)
        {
            File file = listOfFiles[j];
            System.out.println("File Name::: "+ file.getName());

            counter++;
            String fileName,sentStr="", s;
            fileName = file.getName();
            String[] parts= fileName.split("\\.");
            String[] sentenceName=parts[0].split("_");

            int docid = Integer.parseInt(sentenceName[1]);//Integer.parseInt(id_labels[0]);

            InputStream in = new FileInputStream(path + file.getName());
            String line="";
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            //			  line="";
            // reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null)
                sentStr = line+sentStr;
            System.out.println(sentStr);
        }
        counter=0;
        String last_sentence="";
        for (int j = 0; j < listOfFiles.length; j++) {
            File file = listOfFiles[j];

            if (file.isFile() && file.getName().endsWith(".txt")) {

                counter++;
                String fileName,sentStr="", s;
                fileName = file.getName();
                String[] parts= fileName.split("\\.");
                String[] sentenceName=parts[0].split("_");

                int docid = Integer.parseInt(sentenceName[1]);//Integer.parseInt(id_labels[0]);

                InputStream in = new FileInputStream(path + file.getName());
                String line="";
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                //				  line="";
                // reader = new BufferedReader(new InputStreamReader(in));
                while ((line = reader.readLine()) != null)
                    sentStr = line+sentStr;

                sentStr=sentStr.replaceAll("[^A-Za-z0-9 ,;]", " ");

                sentStr=sentStr.trim();

                if(Character.isDigit(sentStr.charAt(0)))
                    sentStr= "A "+sentStr;

                if(!Character.isLetterOrDigit(sentStr.charAt(0)))
                    sentStr=sentStr.substring(1);

                if(Character.isAlphabetic(sentStr.charAt(0)))
                    sentStr= sentStr.substring(0, 1).toUpperCase() + sentStr.substring(1);
                // sentStr = sentStr.substring(0, 2).toUpperCase() + sentStr.substring(1);

                if(!sentStr.contains("."))
                    sentStr=sentStr+" .";

                reader.close();

                if(sentences.contains(sentStr))  //to avoid duplicates
                {
                    counter--;
                    continue;
                }

                sentences.add(sentStr);
                id_list.add(docid);

                //// call the api every 20 sentences
                if(counter==20)
                {
                    call_api(alchemyObj,out);
                    //initialize all array lists
                    sentences = new ArrayList<String>();
                    //id_list = new ArrayList<>();
                    counter=0;
                }

            }
        }

        //call the api for last group of sentences
        if(sentences.size()>0)
            call_api(alchemyObj,out);

        out.close();
    }
	public static void	call_api(AlchemyAPI alchemyObj,Writer out) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException{

		String all_sentences="";
		for(int i=0;i<sentences.size();i++){
			all_sentences = all_sentences+"  "+ sentences.get(i);
		}

		// Extract a ranked list of relations from a text string.
		Document  doc = alchemyObj.TextGetRelations(all_sentences);
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

			System.out.println("--------------------------- "+list_counter);




			if (nNode.getNodeType() == Node.ELEMENT_NODE) {


				Element eElement = (Element) nNode;
				String out_sentence= eElement.getElementsByTagName("sentence").item(0).getTextContent();
				if(!last_sentence.equals(out_sentence))
				{
					list_counter++;
					last_sentence=out_sentence;
					global_counter++;
					System.out.println("global counter : " + global_counter);
				}

				System.out.println("sentence : " + out_sentence);

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
					for(int i=0;i<sentences.size();i++){
						String s1=sentences.get(i).replaceAll("\\s+","");
						String s2 = out_sentence.replaceAll("\\s+","");
						System.out.println(sentences.get(i));
						System.out.println(out_sentence);
						if(s1.equals(s2))
						{
							index=i;
							break;
						}
					}
					out.write(","+id_list.get(index)+"\n");  //list_counter


				}

			}

		}

	}







	// utility function
	public static String getFileContents(String filename)
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
	public static String getStringFromDocument(Document doc) {
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
