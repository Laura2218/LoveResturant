package resturantService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class updateRes {
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String name = request.getParameter("name").trim();
    String phone = request.getParameter("phone").trim();
    String address = request.getParameter("address").trim();
    String mphone = (String)request.getSession().getAttribute("contractNumber");
    String rname = (String)request.getSession().getAttribute("res");
    int kind = searchResByKind.h.get(request.getParameter("kind"));
    String img = name+".jpg";
    System.out.println(name+" "+phone+" "+address+" "+mphone+" "+kind+" "+img);
    DBoperation conn = new DBoperation();
    int flag = conn.updateResturant(rname, name, address, kind, mphone, img, mphone);
    if (flag==DBoperation.SQL_CORRECT) { 
      HttpSession session = request.getSession();  
      session.setAttribute("res",  name);
      request.getRequestDispatcher("success.jsp").forward(request,response); 
     }
    else if (flag==DBoperation.SQL_KEY_DUP){
      request.setAttribute( "error","1"); 
      request.getRequestDispatcher("addRes.jsp").forward(request,response);
    }
    else {
      request.setAttribute( "error","2"); 
      request.getRequestDispatcher("addRes.jsp").forward(request,response);
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.sendError(403);
  }

}
