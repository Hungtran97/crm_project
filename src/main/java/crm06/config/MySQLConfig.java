package crm06.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConfig {
	public static Connection getConnection() {
		Connection connection = null;
		try {
//		khai bao driver su dung cho JDBC (search tren internet)
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database_crm", "root", "123456");
		} catch (Exception e) {
			System.out.println("Khong tim thay driver" + e.getMessage());

		}
		return connection;

	}
}
