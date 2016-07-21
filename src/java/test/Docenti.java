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

/**
 *
 * @author TTm
 */
public class Docenti extends HttpServlet {

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
            throws ServletException {

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
            Statement st = conn.createStatement();
            PrintWriter out = response.getWriter();

            if (action.equals("getDipList")) {
                String query = "SELECT DISTINCT cod_dip_aff, afferenza_organizzativa"
                        + " FROM docenti ORDER BY afferenza_organizzativa";

                ResultSet rs = st.executeQuery(query);
                String temp = "";
                while (rs.next()) {
                    temp += "<option value='" + rs.getString("cod_dip_aff") + "'>" + rs.getString("afferenza_organizzativa") + "</option>";
                }
                out.write(temp);
                rs.close();
            }

            if (action.equals("getRoleList")) {
                String query = "SELECT DISTINCT ruolo, ruolo_doc_cod"
                        + " FROM docenti ORDER BY ruolo";
                ResultSet rs = st.executeQuery(query);
                String temp = "";
                while (rs.next()) {
                    temp += "<option value='" + rs.getString("ruolo_doc_cod") + "'>" + rs.getString("ruolo_doc_cod") + "-" + rs.getString("ruolo") + "</option>";
                }
                out.write(temp);
                rs.close();
            }

            if (action.equals("getAllDoc")) {
                String query = "SELECT codice_fiscale, cognome, nome"
                        + " FROM docenti ORDER BY cognome, nome";
                ResultSet rs = st.executeQuery(query);
                String temp = "";
                while (rs.next()) {
                    temp += "<option value='" + rs.getString("codice_fiscale") + "'>" + rs.getString("cognome") + " " + rs.getString("nome") + "</option>";
                }
                out.write(temp);
                rs.close();
            }

            if (action.equals("getFilteredDoc")) {
                String dip_cod = (String) request.getParameter("dip_cod");
                String ruolo_cod = (String) request.getParameter("ruolo_cod");
                String query = "SELECT codice_fiscale, cognome, nome"
                        + " FROM docenti"
                        + " WHERE cod_dip_aff = '" + dip_cod + "' AND ruolo_doc_cod ='" + ruolo_cod + "'"
                        + " ORDER BY cognome, nome";
                ResultSet rs = st.executeQuery(query);
                String temp = "";
                while (rs.next()) {
                    temp += "<option value='" + rs.getString("codice_fiscale") + "'>" + rs.getString("cognome") + " " + rs.getString("nome") + "</option>";
                }
                out.write(temp);
                rs.close();
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
