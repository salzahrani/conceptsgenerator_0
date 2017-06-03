package triplet.clearNLP; /**
 * Copyright (C) 2015 Saud Alashri - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Email: salashri at asu.edu
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {    
    public ResultSet getInputStrings() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String url = "jdbc:postgresql://hdshcresearch.asu.edu:5432/aqp";
        String user = "webserver";
        String password = "research.HDSHC.postgres";
        try {            
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT id, textid, paragraph, code, textcoded, entered, coder FROM codeparagraphsresolved;");
            rs = pst.executeQuery();                        
        } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
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
		return rs;
    }
}