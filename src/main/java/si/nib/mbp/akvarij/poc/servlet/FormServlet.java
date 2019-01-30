/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.nib.mbp.akvarij.poc.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import si.nib.mbp.akvarij.poc.pojo.FormaBean;

/**
 *
 * @author Peter
 */
public class FormServlet extends HttpServlet {

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
        FormaBean bean = null;
        HttpSession session = request.getSession();
                
        if (session.getAttribute("formaBean") == null)
        {
            bean = new FormaBean();
        }
        else
        {
            bean = (FormaBean) session.getAttribute("formaBean");
        }
        
        //preberemo datume
        String datOd = request.getParameter("datumOd");
        String datDo = request.getParameter("datumDo");
        String id = request.getParameter("id");
        bean.setDatumOd(datOd);
        bean.setDatumDo(datDo);
        bean.setId(id);
        session.setAttribute("formaBean", bean);
        response.sendRedirect("index.jsp");

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

    
    @Override
    public String getServletInfo() {
        return "OK";
    }// </editor-fold>

}
