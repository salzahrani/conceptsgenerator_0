package tools;
import java.io.*;

/**
 * Created by sultanalzahrani on 6/9/17.
 */
public class MakingAllFilesAscii
{
    public static void main(String[] args)
    {
        // (1) reading punch of files...
        //String folderName = "/corpus/raw/json/";
        //System.out.print(Paths.get(".").toAbsolutePath().normalize().toString());
        //System.out.println("Working Directory = " +
        //        System.getProperty("user.dir"));

        String folderName = "./corpus/raw/json/";
        File folder = new File(folderName);
        listFilesForFolder(folder,folderName);


    }

    public static void listFilesForFolder(final File folder,String path)
    {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry, path);
            } else {
                System.out.println("Getname: " + fileEntry.getName());
                String extension = fileEntry.getName();
                String[] parts = extension.split("\\.");
                extension = parts[1];
                System.out.println("File extension: " + extension);
                if (extension.equals("json"))
                {
                    // read file content
                    String content = readFile_as_Text(path+fileEntry.getName());
                    String ascii_content = fixTextToAscii(content);
                    writeToFile( path, parts[0]+"_normalized."+parts[1],  content);


                }


            }
        }
    }

    public static void writeToFile(String path, String fileName, String content)
    {
        try {
            File fout = new File(path + fileName);
            FileOutputStream fos = new FileOutputStream(fout);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(content);
            osw.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String fixTextToAscii(String content)
    {

        return content.replaceAll("\\P{InBasic_Latin}", "");
    }

    public  static String readFile_as_Text(String fileName)
    {
        BufferedReader br = null;
        FileReader fr = null;
        StringBuffer sb = new StringBuffer();

        try {

            fr = new FileReader(fileName);
            br = new BufferedReader(fr);

            String sCurrentLine;

            br = new BufferedReader(new FileReader(fileName));

            while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine);
                sb.append(sCurrentLine + "\n");
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
        return sb.toString();

    }


}