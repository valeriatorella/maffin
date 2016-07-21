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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author TTm
 */
@WebServlet(name = "Insegnamenti", urlPatterns = {"/Insegnamenti"})
public class Insegnamenti extends HttpServlet {

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

            if (action.equals("getDipList")) {
                String query = "SELECT DISTINCT dip_cod, dip_des"
                        + " FROM offerta_formativa ORDER BY dip_des";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                String temp = "";
                while (rs.next()) {
                    temp += "<option value='" + rs.getString("dip_cod") + "'>" + rs.getString("dip_des") + "</option>";
                }
                out.write(temp);
            }

            // DA NON UTILIZZARE, SONO TROPPI PER UNA DROP LIST
            if (action.equals("getAllIns")) {
                String query = "SELECT id_ins, nome_cds, pds_des, des"
                        + " FROM offerta_formativa ORDER BY des";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                String temp = "";
                while (rs.next()) {
                    temp += "<option value='" + rs.getString("id_ins") + "'>" + rs.getString("nome_cds") + "-" + rs.getString("pds_des") + "-" + rs.getString("des") + "</option>";
                }
                System.out.println(temp);
                out.write(temp);
            }

            if (action.equals("getInsByDip")) {
                String dip_cod = (String) request.getParameter("dip_cod");

                String query = "SELECT id_ins, nome_cds, pds_des, des"
                        + " FROM offerta_formativa"
                        + " WHERE dip_cod = '" + dip_cod + "'"
                        + " ORDER BY nome_cds, pds_des, des";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                String temp = "";
                while (rs.next()) {
                    temp += "<option value='" + rs.getString("id_ins") + "'>" + rs.getString("nome_cds") + "-" + rs.getString("pds_des") + "-" + rs.getString("des") + "</option>";
                }
                out.write(temp);
            }

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
