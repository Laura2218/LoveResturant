package resturantService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class searchResByPop extends HttpServlet{

  private static final long serialVersionUID = 1L;

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    DBoperation conn = new DBoperation();
    String temp;
    ArrayList<Map<String, Object>>lst = conn.searchResturant();
    if (lst!=null) { 
      HttpSession session = request.getSession();  
      for(Map<String, Object> m:lst) {
        temp = (String)m.get("RImage");
        m.remove("RImage");
        m.put("RImage","resImage/"+temp);
      }
      session.setAttribute("resturant",  lst);
      request.getRequestDispatcher("showRes.jsp").forward(request,response); 
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
