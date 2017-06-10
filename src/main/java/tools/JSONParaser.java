package tools;

/**
 * Created by sultanalzahrani on 6/8/17.
 */
import java.io.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import tools.Wrapcontentandheading;

public class JSONParaser {
    public static String fileName = "";
    public static String path = "";

    public static JsonObject rootObj;
    public static JsonArray jsondoc_arr = null;

    public JSONParaser(String pFileName)
    {
        fileName = pFileName;
    }

    public JSONParaser(String pfilePath,String pPath)
    {
        fileName = pfilePath;
        path = pPath;
    }

    public static void main(String[] args)
    {
        doSerialize();
        DoDeserialize();
    }


    public static void doSerialize()
    {
        String[] fileName_lst = {
                "1988-2017-azerbaijan w keywords_normalized.json",
                "1990-2017-moldova w keywords_normalized.json",
                "2004-2017-estonia or latvia w keywords_normalized.json",
                "2010-2017-georgia w keywords_normalized.json",
                "2010-2017-ukraine w keywords - new mapping only_normalized.json",
                "2010-2017-ukraine w keywords_normalized.json",
                "2014-2017-ukraine w keywords_normalized.json"
        };

        path = "./corpus/raw/json/";

        for(String fileName_item: fileName_lst)
        {
            System.out.println("Starting on file: " + fileName_item);
            fileName = fileName_item;
            readFile(); // READING PARSING & SERIALIZE
        }
    }

    public static void DoDeserialize()
    {
        String[] fileName_ser_lst = {
                "1988-2017-azerbaijan w keywords_normalized.ser",
                "1990-2017-moldova w keywords_normalized.ser",
                "2004-2017-estonia or latvia w keywords_normalized.ser",
                "2010-2017-georgia w keywords_normalized.ser",
                "2010-2017-ukraine w keywords - new mapping only_normalized.ser",
                "2010-2017-ukraine w keywords_normalized.ser",
                "2014-2017-ukraine w keywords_normalized.ser"};
        for(String fileName_item: fileName_ser_lst)
        {
            System.out.println("Starting on file: " + fileName_item);
            readSampleOfTheArray(fileName_item); // READING PARSING & SERIALIZE
        }
    }

    public static void readSampleOfTheArray(String fileName_item)
    {
        String ser_path = "./corpus/raw/Serialized/";

        try {
            FileInputStream fileIn = new FileInputStream(ser_path + fileName_item);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Wrapcontentandheading[] o = (Wrapcontentandheading[]) in.readObject();
            System.out.println("File name: " + fileName_item);
            for(int i=0; i<10 && i<o.length;i++)
            {
                System.out.println(o[i].toString());
            }
            in.close();
            fileIn.close();
        }catch(IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static  JsonObject readFile()   {
        JsonObject jsonObject = null;
        try {

            String content  = tools.MakingAllFilesAscii.readFile_as_Text(path + fileName);
            content = content.replaceAll("\r?\n", " ");
            content = content.replaceAll("\\P{InBasic_Latin}", " ");
            JsonReader jsonReader = Json.createReader(new StringReader(content));
            jsonObject = jsonReader.readObject();
            rootObj = jsonObject;
            Wrapcontentandheading[] w_arr  = readingArray();
            jsonReader.close();

            return rootObj;




        } catch (Exception e) {
            System.err.print("Filename (Error...)= " + fileName);
            e.printStackTrace();
        }

        return jsonObject;

    }

    public static Wrapcontentandheading[] readingArray()
    {
        // under rows:
        Wrapcontentandheading[] h_c_arr = null;
        try {
            int i = 0;
            jsondoc_arr = (JsonArray) rootObj.getJsonArray("rows");
            int size = jsondoc_arr.size();
            System.out.println("FileName: " + fileName);
            String classlbl = "";
            switch (fileName)
            {
                case "1988-2017-azerbaijan w keywords_normalized.json":
                    classlbl = "azerbaijan"; break;
                case "1990-2017-moldova w keywords_normalized.json":
                    classlbl = "moldova"; break;
                case "2004-2017-estonia or latvia w keywords_normalized.json":
                    classlbl = "estonia_latvia"; break;
                case "2010-2017-georgia w keywords_normalized.json":
                    classlbl = "georgia"; break;
                case "2010-2017-ukraine w keywords - new mapping only_normalized.json":
                    classlbl = "ukraine_mapping"; break;
                case "2010-2017-ukraine w keywords_normalized.json":
                    classlbl = "ukraine_20102017"; break;
                case "2014-2017-ukraine w keywords_normalized.json":
                    classlbl = "ukraine_2014_2017";break;
                default:
                    classlbl = "ERROR"; break;
            }
            System.out.println("number of document inclosed: " + size);
            String[] heading_arr = new String[size];
            String[] content_arr = new String[size];
            h_c_arr = new Wrapcontentandheading[size];
            for (JsonValue value : jsondoc_arr) {
                String heading = " ", content = " ";
                int fileID =-1;
                JsonObject o = (JsonObject) value;
                heading  = (String)(o.getString("Headline"));
                content = (String)(o.getString("RawText"));
                fileID = (int)(o.getInt("StoryID"));
                Wrapcontentandheading w = new Wrapcontentandheading(content,
                        heading,
                        fileID,
                        classlbl
                        );
                h_c_arr[i++] = w;

            }
            serializeIt(h_c_arr);
            Wrapcontentandheading[] h_c_arr_des = deserializeIt();
        }
            catch(Exception e)
            {
                System.err.print("Filename (Error...)= " + fileName);
                e.printStackTrace();
            }
        // serialize the object


        return h_c_arr;

    }

    public static Wrapcontentandheading[] deserializeIt()
    {
        Wrapcontentandheading[] h_c_arr = null;
        try {
            String fileName_ser = "./corpus/raw/Serialized/"
                    + fileName.substring(0, fileName.lastIndexOf(".")) + ".ser";
            FileInputStream fileIn = new FileInputStream(fileName_ser);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            h_c_arr = (Wrapcontentandheading[]) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return h_c_arr;
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return h_c_arr;
        }
        int size = h_c_arr.length;
        return h_c_arr;
    }

    public static void serializeIt(Wrapcontentandheading[] h_c_arr)
    {
        try {
            String fileName_ser = "./corpus/raw/Serialized/"
                    + fileName.substring(0, fileName.lastIndexOf(".")) + ".ser";
            System.out.println("Serialized file Name = " + fileName_ser);
            FileOutputStream fileOut =
                    new FileOutputStream(fileName_ser);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(h_c_arr);
            out.close();
            fileOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
