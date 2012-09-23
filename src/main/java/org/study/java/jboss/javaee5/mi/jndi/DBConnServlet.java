package org.study.java.jboss.javaee5.mi.jndi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class DBConnServlet
 */
public class DBConnServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String HEADER =
									"<html>" +
									"<head>" +
										"<title>" +
											"JNDI TEST" +
										"</title>" +
									"</head>" +
									"<body>" +
									"<h1>JNDI TEST</h1>" +
									"<ul>";
	
	private static final String FOOTER =
									"</ul>" +
									"</body>" +
									"</html>";
	
	private static Logger logger = Logger.getLogger(DBConnServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DBConnServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter writer = response.getWriter();
		
		DataSource ds = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			writer.println(HEADER);
			
			ds = (DataSource) new InitialContext().lookup("java:/jndiDS");
			conn = ds.getConnection();
			ps = conn.prepareStatement("SELECT * FROM test;");
			rs = ps.executeQuery();
			while (rs.next()) {
				Test test = new Test();
				test.setId(rs.getInt("id"));
				test.setValue(rs.getString("value"));
				logger.info(test.toString());
				writer.println("<li>" + test + "</li>");
			}
			
			writer.println(FOOTER);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null)	try { rs.close(); } catch (SQLException e) {}
			if (ps != null)	try { ps.close(); } catch (SQLException e) {}
			if (conn != null) try { conn.close(); } catch (SQLException e) {}
			if (writer != null)	writer.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
