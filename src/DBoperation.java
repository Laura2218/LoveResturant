import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Date;
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
            return DriverManager.getConnection(dbUrl, dbusername, dbpassword);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Boolean checkExist1(String table, String field, String fieldValue) {
        Connection connect = connectDB();
        if (connect == null)
            return false;
        String sqlStatement = "select * from " + table + " where " + field + "=? ;";
        System.out.println(sqlStatement);
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

    private Boolean checkExist2(String fieldValue1, String fieldValue2) {
        Connection connect = connectDB();
        if (connect == null)
            return false;
        String sqlStatement = "select * from food where RName=? and TName=?; ";
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

    private Boolean checkExist3(String phone, String RName, String TName) {
        Connection connect = connectDB();
        if (connect == null)
            return false;
        String sqlStatement = "select * from comments where phoneNumber = ? and RName=? and TName=?; ";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
            ps.setString(1, phone);
            ps.setString(2, RName);
            ps.setString(3, TName);
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

    // 人气加1
    private Boolean addPopularity(String RName) {
        Connection connect = connectDB();
        if (connect == null)
            return false;
        String sqlStatement = "update resturant set Popularities = Popularities +1";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int checkUserExist(String phone, String name) {
        Connection connect = connectDB();
        if (connect == null)
            return SQL_ERROR;
        String sqlStatement = "select * from user where phoneNumber = ? and name=? ";
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

    // 添加餐厅
    public int addResturant(String name, String address, int kind, String phone, String imagePath, String manager) {
        Connection connect = connectDB();
        if (connect == null)
            return SQL_ERROR;
        if (checkExist1("resturant", "RName", name))
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

    // 更新餐厅信息
    public int updateResturant(String oldName, String name, String address, int kind, String phone, String imagePath,
                               String manager) {
        Connection connect = connectDB();
        if (connect == null)
            return SQL_ERROR;
        if (!oldName.equals(name) && checkExist1("resturant", "RName", name))
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

    // 添加菜品
    public int addFood(String rname, String fname, String description, String imagePath) {
        Connection connect = connectDB();
        if (connect == null)
            return SQL_ERROR;
        if (checkExist2(rname, fname))
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

    // 更新菜品信息
    public int updateFood(String oldRName, String oldFName, String rname, String fname, String description,
                          String imagePath) {
        Connection connect = connectDB();
        if (connect == null)
            return SQL_ERROR;
        if (!(oldRName.equals(rname) && oldFName.equals(fname)) && checkExist2(rname, fname))
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

    // 添加用户
    public int addUser(String phone, String name) {
        Connection connect = connectDB();
        if (connect == null)
            return SQL_ERROR;
        if (checkExist1("user", "PhoneNumber", phone))
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

    // 添加评论
    public int addComment(String phone, String RName, String FName, String comment) {
        Connection connect = connectDB();
        if (connect == null)
            return SQL_ERROR;
        if (checkExist3(phone, RName, FName))
            return SQL_KEY_DUP;
        String sqlStatement = "insert into user values(?,?)";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
            ps.setString(1, phone);
            ps.setString(2, RName);
            ps.setString(3, FName);
            ps.setDate(4, new Date(new java.util.Date().getTime()));
            ps.executeUpdate();
            return SQL_CORRECT;
        } catch (SQLException e) {
            e.printStackTrace();
            return SQL_ERROR;
        }
    }

    // 更新评论
    public int updateComment(String phone, String RName, String FName, String comment) {
        Connection connect = connectDB();
        if (connect == null)
            return SQL_ERROR;
        String sqlStatement = "update comments set comment = ? where phoneNumber = ? and RName = ? and FName = ?";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
            ps.setString(1, comment);
            ps.setString(2, phone);
            ps.setString(3, RName);
            ps.setString(4, FName);
            ps.executeUpdate();
            return SQL_CORRECT;
        } catch (SQLException e) {
            e.printStackTrace();
            return SQL_ERROR;
        }
    }

    // 根据名称查询餐厅
    public ArrayList<Map<String, Object>> searchResturant(String name) {
        Connection connect = connectDB();
        if (connect == null)
            return null;
        String sqlStatement = "select * from resturant where RName like ?";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            return extractInfo(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 根据类别查询餐厅
    public ArrayList<Map<String, Object>> searchResturant(int kind) {
        Connection connect = connectDB();
        if (connect == null)
            return null;
        String sqlStatement = "select * from resturant where Kind = ? order by Popularities desc";
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

    // 根据人气查询餐厅
    public ArrayList<Map<String, Object>> searchResturant() {
        Connection connect = connectDB();
        if (connect == null)
            return null;
        String sqlStatement = "select * from resturant order by Popularities desc";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            return extractInfo(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Map<String, Object>> searchResByManager(String phone){
        Connection connect = connectDB();
        if (connect == null) return null;
        String sqlStatement = "select * from resturant where phoneNumber = ?";
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

    // 根据餐厅名称查询菜品
    public ArrayList<Map<String, Object>> searchFoodbyRes(String RName) {
        Connection connect = connectDB();
        if (connect == null || addPopularity(RName))
            return null;
        String sqlStatement = "select FName, Description, Image from food where RName = ?";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
            ps.setString(1, RName);
            ResultSet rs = ps.executeQuery();
            return extractInfo(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 查询具体菜品
    public ArrayList<Map<String, Object>> searchFoodbyDet(String RName, String FName) {
        Connection connect = connectDB();
        if (connect == null || addPopularity(RName))
            return null;
        String sqlStatement = "select FName, Description, Image from food where RName = ? and FName = ?";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
            ps.setString(1, RName);
            ps.setString(2, FName);
            ResultSet rs = ps.executeQuery();
            return extractInfo(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 查询菜品评价
    public ArrayList<Map<String, Object>> searchComments(String RName, String FName) {
        Connection connect = connectDB();
        if (connect == null)
            return null;
        String sqlStatement = "select Description, commentDate from comments where RName = ? and FName = ?";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
            ps.setString(1, RName);
            ps.setString(2, FName);
            ResultSet rs = ps.executeQuery();
            return extractInfo(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 查询指定用户评价
    public ArrayList<Map<String, Object>> searchCommentsbyPer(String phone, String RName, String FName) {
        Connection connect = connectDB();
        if (connect == null)
            return null;
        String sqlStatement = "select Description, commentDate from comments where phoneNumber = ? and RName = ? and FName = ?";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(sqlStatement);
            ps.setString(1, phone);
            ps.setString(2, RName);
            ps.setString(3, FName);
            ResultSet rs = ps.executeQuery();
            return extractInfo(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
