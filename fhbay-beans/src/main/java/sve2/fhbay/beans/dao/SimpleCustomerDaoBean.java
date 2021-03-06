package sve2.fhbay.beans.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.dao.SimpleCustomerDao;
import sve2.fhbay.util.JdbcUtil;
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;

@Stateless
/*
 * Resource Definition + Naming (variante 1a)
 */
//@Resource(name="jdbc/fhbayds",
//			mappedName="java:jboss/datasources/FhBayDS",
//			type=javax.sql.DataSource.class)
public class SimpleCustomerDaoBean implements SimpleCustomerDao {
	
	/*
	 * Dependency Injection (Variante 2a)
	 */
//	@Resource(mappedName="java:jboss/datasources/FhBayDS")
//	private DataSource dataSource;
	
	/*
	 * Variante 2b
	 */
//	@Resource(name="jdbc/fhbayds")
//	private DataSource dataSource;

	/*
	 * Variante 
	 */
	private DataSource dataSource;
	
	/*
	 * Abhängigkeitssuche
	 */
	private DataSource getDataSource() {
		/*
		 * Variante 1a/1b
		 */
//		try {
//			Context ctx = new InitialContext();
//			//java:comp/env -> wurde einmal definiert 
//			return (DataSource) ctx.lookup("java:comp/env/jdbc/fhbayds");
//		} catch (NamingException e) {
//			throw new EJBException(e);
//		}
		
		/*
		 * Variante 2b
		 */
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
  @Override
  public void persist(Customer cust) {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      // version 1 (comment out for version 2)
      // DataSource dataSource = getDataSource();
      conn = getDataSource().getConnection();
      stmt = conn
          .prepareStatement("INSERT INTO SimpleCustomer (firstname, lastname, username, password, email) VALUES (?, ?, ?, ?, ?)",
              Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, cust.getFirstname());
      stmt.setString(2, cust.getLastname());
      stmt.setString(3, cust.getUsername());
      stmt.setString(4, cust.getPassword());
      stmt.setString(5, cust.getEmail()); 
	  
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected != 1)
        throw new EJBException("Insert into table SimpleCustomer failed.");
      
      cust.setId(JdbcUtil.getUniqueKey(Long.class, stmt));
    }
    catch (SQLException e) {
      throw new EJBException(e);
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException e) {
        throw new EJBException(e);
      }
    }
  }

	@Override
	public Customer findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Customer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
