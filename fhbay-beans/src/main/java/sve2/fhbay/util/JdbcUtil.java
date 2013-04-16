package sve2.fhbay.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUtil {
  @SuppressWarnings("unchecked")
  public static <T> T getUniqueKey(Class<T> keyClass, PreparedStatement ps) throws SQLException {
    ResultSet rs = null;
    try {
      rs = ps.getGeneratedKeys();
      if (rs.next())
        return (T)rs.getObject(1);
      else
        return null;
    }
    finally {
      if (rs != null)
        rs.close();
    } 
  }
}
