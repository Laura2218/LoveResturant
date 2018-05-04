package resturantService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class getFood extends HttpServlet{

  private static final long serialVersionUID = 1L;

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String name = request.getParameter("rname").trim();
    String temp;
    System.out.println("rname:"+name);
    DBoperation conn = new DBoperation();
    ArrayList<Map<String, Object>>lst = conn.searchFoodbyRes(name);
    if (lst!=null) { 
      HttpSession session = request.getSession();  
      for(Map<String, Object> m:lst) {
        temp = (String)m.get("RImage");
        m.remove("RImage");
        m.put("RImage","foodImage/"+temp);
      }
      session.setAttribute("res", name);
      session.setAttribute("foodlist",  lst);
      if(request.getParameter("mod") == null)
      request.getRequestDispatcher("showFood.jsp").forward(request,response); 
      else request.getRequestDispatcher("manageFood.jsp").forward(request,response); 
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
