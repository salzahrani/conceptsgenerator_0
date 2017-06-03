package triplet.clearNLP; /**
 * Copyright (C) 2015 Saud Alashri - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Email: salashri at asu.edu
 * This program extracts triplets using ClearNLP
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

//import jregex.Replacer;

//import com.googlecode.clearnlp.demo.DemoNLPDecoder;

public class Main {
	
	//ArrayList<String> classes = new ArrayList<>();
	// ArrayList<String> categories = new ArrayList<>();
	static ArrayList<ArrayList<String>> mainList;
	public static void getTriplets(String fileLocation,String id,String class_label,String category_label,int textid,int storyid, int counter,Writer writer,Writer sub_pobj_writer,Writer obj_pobj_writer) {
		BufferedReader br = null;
		mainList = new ArrayList<ArrayList<String>>();
		try {
			br = new BufferedReader(new FileReader(fileLocation));
			String line = null;
			int sentNo =1;
			while ((line = br.readLine()) != null) {
				StringTokenizer stk = new StringTokenizer(line, "\t");
				ArrayList<String> token = new ArrayList<String>();
				while (stk.hasMoreTokens()) {
					token.add(stk.nextToken());
				}
				mainList.add(token);
				if(line.equals("")){
					extractTriplets(id,class_label,category_label,textid,sentNo,storyid,counter,writer, sub_pobj_writer, obj_pobj_writer);
					sentNo++;
					mainList.clear();
					System.out.println("END OF EXECUTION ");
				}				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void extractTriplets(String id,String class_label,String category_label,int textid,int sentNo,int storyid, int counter,Writer writer,Writer sub_pobj_writer,Writer obj_pobj_writer) throws SQLException, IOException{
		
		for (int i = 0; i < mainList.size(); i++) {
			ArrayList<String> tempArr = mainList.get(i);
			if (tempArr.size() != 0) {
				TripletsClearnlp triplets=new TripletsClearnlp();
				triplets.id =id;
				triplets.textid=textid;
				triplets.sentNo=sentNo;
				triplets.storyid=storyid;
				// get the A0, A1 or A2 label for a line/word
				String temp = tempArr.get(7);
				//If it's subject, i.e. A0
				if (temp != null & temp.contains(":A0")) {
					StringTokenizer stk = new StringTokenizer(temp, ";");
					while (stk.hasMoreTokens()) {
						String nextToken = stk.nextToken();
						if (nextToken.contains(":A0")) {
							// get the line number for the verb to which subject belongs to
							String lineNum = nextToken.substring(0, nextToken.indexOf(":A0"));
							int lineNumber = Integer.parseInt(lineNum) - 1;
							String sub = mainList.get(i).get(2);
							String verb = mainList.get(lineNumber).get(2);
							String objAndPObj= getA1A2(lineNum);							
							String array[] = null;
							String obj="";							
							String objPObj ="";
							if(objAndPObj!=null){
								array=objAndPObj.split(" POBJ:");
								obj = array[0];
								if(array.length>1 && (array[1]!=null)){
									 objPObj= array[1];
								}
							}							
							
							String amLoc = getAMLoc(lineNum);
							String amTmp = getAMTmp(lineNum);
							String pObjOfSub= getPObj3(Integer.toString(i));
							if (sub != null && verb != null && obj != null && !sub.equals("") && !verb.equals("") && !obj.equals("")) {
								
								writer.write(sub);
								writer.write("," + verb);
								writer.write("," + obj);
								writer.write(","+id+"\n");
								//writer.write(","+class_label+","+category_label+"\n");
							//	writer.print(","+classes.get(paragraphNo-1));
							//	writer.print(","+categories.get(paragraphNo-1)+"\n");
								
								triplets.sub=sub;
								//System.out.print((i + 1) + "SUB:" + sub);
								if (pObjOfSub != null) {
									triplets.subPobj=pObjOfSub;
									sub_pobj_writer.write(pObjOfSub+"\n");
							//		System.out.print(" SUB POBJ:" + pObjOfSub);
								}
								else
									sub_pobj_writer.write(""+"\n");
									
								triplets.verb=verb;
								//System.out.print(" VERB:" + verb);
								triplets.obj=obj;
								//System.out.print(" OBJ:" + obj);
								triplets.objPobj=objPObj;
								
								if(objPObj != null)
									obj_pobj_writer.write(objPObj+"\n");
								else
									obj_pobj_writer.write(""+"\n");
							//	System.out.print(" P OBJ:" + objPObj);								
								/*if (pObjOfObj != null) {
									System.out.print(" OBJ PRED:" + pObjOfObj);
								}*/
								if (amLoc != null) {
									triplets.amLoc=amLoc;
								//	System.out.print(" AM-LOC:" + amLoc);
								}
								if (amTmp != null) {
									triplets.amTmp=amTmp;
								//	System.out.print(" AM-TMP:" + amTmp);
								}
								//System.out.println();
								//preparedStatement=this.insertRecordIntoTable(triplets, preparedStatement);
							}
						}						
					}
				}
				if (temp != null & temp.contains(":A1")) {
					StringTokenizer stk = new StringTokenizer(temp, ";");
					while (stk.hasMoreTokens()) {
						String nextToken = stk.nextToken();
						if (nextToken.contains(":A1")) {
							String lineNum = nextToken.substring(0,nextToken.indexOf(":A1"));
							int lineNumber = Integer.parseInt(lineNum) - 1;
							String sub = mainList.get(i).get(2);
							String verb = mainList.get(lineNumber).get(2);
							String objAndPObj = getA2(lineNum);
							String array[] = null;
							String obj="";							
							String objPObj ="";
							if(objAndPObj!=null){
								array=objAndPObj.split(" POBJ:");
								obj = array[0];
								if(array.length>1 && (array[1]!=null)){
									 objPObj= array[1];
								}
							}							
							String amLoc = getAMLoc(lineNum);
							String amTmp = getAMTmp(lineNum);
							String pObjOfSub= getPObj3(Integer.toString(i));
							//String pObjOfObj= getPObj3(lineNum);
							if (sub != null && verb != null && obj != null && !sub.equals("") && !verb.equals("") && !obj.equals("")) {
							
								//writer.print(paragraphNo+"," + counter++);
								writer.write(sub);
								writer.write("," + verb);
								writer.write("," + obj);
								writer.write(","+id+"\n");
								//writer.write(","+class_label+","+category_label+"\n");
							//	writer.print(","+classes.get(paragraphNo-1));
							//	writer.print(","+categories.get(paragraphNo-1)+"\n");
								
								
								triplets.sub=sub;
								//System.out.print((i + 1) + "SUB:" + sub);

								if (pObjOfSub != null) {
									triplets.subPobj=pObjOfSub;
									sub_pobj_writer.write(pObjOfSub+"\n");
								//	System.out.print(" SUB POBJ:" + pObjOfSub);
								}
								else
									sub_pobj_writer.write(""+"\n");
								
								triplets.verb=verb;
								triplets.obj=obj;
								triplets.objPobj=objPObj;

								if(objPObj != null)
									obj_pobj_writer.write(objPObj+"\n");
								else
									obj_pobj_writer.write(""+"\n");
								//System.out.print(" VERB:" + verb);
								//System.out.print(" OBJ:" + obj);
							//	System.out.print(" P OBJ:" + objPObj);
								/*if (pObjOfObj != null) {
									System.out.print(" OBJ PRED:" + pObjOfObj);
								}*/
								if (amLoc != null) {
									triplets.amLoc=amLoc;
								//	System.out.print(" AM-LOC:" + amLoc);
								}
								if (amTmp != null) {
									triplets.amTmp=amTmp;
								//	System.out.print(" AM-TMP:" + amTmp);
								}
														
							}
						}
					}
				}				
			}
		}
		
	
	}
	
	public static String getA1A2(String lineNum) {
		String result = null;
		for (int i = 0; i < mainList.size(); i++) {
			ArrayList<String> tempArr = mainList.get(i);
			if (tempArr.size() != 0) {
				// get A0, A1 or A2?
				String temp = tempArr.get(7);
				// get lower case label such as prep, pobj, or dobj?
				String prepo =tempArr.get(6);
				// get the upper case label such as NNP, VBD?
				String isverb =tempArr.get(3);
				// if the line contains a direct (A1) or an indirect object (A2)
				if (temp != null & (temp.contains(lineNum+":A1")||temp.contains(lineNum+":A2"))) {
					//getPObj(lineNum);//not sure y it is called. Need to remove
					if (prepo != null & (prepo.contains("prep"))) {
						return mainList.get(i).get(2)+" POBJ:"+getPObj4(tempArr.get(0));
					}
					if (isverb != null & (isverb.contains("VB"))) {
						return getA1A2ForVerbs(tempArr.get(0));
					}
					return mainList.get(i).get(2);
				}
			}
		}		
		return result;
	}
	
	public static String getA1A2ForVerbs(String lineNum) {
		String result = null;
		for (int i = 0; i < mainList.size(); i++) {
			ArrayList<String> tempArr = mainList.get(i);
			if (tempArr.size() != 0) {
				String temp = tempArr.get(7);
				String prepo =tempArr.get(6);
				String isverb =tempArr.get(3);
				if (temp != null & (temp.contains(lineNum+":A1")||temp.contains(lineNum+":A2"))) {
					//getPObj(lineNum);//not sure y it is called. Need to remove
					if (prepo != null & (prepo.contains("prep"))) {
						return mainList.get(i).get(2)+" POBJ:"+getPObj4(tempArr.get(0));
					}					
					return mainList.get(i).get(2);
				}
			}
		}		
		return result;
	}
	
	public static String getA2(String lineNum) {
		for (int i = 0; i < mainList.size(); i++) {
			ArrayList<String> tempArr = mainList.get(i);
			if (tempArr.size() != 0) {
				String temp = tempArr.get(7);
				if (temp != null & (temp.contains(lineNum+":A2"))) {
					String pObj=getPObj4(tempArr.get(0));
					if(pObj!=null)
					return mainList.get(i).get(2)+" POBJ:"+pObj;
					else
						return mainList.get(i).get(2);
				}
			}
		}
		return null;
	}
	
	public static String getAMLoc(String lineNum) {
		for (int i = 0; i < mainList.size(); i++) {
			ArrayList<String> tempArr = mainList.get(i);
			if (tempArr.size() != 0) {
				String temp = tempArr.get(7);
				if (temp != null & (temp.contains(lineNum+":AM-LOC"))) {
					return mainList.get(i).get(2)+" "+getAMLocValue(mainList.get(i).get(0));
				}
			}
		}
		return null;
	}
	
	public static String getAMTmp(String lineNum) {
		for (int i = 0; i < mainList.size(); i++) {
			ArrayList<String> tempArr = mainList.get(i);
			if (tempArr.size() != 0) {
				String temp = tempArr.get(7);
				if (temp != null & (temp.contains(lineNum+":AM-TMP"))) {
					String tmpValue=getAMTmpValue(mainList.get(i).get(0));
					if(tmpValue!=null){
						return mainList.get(i).get(2)+" "+tmpValue;
					}
					else{
						return mainList.get(i).get(2);
					}						
				}
			}
		}
		return null;
	}
	
	public static String getAMLocValue(String lineNum) {
		for (int i = 0; i < mainList.size(); i++) {
			ArrayList<String> tempArr = mainList.get(i);
			if (tempArr.size() != 0) {
				String temp = tempArr.get(5);
				if (temp != null & (temp.equals(lineNum))) {
					return mainList.get(i).get(2);
				}
			}
		}
		return null;
	}
	
	public static String getAMTmpValue(String lineNum) {
		for (int i = 0; i < mainList.size(); i++) {
			ArrayList<String> tempArr = mainList.get(i);
			if (tempArr.size() != 0) {
				String temp = tempArr.get(5);
				if (temp != null & (temp.equals(lineNum))) {
					return mainList.get(i).get(2);
				}
			}
		}
		return null;
	}
	
	
	
	public static String getPObj4(String lineNum) {
		String returnValue=null;
		String amod=null;
		for (int i = Integer.parseInt(lineNum); i < mainList.size(); i++) {
			ArrayList<String> tempArr = mainList.get(i);
			if (tempArr.size() != 0) {
				String temp = tempArr.get(6);
				if (temp != null & (temp.contains("pobj")) &(tempArr.get(5).contains(lineNum))) {
					returnValue=mainList.get(i).get(2);					
					amod=getAmodForPObj(tempArr.get(0));
					if(amod!=null){
						returnValue=amod + " "+returnValue;								
					}
					return returnValue;
				}
			}
		}
		return null;
	}
	
	public static String getAmodForPObj(String lineNum) {
		for (int i = 0; i < Integer.parseInt(lineNum); i++) {
			ArrayList<String> tempArr = mainList.get(i);
			if (tempArr.size() != 0) {
				String temp = tempArr.get(6);
				if (temp != null & (temp.contains("amod")) &(tempArr.get(5).contains(lineNum))) {
					return mainList.get(i).get(2);					
				}
			}
		}
		return null;
	}
	
	/*
	 * this method is created to extract the predicate object incase the obj or subject contains
	 * a preposition attached to it. This has to be a recursive call
	 */
	/*
	 * checks if a subject or object has preposition, if present then
	 * get the corresponding pobj
	 */
	
	public static String getPObj3(String lineNum) {
		int prepoLine= Integer.parseInt(lineNum);
		ArrayList<String> tempArr = mainList.get(prepoLine);
		if (tempArr.size() != 0) {
			String temp = tempArr.get(6);
			String num =Integer.toString(prepoLine);
			if (temp != null & (temp.contains("prep")||temp.contains("agent")) &(tempArr.get(5).contains(num))) {
				return getPObj4(tempArr.get(0));				
			}
		}
		prepoLine= Integer.parseInt(lineNum)+1;
		tempArr = mainList.get(prepoLine);
		if (tempArr.size() != 0) {
			String temp = tempArr.get(6);
			String num =Integer.toString(prepoLine);
			if (temp != null & (temp.contains("prep")) &(tempArr.get(5).contains(num))) {
				String prepo = tempArr.get(2);
				return prepo+" "+getPObj4(tempArr.get(0));
			}
			if (temp != null & (temp.contains("det"))){
				tempArr = mainList.get(prepoLine+1);
				if (tempArr.size() != 0) {
					return tempArr.get(2);
				}
			}
		}
		return null;
	}
	

	public static void main(String[] args) throws IOException {

		parseIt();

	}

	public static void parseIt() throws IOException
	{

		//PrintWriter writer = new PrintWriter(new FileWriter("coded_triplets.csv"));
		String out_path="./corpus/triplets/";
		FileOutputStream fos = new FileOutputStream(out_path+"triplets_ClearNLP.csv");

		Writer out = new OutputStreamWriter(fos, "UTF8");
		out.write("Subject,Verb,Object,id\n");
		//PrintWriter sub_pobj_writer= new PrintWriter(new FileWriter("coded_sub_pobj.txt"));
		out_path="./corpus/triplets/";
		FileOutputStream sub_pobj_fos = new FileOutputStream(out_path+"sub_pobj_ClearNLP.txt");
		Writer sub_pobj_out = new OutputStreamWriter(sub_pobj_fos, "UTF8");

		//	PrintWriter obj_pobj_writer = new PrintWriter(new FileWriter("coded_obj_pobj.txt"));
		out_path= "./corpus/triplets/" ;
		FileOutputStream obj_pobj_fos = new FileOutputStream(out_path+"obj_pobj_ClearNLP.txt");
		Writer obj_pobj_out = new OutputStreamWriter(obj_pobj_fos, "UTF8");
		System.out.println("Getting to File (Sultan test...)...");
		getInputStrings(out, sub_pobj_out, obj_pobj_out);
		out.close();
		sub_pobj_out.close();
		obj_pobj_out.close();
	}
	
	
	public static ResultSet getInputStrings(Writer writer,Writer sub_pobj_writer,Writer obj_pobj_writer) {
		System.out.println("I'm inside... (Sultan test...)...");
		triplet.nlp.DemoNLPDecoder demoNLPDecoder = triplet.nlp.DemoNLPDecoder.getInstance();
		String fileLocation = "./src/main/java/triplet/nlp/output.txt";
		//String fileLocation = "/Users/sultanalzahrani/Projects/ecliplseworkspkace/NLP/output.txt";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
       
        String textcoded;
        String output;
        
        long start_time=0;
        try {            
         
            //classes.txt is an input but unknown
            InputStream in = new FileInputStream("classes.txt");
			String line="";
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			  
			
            
            int i=1;
            String id; //unique id for all docs
            int textid;
            int storyid; //not needed
            String path="./corpus/Docs_Coeref_resovled/";
            File folder = new File(path); 
            
    		File[] listOfFiles = folder.listFiles();
    		System.out.println("Please let me how many files you have: "+listOfFiles.length );
    		int counter=1;
    		for (int j = 0; j < listOfFiles.length; j++) {
    			  File file = listOfFiles[j];
    			  System.out.println("i: " + i+ ": name: "+file.getName());
    			  
    			  if (file.isFile() && file.getName().endsWith(".txt")) {
    				  //id++;
    				  counter=1;
    				  String fileName,text="", s;
    				  fileName = file.getName();
    				  String[] parts= fileName.split("\\.");
    				 // System.out.println("Here-----------"+j); 
    				  id = parts[0];//This is DOC id change it //Integer.parseInt(parts[0]);//id_labels[0]);//sentenceName[1]);
    				 // System.out.println("+++++++ id: "+paragraphNo+" class: "+class_label+" category: "+category_label);
    				  in = new FileInputStream(path + file.getName());
    				  line="";
    				  reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    						
    				  while ((line = reader.readLine()) != null) 
    					  text = line+text;
    				  System.out.println(text);
    				  reader.close();
    				  System.out.println("text_of_file: " + text);
    				  //paragraphNo = 12;//rs.getInt("paragraph");
    					textid = 12;//rs.getInt("textid");
    					storyid =12;//rs.getInt("id");
    					
    					textcoded =text;
    							
    							//rs.getString("textcoded");
    				//	System.out.println("textid: " + textid);
    					System.out.println("Input text is : " + textcoded+"text id : "+textid);
    				//	textcoded=Triplet.removePrefixedStrings(textcoded);
    			
    					
    					output = triplet.stanford.CoreferenceResolver.runCorefResolution(textcoded);
    					System.out.println("Output of the Coreference Resolver: "+ output);
    					//change path in DemoNLPDecoder getInstance
    					demoNLPDecoder.runNLPDecoder(output);
    					System.out.println("before getTriplet..."+ fileLocation);
    					
    					getTriplets(fileLocation,id,"xx","xx",textid,storyid,counter,writer, sub_pobj_writer, obj_pobj_writer);
    				//	System.out.println("Processed text is : " + output);
    					System.out.println("********* Finished sent # "+i+" **************");
    					i++;
    				  
    			  }
    			  
    		}
            

    		
    		
        } catch (Exception e){//SQLException ex) {
               // Logger lgr = Logger.getLogger(DB.class.getName());
              //  lgr.log(Level.SEVERE, ex.getMessage(), ex);
        			System.out.println(e);
        } finally {
            try {
            	if (rs != null) {
					rs.close();
				}
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        } 
        long end_time = System.currentTimeMillis();
		long difference = end_time-start_time;
		//System.out.println("Total time taken : " + difference);
		return rs;
    }
	
	private  static String removePrefixedStrings(String input) {
		String output=input;
		if(input.contains("--")){
			String out[];
		//	System.out.println(input);
			//System.out.println(input.split("--")[0]);
			out=input.split("--");
			if(out.length==0)
				output=null;
			else if(out.length==1)
				output=new String(input.split("--")[0]);
			else if (out.length==2)
				output=new String(input.split("--")[1]);
			else
				output = new String(input.replace("--", ","));
		}
		//System.out.println("***********output*************" + output);
		return output;
	}
	private Connection  createConnection() {
		String url = "jdbc:postgresql://hdshcresearch.asu.edu:5432/aqp";
        String user = "webserver";
        String password = "research.HDSHC.postgres";
        Connection con =null;
        
		
		try {
			con = DriverManager.getConnection(url, user, password);			
			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	private PreparedStatement createPreparedStatement(Connection con){
		PreparedStatement preparedStatement=null;
		String insertTableSQL = "INSERT INTO triplets_clearnlp_isil " +
				"(paragraph_no, textid, storyid, sent_no, sub, sub_pobj, verb, obj,obj_pobj, am_tmp, am_loc, field1, field2, field3) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?);";

		try {
			preparedStatement = con.prepareStatement(insertTableSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
		
	}
	
	private PreparedStatement closePreparedStatement(PreparedStatement preparedStatement) {
		try {
			if (preparedStatement != null) {
				preparedStatement.close();
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
	}
	
	private Connection closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	private PreparedStatement executePreparedStatement(PreparedStatement preparedStatement) {
		try {
			preparedStatement.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
	}
	
	private PreparedStatement  insertRecordIntoTable(TripletsClearnlp triplets,
			PreparedStatement preparedStatement) throws SQLException {
		try {
			//preparedStatement.setInt(1, triplets.id);
			preparedStatement.setInt(2, triplets.textid);
			preparedStatement.setInt(3, triplets.storyid);
			preparedStatement.setInt(4, triplets.sentNo);
			preparedStatement.setString(5, triplets.sub);
			preparedStatement.setString(6, triplets.subPobj);
			preparedStatement.setString(7, triplets.verb);
			preparedStatement.setString(8, triplets.obj);
			preparedStatement.setString(9, triplets.objPobj);
			preparedStatement.setString(10, triplets.amTmp);
			preparedStatement.setString(11, triplets.amLoc);
			preparedStatement.setString(12, "");
			preparedStatement.setString(13, "");
			preparedStatement.setString(14, "");
			// execute insert SQL statement
			preparedStatement.addBatch();
			System.out.println("Record is inserted into DBUSER table!");			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return preparedStatement;
	}
 
}
