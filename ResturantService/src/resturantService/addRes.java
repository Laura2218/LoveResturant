package resturantService;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class addRes extends HttpServlet{
  
  private static final long serialVersionUID = 1L;
  private static final String UPLOAD_DIRECTORY = "resImage";
  private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
  private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
  private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String mphone = (String)request.getSession().getAttribute("contractNumber");
    String name = "";
    String phone = "";
    String address = "";
    String kindString = "";
    int kind;
 // 配置上传参数
    DiskFileItemFactory factory = new DiskFileItemFactory();
    // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
    factory.setSizeThreshold(MEMORY_THRESHOLD);
    // 设置临时存储目录
    factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

    ServletFileUpload upload = new ServletFileUpload(factory);

    // 设置最大文件上传值
    upload.setFileSizeMax(MAX_FILE_SIZE);

    // 设置最大请求值 (包含文件和表单数据)
    upload.setSizeMax(MAX_REQUEST_SIZE);

    // 中文处理
    upload.setHeaderEncoding("UTF-8");

    // 构造临时路径来存储上传的文件
    // 这个路径相对当前应用的目录
    String uploadPath = request.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;

    // 如果目录不存在则创建
    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) {
      uploadDir.mkdir();
    }
      List<FileItem> formItems;
      try {
        formItems = upload.parseRequest(request);
      if (formItems != null && formItems.size() > 0) {
        // 迭代表单数据
        for (FileItem item : formItems) {
          // 处理不在表单中的字段
          if (!item.isFormField()) {
            String fileName = name + ".jpg";
            String filePath = uploadPath + File.separator + fileName;
            File storeFile = new File(filePath);
            // 在控制台输出文件的上传路径
            System.out.println(filePath);
            // 保存文件到硬盘
            item.write(storeFile);
          }
          else {
            if(((String)item.getFieldName()).equals("rname"))  
            {  
                name = item.getString().trim();  
            } 
            else if(((String)item.getFieldName()).equals("raddress")){
                address = item.getString().trim();
            }
            else if (((String)item.getFieldName()).equals("rphone")){
              phone = item.getString().trim();
            }
            else {
              kindString = item.getString();
            }
          }
        }
      }
      } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute( "error","2"); 
        request.getRequestDispatcher("addRes.jsp").forward(request,response);
      }
    
    if (searchResByKind.h.get(kindString) == null ) kind = 10;
    else kind = searchResByKind.h.get(kindString);
    String img = name+".jpg";
    System.out.println(name+" "+phone+" "+address+" "+mphone+" "+kind+" "+img);
    DBoperation conn = new DBoperation();
    int flag = conn.addResturant(name, address, kind, phone, img, mphone);
    if (flag==DBoperation.SQL_CORRECT) { 
      HttpSession session = request.getSession();  
      session.setAttribute("res",  name);
      request.getRequestDispatcher("manageRes.jsp").forward(request,response); 
     }
    else if (flag==DBoperation.SQL_KEY_DUP){
      request.setAttribute( "error","1"); 
      request.getRequestDispatcher("error.jsp").forward(request,response);
    }
    else {
      request.setAttribute( "error","2"); 
      request.getRequestDispatcher("error.jsp").forward(request,response);
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.sendError(403);
  }

}
