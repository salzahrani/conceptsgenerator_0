package tools;

/**
 * Created by sultanalzahrani on 6/8/17.
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class JSONParaser {
    public String filePath = "";
    public JsonObject rootObj;
    public JsonArray jsondoc_arr = null;

    public JSONParaser(String pfilePath)
    {
        filePath = pfilePath;
    }

    public JsonObject readFile()   {
        JsonObject jsonObject = null;
        try {
        InputStream fis = new FileInputStream(this.filePath);
        //create JsonReader object
        JsonReader jsonReader = Json.createReader(fis);
        int i;
        char c;
        jsonObject = jsonReader.readObject();
            /*while((i = fis.read())!=-1) {

                // converts integer to character
                c = (char)i;

                // prints character
                System.out.print(c);
            }*/
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        rootObj = jsonObject;
        return jsonObject;

    }

    public Wrapcontentandheading[] readingArray()
    {
        // under rows:
        int i = 0;
        jsondoc_arr = (JsonArray) rootObj.getJsonArray("rows");
        int size = jsondoc_arr.size();

        String[] heading_arr = new String[size];
        String[] content_arr = new String[size];
        Wrapcontentandheading[] h_c_arr = new Wrapcontentandheading[size];
        for(JsonValue value : jsondoc_arr)
        {
            String heading=null,content=null;
            JsonObject o = (JsonObject)value;
            heading  = (String)(o.getString("Headline"));
            System.out.println("h>>>" + heading_arr[i]);
            content = (String)(o.getString("RawText"));
            System.out.println("c>>>" + content_arr[i-1]);

            Wrapcontentandheading w = new Wrapcontentandheading(content,heading);
            h_c_arr[i++] = w;

        }
        return h_c_arr;


    }


}
