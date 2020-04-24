/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import demo.jax.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author user
 */
@WebServlet(name = "Check", urlPatterns = {"/Check"})
public class Check extends HttpServlet {

    private boolean flag;
    private String fileName = "";
    private String otvet = "";
    private String name = "";
    private String zapros = "";

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
        fileName = getServletContext().getRealPath("\\resources\\sample.xml");
        zapros = request.getParameter("button");
        name = request.getParameter("text");

        switch (zapros) {
            case "Check_SAX":
                flag = false;
                goSAX(name);
                break;
            case "Check_DOM":
                flag = false;
                goDOM(name, fileName);
                break;
            case "Check_JaxB":
                flag = true;
                goJaxB(fileName);
                break;
            default:
                otvet = "Что-то пошло нетак.";
                break;

        }

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Check</title>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("<link rel=\"stylesheet\" href=\"resources\\css\\style.css\">");
            out.println("</head>");
            out.println("<body>");

            out.println("<table border=\"0\" cellpadding=\"5\" cellspacing=\"5\">");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th colspan=\"2\">");
            if (flag) {
                out.println("Чтение с помощью JaxB");

            } else {
                out.println("Поиск тега \"" + name + "\" методом " + zapros.substring(6, 9) + ".");
            }
            out.println(" \"</th>");
            out.println(" </tr>");
            out.println("</thead>");
            out.println("<tbody>");

            out.println("<tr>");
            out.println("<td class=\"b\">" + otvet + "</td>");

            out.println("</tr>");
            out.println("<tr>");
            out.println("<td><form id=\"send\" name=\"f\" method=\"POST\" action=\"index.html\">");
            out.println("<input type=\"submit\" name=\"button\" value=\"Вернуться\" form=\"send\"></form></td>");
            out.println("</tr>");

            out.println("</table>");

            out.println("</body>");
            out.println("</html>");
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

    private void goSAX(String name) {

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DemoSAX handler = new DemoSAX(name);
            saxParser.parse(fileName, handler);

            otvet = handler.getSw();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Check.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void goDOM(String name, String uri) {
        DemoDOM demoDOM = new DemoDOM(name, uri);
        demoDOM.init();
        otvet = demoDOM.find();

    }

    private void goJaxB(String fileName) {

        try {
            JAXBContext context = JAXBContext.newInstance(UserList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object o = unmarshaller.unmarshal(new File(fileName));
            UserList list = (UserList) o;
            StringBuilder sb = new StringBuilder();
            sb.append("Objects created from XML:<BR><ol>");
            for (User user : list.getUserList()) {
                sb.append("<li>"+user + "<BR></li>");
            }
            otvet = sb.append("</ol>").toString();
        } catch (JAXBException ex) {
            Logger.getLogger(Check.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
