package resturantService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class updateFood {
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String fname = request.getParameter("name").trim();
    String description = request.getParameter("description").trim();
    DBoperation conn = new DBoperation();
    String rname = (String)request.getSession().getAttribute("res");
    String ofname = (String)request.getSession().getAttribute("food");
    String imagePath = fname+"_"+rname+".jpg";
    int flag = conn.updateFood(rname, ofname, rname, fname, description, imagePath);
    if (flag==DBoperation.SQL_CORRECT) { 
      HttpSession session = request.getSession();  
      session.setAttribute("food",  fname);
      request.getRequestDispatcher("success.jsp").forward(request,response); 
     }
    else if (flag==DBoperation.SQL_KEY_DUP){
      request.setAttribute( "error","1"); 
      request.getRequestDispatcher("addFood.jsp").forward(request,response);
    }
    else {
      request.setAttribute( "error","2"); 
      request.getRequestDispatcher("addFoos.jsp").forward(request,response);
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.sendError(403);
  }

}
