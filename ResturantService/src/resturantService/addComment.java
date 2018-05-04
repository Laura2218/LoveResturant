package resturantService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class addComment extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String comment = request.getParameter("comment").trim();
    String phone = (String)request.getSession().getAttribute("contractNumber");
    String rname = (String)request.getSession().getAttribute("res");
    String fname = (String)request.getSession().getAttribute("food");
   
    DBoperation conn = new DBoperation();
    int flag = conn.addComment(phone, rname, fname, comment);
    if (flag==DBoperation.SQL_CORRECT) { 
      request.getRequestDispatcher("success.jsp").forward(request,response); 
     }
    else {
      request.setAttribute( "error","2"); 
      request.getRequestDispatcher("addCommemt.jsp").forward(request,response);
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    doPost(request, response);
  }

}
