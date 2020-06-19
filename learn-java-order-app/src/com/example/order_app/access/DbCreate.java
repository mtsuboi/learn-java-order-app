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

	//受注をCreate
    private void createOrders() {
        String sqlDrop = "DROP TABLE IF EXISTS orders";
    	String sqlCreate = "CREATE TABLE orders (order_id VARCHAR(6) NOT NULL,order_status CHAR(1),order_date DATE,ship_date DATE,customer_name VARCHAR(255),customer_zipcode VARCHAR(7),customer_address VARCHAR(255),customer_tel VARCHAR(20),order_amount INTEGER,PRIMARY KEY (order_id))";

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

	//受注明細をCreate
    private void createOrderDetail() {
        String sqlDrop = "DROP TABLE IF EXISTS order_detail";
    	String sqlCreate = "CREATE TABLE order_detail (order_id VARCHAR(6) NOT NULL,order_detail_no INTEGER NOT NULL,item_id VARCHAR(5),item_name VARCHAR(255),item_price INTEGER,order_quantity INTEGER,order_detail_amount INTEGER,PRIMARY KEY (order_id,order_detail_no))";

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

    private void execSql() {
        String sql= "DELETE FROM  order_detail WHERE order_id='000004'";

        try (Connection con = getConnection()) {
        	try (PreparedStatement stmt = con.prepareStatement(sql)) {
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
	    //new DbCreate().createItems();

	    //受注をCreate
	    //new DbCreate().createOrders();
	    //受注明細をCreate
	    //new DbCreate().createOrderDetail();

    	new DbCreate().execSql();

	}

}
