package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Database {
	private static String dbOldUrl = "jdbc:mysql://localhost:3306/course_catalog";
	//private static String dbNewUrl = "jdbc:mysql://localhost:3306/administration_system?serverTimezone=UTC";
	private static String dbNewUrl = "jdbc:mysql://localhost:3306/administration_system";
	private static String dbUser = "root";
	//private static String dbPwd = "aptx4869";
	private static String dbPwd = "123456";
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "数据库无法通信", "错误",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}


	// 获得一个旧数据库连接
	public static Connection getOldConnection() throws SQLException {
		//System.out.println("连接数据库...");
		return DriverManager.getConnection(dbOldUrl, dbUser, dbPwd);
	}

	// 获得一个新数据库连接
	public static Connection getNewConnection() throws SQLException {
		//System.out.println("连接数据库...");
		return DriverManager.getConnection(dbNewUrl, dbUser, dbPwd);
	}

	// 关闭数据库连接
	public static void freeDB(ResultSet rs, Statement st, Connection conn) throws SQLException {

		if (rs != null)
			rs.close();
		if (st != null)
			st.close();
		if (conn != null)
			conn.close();
		System.out.println("关闭数据库");
	}
}
