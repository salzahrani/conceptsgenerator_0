package tools;

/**
 * Created by sultanalzahrani on 6/8/17.
 */
public class Wrapcontentandheading implements java.io.Serializable
{
    String content;
    String heading;
    int fileID;
    String classlbl;
    public Wrapcontentandheading(String p_content, String p_heading)
    {
        this.content = p_content;
        this.heading = p_heading;
        this.fileID = -1;
        this.classlbl = "";
    }
    public Wrapcontentandheading(String p_content, String p_heading,int pFileID,String pclasslbl)
    {
        this.content = p_content;
        this.heading = p_heading;
        this.fileID = pFileID;
        this.classlbl = pclasslbl;
    }
    @Override
    public String toString()
    {
        int heading_s  = (10>heading.length())?heading.length():10;
        int content_s  = (10>content.length())?content.length():10;

        return "||" + fileID + "||" + classlbl + "||"
                +   heading.substring(0,heading_s) +"||" + content.substring(0,content_s);
    }
}
