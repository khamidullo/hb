package uz.hbs.temp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import uz.hbs.db.MyBatisHelper;

public class TablesRowCount {
	public static void main(String[] args) throws SQLException {
		DataSource dataSource = MyBatisHelper.getSqlSessionFactory().getConfiguration().getEnvironment().getDataSource();
		
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tableowner = 'hb_user' ORDER BY tablename");
		
		int tables = 0;
		while (rs.next()) {
			tables++;
			String table = rs.getString(1);
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT COUNT(*) FROM " + table);
			if (rs1.next()) {
				int count = rs1.getInt(1);
				
				System.out.println(table.toUpperCase() + ": " + count);
			}
			rs1.close();
			stmt1.close();
		}
		rs.close();
		stmt.close();
		conn.close();

		System.out.println("Tables count: " + tables);
	}
}
