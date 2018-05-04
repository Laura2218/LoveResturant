package resturantService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class getRes extends HttpServlet{

  private static final long serialVersionUID = 1L;

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String name = request.getParameter("rname").trim();
    System.out.println("name:"+name);
    DBoperation conn = new DBoperation();
    ArrayList<Map<String, Object>>lst = conn.searchResturantByExactName(name);
    if (lst!=null) { 
      HttpSession session = request.getSession();  
      session.setAttribute("resturant",  lst);
      request.getRequestDispatcher("showComment.jsp").forward(request,response); 
     }
    else {
      request.setAttribute( "error","2"); 
      request.getRequestDispatcher("search.jsp").forward(request,response);
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.sendError(403);
  }

}
