package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DBUtil {
	static {
	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnect() {
		Connection con = null;
		String url = "jdbc:mysql://127.0.0.1:3306/project_smartfarm?serverTimezone=UTC";
		String user = "sample";
		String password = "1234";
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	//자원반납
	public static void close(ResultSet rs, PreparedStatement ptmt, Connection con) {
		try {
			if(rs!=null)rs.close();
			if(ptmt!=null)ptmt.close();
			if(con!=null)con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(PreparedStatement ptmt, Connection con) {
		try {
			if(ptmt!=null)ptmt.close();
			if(con!=null)con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection con) {
		try {
			if(con!=null)con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}









