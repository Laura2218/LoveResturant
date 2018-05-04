package resturantService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * RESTURANT(RName#, Address, Kind, RPhoneNumber, Popularities, RImage, PhoneNumber#);
 * USER(PhoneNumber#, Name);
 * FOOD(RName#, FName#, Description, Image);
 * COMMENTS(PhoneNumber#, RName#, FName#, Comment, CommentDate)

 */
public class DBoperation {
  private String jdbcDriver = "com.mysql.jdbc.Driver";
  private String dbusername = "root";
  private String dbpassword = "2218234907";
  private String dbUrl = "jdbc:mysql://localhost:3306/resturant_services?useSSL=false";
  public static int SQL_CORRECT = 1;
  public static int SQL_ERROR = 2;
  public static int SQL_KEY_DUP = 3;

  private Connection connectDB() {
    try {
      Class.forName(jdbcDriver);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    try {
      Connection connect = DriverManager.getConnection(dbUrl, dbusername, dbpassword);
      return connect;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private ArrayList<Map<String, Object>> extractInfo(ResultSet rs) {
    ArrayList<Map<String, Object>> lst = new ArrayList<>();

    ResultSetMetaData md;
    try {
      md = rs.getMetaData();
      int column = md.getColumnCount();
      while (rs.next()) {
        Map<String, Object> rowData = new HashMap<String, Object>();
        for (int i = 1; i <= column; i++) {
          rowData.put(md.getColumnName(i), rs.getObject(i));
        }
        lst.add(rowData);
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return lst;
  }

  private Boolean checkExist(String table, String field, String fieldValue) {
    Connection connect = connectDB();
    if (connect == null)
      return false;
    String sqlStatement = "select * from " + table + " where " + field + "=? ;";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, fieldValue);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private Boolean checkExist(String table, String field1, String field2, String fieldValue1, String fieldValue2) {
    Connection connect = connectDB();
    if (connect == null)
      return false;
    String sqlStatement = "select * from " + table + " where " + field1 + "=? and " + field2 + "=?; ";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, fieldValue1);
      ps.setString(2, fieldValue2);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private Boolean checkExist3(String phone, String rname, String fname) {
    Connection connect = connectDB();
    if (connect == null)
      return false;
    String sqlStatement = "select * from comments where PhoneNumber = ? and RName = ? and FName = ?";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, phone);
      ps.setString(2, rname);
      ps.setString(3, fname);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public int addResturant(String name, String address, int kind, String phone, String imagePath, String manager) {
    Connection connect = connectDB();
    if (connect == null)
      return SQL_ERROR;
    if (checkExist("resturant", "RName", name))
      return SQL_KEY_DUP;
    String sqlStatement = "insert into resturant values (?,?,?,?,?,?,?);";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, name);
      ps.setString(2, address);
      ps.setInt(3, kind);
      ps.setString(4, phone);
      ps.setInt(5, 0);
      ps.setString(6, imagePath);
      ps.setString(7, manager);
      ps.executeUpdate();
      return SQL_CORRECT;
    } catch (SQLException e) {
      e.printStackTrace();
      return SQL_ERROR;
    }
  }

  public int updateResturant(String oldName, String name, String address, int kind, String phone, String imagePath,
      String manager) {
    Connection connect = connectDB();
    if (connect == null)
      return SQL_ERROR;
    if (!oldName.equals(name) && checkExist("resturant", "RName", name))
      return SQL_KEY_DUP;
    String sqlStatement = "update resturant set RName = ?, Address = ?, Kind = ?, RPhoneNumber = ?, RImage = ?, PhoneNumber = ? where RName = ?;";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, name);
      ps.setString(2, address);
      ps.setInt(3, kind);
      ps.setString(4, phone);
      ps.setString(5, imagePath);
      ps.setString(6, manager);
      ps.setString(7, oldName);
      ps.executeUpdate();
      return SQL_CORRECT;
    } catch (SQLException e) {
      e.printStackTrace();
      return SQL_ERROR;
    }
  }

  // RName#, FName#, Description, Image
  public int addFood(String rname, String fname, String description, String imagePath) {
    Connection connect = connectDB();
    if (connect == null)
      return SQL_ERROR;
    if (checkExist("food", "RName", "FName", rname, fname))
      return SQL_KEY_DUP;
    String sqlStatement = "insert into food values(?,?,?,?)";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, rname);
      ps.setString(2, fname);
      ps.setString(3, description);
      ps.setString(4, imagePath);
      ps.executeUpdate();
      return SQL_CORRECT;
    } catch (SQLException e) {
      e.printStackTrace();
      return SQL_ERROR;
    }
  }

  public int updateFood(String oldRName, String oldFName, String rname, String fname, String description,
      String imagePath) {
    Connection connect = connectDB();
    if (connect == null)
      return SQL_ERROR;
    if (!(oldRName.equals(rname) && oldFName.equals(fname)) && checkExist("food", "RName", "FName", rname, fname))
      return SQL_KEY_DUP;
    String sqlStatement = "update food set RName = ?, FName = ?, Description = ?, Image = ? where RName = ? and FName = ?;";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, rname);
      ps.setString(2, fname);
      ps.setString(3, description);
      ps.setString(4, imagePath);
      ps.setString(5, oldRName);
      ps.setString(6, oldFName);
      ps.executeUpdate();
      return SQL_CORRECT;
    } catch (SQLException e) {
      e.printStackTrace();
      return SQL_ERROR;
    }
  }

  public int addUser(String phone, String name) {
    Connection connect = connectDB();
    if (connect == null)
      return SQL_ERROR;
    if (checkExist("user", "PhoneNumber", phone))
      return SQL_KEY_DUP;
    String sqlStatement = "insert into user values(?,?)";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, phone);
      ps.setString(2, name);
      ps.executeUpdate();
      return SQL_CORRECT;
    } catch (SQLException e) {
      e.printStackTrace();
      return SQL_ERROR;
    }
  }

  public int addComment(String phone, String rname, String fname, String comment) {
    Connection connect = connectDB();
    if (connect == null)
      return SQL_ERROR;
    if (checkExist3(phone, rname, fname))
      return SQL_KEY_DUP;
    String sqlStatement = "insert into comments values(?,?,?,?,?)";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, phone);
      ps.setString(2, rname);
      ps.setString(3, fname);
      ps.setString(4, comment);
      ps.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
      ps.executeUpdate();
      return SQL_CORRECT;
    } catch (SQLException e) {
      e.printStackTrace();
      return SQL_ERROR;
    }
  }

  public ArrayList<Map<String, Object>> searchComments(String rname, String fname) {
    Connection connect = connectDB();
    if (connect == null)
      return null;
    String sqlStatement = "select Comment, CommentDate from comments where RName = ? and FName = ? ";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, rname);
      ps.setString(2, fname);
      ResultSet rs = ps.executeQuery();
      return extractInfo(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ArrayList<Map<String, Object>> searchFoodbyRes(String rname) {
    Connection connect = connectDB();
    if (connect == null || !popInc(rname))
      return null; 
    String sqlStatement = "select FName, Description, Image from food where RName = ?";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, rname);
      ResultSet rs = ps.executeQuery();
      return extractInfo(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public int checkUserExist(String phone, String name) {
    Connection connect = connectDB();
    if (connect == null)
      return SQL_ERROR;
    String sqlStatement = "select * from user where PhoneNumber = ? and Name = ?";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, phone);
      ps.setString(2, name);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return SQL_KEY_DUP;
      }
      return SQL_CORRECT;
    } catch (SQLException e) {
      e.printStackTrace();
      return SQL_ERROR;
    }
  }

  public ArrayList<Map<String, Object>> searchResturant(String name) {
    Connection connect = connectDB();
    if (connect == null)
      return null;
    String sqlStatement = "select * from resturant where RName like ?";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, "%"+name+"%");
      ResultSet rs = ps.executeQuery();
      return extractInfo(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public ArrayList<Map<String, Object>> searchResturantByExactName(String name){
    Connection connect = connectDB();
    if (connect == null)
      return null;
    String sqlStatement = "select * from resturant where RName = ?";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, name);
      ResultSet rs = ps.executeQuery();
      return extractInfo(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ArrayList<Map<String, Object>> searchResByManager(String phone) {
    Connection connect = connectDB();
    if (connect == null)
      return null;
    String sqlStatement = "select * from resturant where PhoneNumber = ?";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, phone);
      ResultSet rs = ps.executeQuery();
      return extractInfo(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }


  public ArrayList<Map<String, Object>> searchResturant(int kind) {
    Connection connect = connectDB();
    if (connect == null)
      return null;
    String sqlStatement = "select * from resturant where Kind = ?";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setInt(1, kind);
      ResultSet rs = ps.executeQuery();
      return extractInfo(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ArrayList<Map<String, Object>> searchResturant() {
    Connection connect = connectDB();
    if (connect == null)
      return null;
    String sqlStatement = "select * from resturant order by Popularities desc limit 5";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ResultSet rs = ps.executeQuery();
      return extractInfo(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  private Boolean popInc(String rname) {
    Connection connect = connectDB();
    if (connect == null)
      return false;
    String sqlStatement = "update resturant set popularities = popularities + 1 where RName = ?";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, rname);
      ps.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
  
  public Boolean updateComment(String phone, String rname, String fname, String comment) {
    Connection connect = connectDB();
    if (connect == null)
      return false;
    String sqlStatement = "update comments set Comment = ?, CommentDate=? where PhoneNumber = ? and RName = ? and FName = ?";
    try {
      PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
      ps.setString(1, rname);
      ps.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
      ps.setString(3, phone);
      ps.setString(4, rname);
      ps.setString(5, fname);
      ps.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
