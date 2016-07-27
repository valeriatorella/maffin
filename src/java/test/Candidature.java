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
import java.util.HashMap;
import java.util.Map;
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
        
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.addHeader("Access-Control-Max-Age", "3600");
        response.addHeader("Access-Control-Allow-Headers", "x-requested-with");

        try {
            response.setContentType("text/html;charset=UTF-8");
            String action = request.getParameter("action");
            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://ec2-52-18-91-69.eu-west-1.compute.amazonaws.com:3306/maffin";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "MaffinRemoteDB", "Bonita.Repo.2016");
            PrintWriter out = response.getWriter();
            Map<String,String> paramList = new HashMap<>();

            if (action.equals("insCand")) {
                paramList.put("dip_doc", (String) request.getParameter("dip_doc"));
                paramList.put("cf_doc", (String) request.getParameter("cf_doc"));
                paramList.put("ruolo_doc", (String) request.getParameter("ruolo_doc"));
                paramList.put("dip_ins", (String) request.getParameter("dip_cod"));
                paramList.put("id_ins", (String) request.getParameter("id_ins"));
                
                String query = createQuery(action, paramList);

                Statement st = conn.createStatement();
                boolean rs = st.execute(query);
            }

            //elenco candidature filtrate per dipartimento capofila.
            if (action.equals("getCandByCap")) {
                  paramList.put("fase",(String) request.getParameter("fase"));
                  paramList.put("dip_cap",(String) request.getParameter("dip_cap"));
                  
                if (!paramList.get("dip_cap").isEmpty()){
                    
                    String query = createQuery(action, paramList);

                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    String temp = "";
                    JSONArray resArray = new JSONArray();
                    JSONObject result;

                    while (rs.next()) {
                        result = new JSONObject();
                        //temp += "<input type='checkbox' name='candidatura' value='" + rs.getString("ID_CAND") + "'/> ";
                        result.put("ID_CAND", rs.getString("ID_CAND"));
                        result.put("RUOLO_DOC_COD", rs.getString("RUOLO_DOC_COD"));
                        result.put("NOME", rs.getString("NOME"));
                        result.put("COGNOME", rs.getString("COGNOME"));
                        result.put("SETT_DOC_COD", rs.getString("SETT_DOC_COD"));
                        result.put("PDS_DES", rs.getString("PDS_DES"));
                        result.put("NOME_CDS", rs.getString("NOME_CDS"));
                        result.put("TIPO_CORSO_COD", rs.getString("TIPO_CORSO_COD"));
                        result.put("SETT_COD", rs.getString("SETT_COD"));
                        result.put("AF_GEN_COD", rs.getString("AF_GEN_COD"));
                        result.put("DES", rs.getString("DES"));
                        result.put("ORE_ATT_FRONT", rs.getString("ORE_ATT_FRONT"));
                        resArray.add(result);
                    }
                    //out.write(temp);
                    out.println(resArray);
                    System.out.println(resArray);
                }
            }
            if (action.equals("convalidaCand")) {
                paramList.put("fase", (String) request.getParameter("fase"));
                paramList.put("dip_cap", (String) request.getParameter("dip_cap"));
                paramList.put("cand_app", (String) request.getParameter("cand_app").replaceAll("-", ","));
                
                if (!paramList.get("cand_app").isEmpty()){
                    String query = createQuery("convalidaCandApprova", paramList);

                    Statement st = conn.createStatement();
                    boolean rs = st.execute(query);

                    String query1 = createQuery("convalidaCandRifiuta", paramList);;

                    Statement st1 = conn.createStatement();
                    boolean rs1 = st1.execute(query1);
                }
            }
            //elenco candidature filtrate per dipartimento afferenza.
            if (action.equals("getCandByAff")) {
                paramList.put("dip_aff", (String) request.getParameter("dip_aff"));
                
                String query = createQuery(action, paramList);

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                //String temp = "";
                JSONArray resArray = new JSONArray();
                JSONObject result;

                while (rs.next()) {
                    result = new JSONObject();
                    result.put("ID_CAND", rs.getString("ID_CAND"));
                    result.put("RUOLO_DOC_COD", rs.getString("RUOLO_DOC_COD"));
                    result.put("NOME", rs.getString("NOME"));
                    result.put("COGNOME", rs.getString("COGNOME"));
                    result.put("SETT_DOC_COD", rs.getString("SETT_DOC_COD"));
                    result.put("PDS_DES", rs.getString("PDS_DES"));
                    result.put("NOME_CDS", rs.getString("NOME_CDS"));
                    result.put("TIPO_CORSO_COD", rs.getString("TIPO_CORSO_COD"));
                    result.put("SETT_COD", rs.getString("SETT_COD"));
                    result.put("AF_GEN_COD", rs.getString("AF_GEN_COD"));
                    result.put("DES", rs.getString("DES"));
                    result.put("ORE_ATT_FRONT", rs.getString("ORE_ATT_FRONT"));
                    resArray.add(result);
                }
                //out.write(temp);
                out.println(resArray);
                System.out.println(resArray);
            }

            if (action.equals("affidaIns")) {
                paramList.put("cand_app", (String) request.getParameter("cand_app").replaceAll("-", ","));
                
                if (!paramList.get("cand_app").isEmpty()){
                    String query = createQuery("affidaInsSelect", paramList);
                    
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);

                    while (rs.next()) {
                        paramList.put("ID_CAND",rs.getString("ID_CAND"));
                        paramList.put("ID_INS", rs.getString("ID_INS"));
                        
                        String query1 = createQuery("affidaInsUpdate", paramList);
                        
                        Statement st1 = conn.createStatement();
                        boolean rs1 = st1.execute(query1);
                    }
                }
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

    public String createQuery(String action, Map<String,String> parameter){
        String query = "";
        switch (action){
            case "insCand":
                query = "INSERT INTO candidature (ID_INS, COD_FIS, RUOLO_DOC_COD, COD_DIP_AFF, COD_DIP_CAP) VALUES "
                        + "('" + parameter.get("id_ins") + "','" + parameter.get("cf_doc") + "','" + parameter.get("ruolo_doc") 
                        + "'," + parameter.get("dip_doc") + "," + parameter.get("dip_ins") + ")";
                break;
                
            case "getCandByCap":
                    query = "SELECT candidature.ID_CAND, docenti.RUOLO_DOC_COD,docenti.NOME,docenti.COGNOME,docenti.SETT_DOC_COD, "
                            + "offerta_formativa.PDS_DES,offerta_formativa.NOME_CDS,offerta_formativa.TIPO_CORSO_COD, "
                            + "offerta_formativa.SETT_COD,offerta_formativa.AF_GEN_COD,offerta_formativa.DES, offerta_formativa.ORE_ATT_FRONT "
                            + "FROM candidature "
                            + "JOIN offerta_formativa ON offerta_formativa.ID_INS = candidature.ID_INS "
                            + "JOIN docenti ON candidature.COD_FIS = docenti.CODICE_FISCALE "
                            + "WHERE candidature.STATO = 'ATTESA' "
                            + "AND candidature.COD_DIP_CAP = " + parameter.get("dip_cap") +" ";
                    switch(parameter.get("fase")){
                        case "A":
                            query += "AND candidature.RUOLO_DOC_COD IN ('PO','PA','RD')";
                        case "B":
                            query += "AND candidature.RUOLO_DOC_COD IN ('PO','PA','RD','RU')";
                    }
                    break;
            
            case "convalidaCandApprova":
                query = "UPDATE candidature SET STATO = 'APPROVATA' WHERE ID_CAND IN (" + parameter.get("cand_app") + ")";
                break;
                
            case "convalidaCandRifiuta":
                query = "UPDATE candidature SET STATO = 'RIFIUTATA' WHERE STATO = 'ATTESA' AND COD_DIP_CAP = " + parameter.get("dip_cap") + " ";
                    switch(parameter.get("fase")){
                        case "A":
                            query += "AND candidature.RUOLO_DOC_COD IN ('PO','PA','RD')";
                        case "B":
                            query += "AND candidature.RUOLO_DOC_COD IN ('PO','PA','RD','RU')";
                    }
                break;
                
            case "getCandByAff":
                query = "SELECT candidature.ID_CAND, docenti.RUOLO_DOC_COD,docenti.NOME,docenti.COGNOME,docenti.SETT_DOC_COD, "
                        + "offerta_formativa.PDS_DES,offerta_formativa.NOME_CDS,offerta_formativa.TIPO_CORSO_COD, "
                        + "offerta_formativa.SETT_COD,offerta_formativa.AF_GEN_COD,offerta_formativa.DES, offerta_formativa.ORE_ATT_FRONT "
                        + "FROM candidature "
                        + "JOIN offerta_formativa ON offerta_formativa.ID_INS = candidature.ID_INS "
                        + "JOIN docenti ON candidature.COD_FIS = docenti.CODICE_FISCALE "
                        + "WHERE candidature.STATO = 'APPROVATA' "
                        + "AND candidature.COD_DIP_AFF = " + parameter.get("dip_aff") +" "
                        + "AND offerta_formativa.AFFIDATO = 0";
                break;
                
            case "affidaInsSelect":
                query = "SELECT offerta_formativa.ID_INS, candidature.ID_CAND "
                            + "FROM offerta_formativa JOIN candidature "
                            + "ON offerta_formativa.ID_INS = candidature.ID_INS "
                            + "WHERE candidature.ID_CAND IN(" + parameter.get("cand_app") + ")";
                break;
                
            case "affidaInsUpdate":
                query = "UPDATE offerta_formativa SET ID_CAND = " + parameter.get("ID_CAND") + ","
                                + "AFFIDATO = 1 WHERE offerta_formativa.ID_INS = " + parameter.get("ID_INS");
                break;
                
        }
        return query;
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
