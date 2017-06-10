package tools;

/**
 * Created by sultanalzahrani on 6/8/17.
 */

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
//import java.nio.file.Paths;
import java.util.Arrays;
public class readJsonTest
{
    public static void main(String[] args)
    {



        String JSON_FILE = "/Users/sultanalzahrani/IdeaProjects/conceptsgenerator/corpus/raw/json/test.json";
        try {
        InputStream fis_0 = new FileInputStream(JSON_FILE);

        int i;
        char c;
        while((i = fis_0.read())!=-1)
        {

                // converts integer to character
                c = (char)i;

                // prints character
                System.out.print(c);
        }
        fis_0.close();
        //create JsonReader object




            JsonReader jsonReader = Json.createReader(new FileReader(JSON_FILE));



            JsonObject jsonObject = jsonReader.readObject();


            jsonReader.close();
            fis_0.close();


            System.out.println(">>>>>   " + jsonObject.getInt("id"));
            System.out.println(">>>>>   " + jsonObject.getString("name"));
            System.out.println(">>>>>   " + jsonObject.getBoolean("permanent"));
            System.out.println(">>>>>   " + jsonObject.getString("role"));

//reading arrays from json
            JsonArray jsonArray = jsonObject.getJsonArray("phoneNumbers");
            long[] numbers = new long[jsonArray.size()];
            int index = 0;
            for(JsonValue value : jsonArray){
                numbers[index++] = Long.parseLong(value.toString());
                System.out.println(">>>>>   " + numbers[index-1]);
            }


//reading inner object from json object
            System.out.println(jsonObject.getJsonObject("address"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }
}


