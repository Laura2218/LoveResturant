package resturantService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class getComment extends HttpServlet{
  private static final long serialVersionUID = 1L;

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String name = request.getParameter("fname").trim();
    String rname = (String)request.getSession().getAttribute("res");
    System.out.println("rname:"+name);
    DBoperation conn = new DBoperation();
    ArrayList<Map<String, Object>>lst = conn.searchComments(rname, name);
    if (lst!=null) { 
      HttpSession session = request.getSession(); 
      session.setAttribute("food", name);
      session.setAttribute("comment",  lst);
      request.getRequestDispatcher("showComment.jsp").forward(request,response); 
     }
    else {
      request.setAttribute( "error","2"); 
      request.getRequestDispatcher("search.jsp").forward(request,response);
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    doPost(request, response);
  }

}
