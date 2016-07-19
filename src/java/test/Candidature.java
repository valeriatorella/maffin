/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Valeria
 */
public class Candidature extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            response.setContentType("text/html;charset=UTF-8");
            String action = request.getParameter("action");
            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost:3306/maffin";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "");
            PrintWriter out = response.getWriter();

            if (action.equals("insCand")) {
                String dip_doc = (String)request.getParameter("dip_doc");
                String cf_doc = (String)request.getParameter("cf_doc");
                String dip_ins = (String)request.getParameter("dip_cod");
                String id_ins = (String)request.getParameter("id_ins");
                
                String query = "INSERT INTO candidature (ID_INS, COD_FIS, COD_DIP_AFF, COD_DIP_CAP) VALUES " 
                                +"('"+id_ins+"','"+cf_doc+"',"+dip_doc+","+dip_ins+")";
                
                Statement st = conn.createStatement();
                boolean rs = st.execute(query);
            }
            
            //elenco candidature filtrate per dipartimento capofila. ATTIVITà CONVALIDARE CANDIDATURE
            if (action.equals("getCandByCap")) {
                String dip_cap = (String)request.getParameter("dip_cap");
                
                String query;
                query = "SELECT docenti.RUOLO_DOC_COD,docenti.NOME,docenti.COGNOME,docenti.AREA_SETT_SSD, " +
                        "offerta_formativa.PDS_DES,offerta_formativa.NOME_CDS,offerta_formativa.TIPO_CORSO_COD, "+
                        "offerta_formativa.AF_GEN_COD,offerta_formativa.DES, offerta_formativa.ORE_ATT_FRONT " +
                        "FROM candidature " +
                        "JOIN offerta_formativa ON offerta_formativa.ID_INS = candidature.ID_INS "+
                        "JOIN docenti ON candidature.COD_FIS = docenti.CODICE_FISCALE "+
                        "WHERE candidature.STATO = 'ATTESA' "+
                        "AND candidature.COD_DIP_CAP = "+dip_cap;
                
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                String temp = "";
                JSONArray resArray = new JSONArray();
                JSONObject result;
                
                while (rs.next()) {
                    result = new JSONObject();
                    result.put("RUOLO_DOC_COD", rs.findColumn("RUOLO_DOC_COD"));
                    result.put("NOME", rs.findColumn("NOME"));
                    result.put("COGNOME", rs.findColumn("COGNOME"));
                    result.put("AREA_SETT_SSD", rs.findColumn("AREA_SETT_SSD"));
                    result.put("PDS_DES", rs.findColumn("PDS_DES"));
                    result.put("NOME_CDS", rs.findColumn("NOME_CDS"));
                    result.put("TIPO_CORSO_COD", rs.findColumn("TIPO_CORSO_COD"));
                    result.put("AF_GEN_COD", rs.findColumn("AF_GEN_COD"));
                    result.put("DES", rs.findColumn("DES"));
                    result.put("ORE_ATT_FRONT", rs.findColumn("ORE_ATT_FRONT"));
                    resArray.add(result);
                }
                temp = "<div> '"+resArray.toString()+"' </div>";
                out.write(temp);
                System.out.println(resArray);
            }
            
            conn.close();
            out.close();
            

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Docenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Docenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Docenti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
