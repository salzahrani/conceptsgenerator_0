package triplet.reverb;

/**
 * Copyright (C) 2015 Saud Alashri - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Email: salashri at asu.edu
 * This program extracts triplets using Reverb
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import edu.washington.cs.knowitall.nlp.ChunkedSentence;

/* String -> ChunkedSentence */
import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;

/* The class that is responsible for extraction. */
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;

/* The class that is responsible for assigning a confidence score to an
 * extraction.
 */
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
import edu.washington.cs.knowitall.extractor.conf.ReVerbOpenNlpConfFunction;

/* A class for holding a (arg1, rel, arg2) triple. */
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;


/* A class for normalizing a (arg1, rel, arg2) triple. */
import edu.washington.cs.knowitall.normalization.BinaryExtractionNormalizer;

public class Main {

    public static void main(String[] args) throws Exception {


        parseIt();

    }

    public static void parseIt()throws Exception
    {
        System.out.println("Running Reverb...");
        String path="./corpus/raw/";

        File folder = new File(path);
        String out_path="./corpus/triplets/";

        FileOutputStream fos = new FileOutputStream(out_path+"triplets_Reverb.csv");
        Writer out = new OutputStreamWriter(fos, "UTF8");
        out.write("Subject,Verb,Object,id\n");
        File[] listOfFiles = folder.listFiles();
        int counter=0;
        for (int j = 0; j < listOfFiles.length; j++) {
            System.out.println("File number: "+j);
            File file = listOfFiles[j];

            if (file.isFile() && file.getName().endsWith(".txt")) {

                counter++;
                String fileName,sentStr="";
                fileName = file.getName();
                String[] parts= fileName.split("\\.");
                StringBuilder  stringBuilder = new StringBuilder();
                String id =parts[0];

                InputStream in = new FileInputStream(path+"/"+file.getName());
                String line="";
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//
                while ((line = reader.readLine()) != null)
                    stringBuilder.append(line);
                stringBuilder.append(" "); // append space

                reader.close();
                sentStr = stringBuilder.toString();


                OpenNlpSentenceChunker chunker = new OpenNlpSentenceChunker();
                ChunkedSentence sent = chunker.chunkSentence(sentStr);

                ReVerbExtractor reverb = new ReVerbExtractor();
                ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
                BinaryExtractionNormalizer bn=new BinaryExtractionNormalizer();
                // int counter=1;
                for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {
                    String sub=bn.normalizeArgument(extr.getArgument1()).toString();
                    sub =sub.replace(",", "");
                    out.write(sub); //Subject
                    String verb=bn.normalizeRelation(extr.getRelation()).toString();
                    verb =verb.replace(",", "");
                    out.write(","+ verb); //Verb

                    String obj=bn.normalizeArgument(extr.getArgument2()).toString();
                    obj =obj.replace(",", "");
                    out.write(","+ obj); //Object

                    out.write(","+id);
                    out.write("\n"); //","+class_label+","+category_label+

                }


            }

        }

        out.close();

    }
}
