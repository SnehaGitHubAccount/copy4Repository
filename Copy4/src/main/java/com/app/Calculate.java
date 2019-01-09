package com.app;


import java.io.IOException;
import java.sql.*;
//import org.apache.commons.dbcp.*;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.PrintWriter;
import java.net.URI;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/CalculateTotal")
public class Calculate extends HttpServlet {

	private BasicDataSource connectionPool;
	

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) 
	{
		
		try {
			System.out.println("In try");
			String[] totalamount = request.getParameterValues("total");
			String[] products = request.getParameterValues("product");
			String[] quantites = request.getParameterValues("quantity");



			 URI dbUri = new URI(System.getenv("DATABASE_URL"));
			  String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
			  connectionPool = new BasicDataSource();

			  if (dbUri.getUserInfo() != null) {
			    connectionPool.setUsername(dbUri.getUserInfo().split(":")[0]);
			    connectionPool.setPassword(dbUri.getUserInfo().split(":")[1]);
			  }
			  connectionPool.setDriverClassName("org.postgresql.Driver");
			  connectionPool.setUrl(dbUrl);
			  connectionPool.setInitialSize(1);
			  System.out.println("1");
			  Connection connection = connectionPool.getConnection();
			String insertTableSQL = "INSERT INTO product.product_entry (product_name,quantity,total_cost) VALUES(?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(insertTableSQL);
			
			for (int i=0; i<products.length; i++)
			{
				System.out.println(products[i] + " " + quantites[i]);
				preparedStatement.setInt(1,5);
				preparedStatement.setString(2, products[i]);
				preparedStatement.setInt(3, Integer.parseInt(quantites[i]));
				preparedStatement.setInt(4,Integer.parseInt(totalamount[i]));
				preparedStatement.addBatch();
			}
			System.out.println("out");
			int[] aa=preparedStatement.executeBatch();
			System.out.println("out 1");
			if(aa.length>0)
			{
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.print("<h3><font color='green'>ThisOrder Is placed Successfully</font></h3><br> <a href='index.jsp'>Click here to Go Back</a>");
			}
			else
			{
				System.out.println("no");
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}

		}

	}


