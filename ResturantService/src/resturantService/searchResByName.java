package resturantService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class searchResByName extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String name = request.getParameter("name").trim();
    String temp;
    System.out.println("name:"+name);
    DBoperation conn = new DBoperation();
    ArrayList<Map<String, Object>>lst = conn.searchResturant(name);
    if (lst!=null) { 
      System.out.println("有数据");
      System.out.println(lst.get(0).get("RName"));
      for(Map<String, Object> m:lst) {
        temp = (String)m.get("RImage");
        m.remove("RImage");
        m.put("RImage","resImage/"+temp);
      }
      HttpSession session = request.getSession();  
      session.setAttribute("resturant",  lst);
      request.getRequestDispatcher("showRes.jsp").forward(request,response); 
     }
    else {
      System.out.println("无数据");
      request.setAttribute( "error","2"); 
      request.getRequestDispatcher("search.jsp").forward(request,response);
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.sendError(403);
  }

}
