/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exia.textebusiness.ejb;
import java.sql.*;  
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Arnaud RIGAUT
 */
public class DBConnect {
    
    Connection conn = null;
    Statement sta;
    ResultSet re;
    ResultSetMetaData metaBase;
    
    
    
    public DBConnect(){
        
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("jdbc/bdd");
            Connection conn = ds.getConnection();
            System.out.println ("Database connection established");
        } catch (Exception e) {
            // exception handling
        }
    }
    
    public ArrayList<String> request(){
        
        ArrayList<String> dico = new ArrayList<>();
        
        try {  
            Statement stmt=conn.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from dico");  
            while(rs.next()){
                dico.add(rs.getString(0));
            }
            conn.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return dico;
        
    }
    
}
