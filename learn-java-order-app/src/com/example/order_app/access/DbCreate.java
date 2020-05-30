package com.example.order_app.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DbCreate {

    private Connection getConnection() throws SQLException {
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("webapp/WEB-INF/applicationContext.xml");
	    DataSource ds =	 (DataSource) context.getBean("dataSource");

        Connection con = ds.getConnection();
        return con;
    }

	//商品マスタをCreate
    private void createItems() {
        String sqlDrop = "DROP TABLE IF EXISTS items";
    	String sqlCreate = "CREATE TABLE items (item_id VARCHAR(5) NOT NULL,item_name VARCHAR(255),item_price INTEGER,PRIMARY KEY (item_id))";

        try (Connection con = getConnection()) {
        	try (PreparedStatement stmt = con.prepareStatement(sqlDrop)) {
        		stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        	try (PreparedStatement stmt = con.prepareStatement(sqlCreate)) {
        		stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
	    //商品マスタをCreate
	    new DbCreate().createItems();

	}

}
