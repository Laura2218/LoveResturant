package resturantService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Register extends HttpServlet{
  
  private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      String phone = request.getParameter("phone").trim();
      String name = request.getParameter("name").trim();
      System.out.println("phone:"+phone+"; name:"+name);
      DBoperation conn = new DBoperation();
      int flag = conn.addUser(phone, name);
      if (flag==DBoperation.SQL_CORRECT) { 
        HttpSession session = request.getSession();  
        session.setAttribute("contractNumber",  phone);
        request.getRequestDispatcher("search.jsp").forward(request,response); 
       }
      else if (flag==DBoperation.SQL_KEY_DUP){
        request.setAttribute( "error","1"); 
        request.getRequestDispatcher("register.jsp").forward(request,response);
      }
      else {
        request.setAttribute( "error","2"); 
        request.getRequestDispatcher("register.jsp").forward(request,response);
      }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.sendError(403);
    }
}
