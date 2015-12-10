import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.neo4j.jdbc.Driver;
import org.neo4j.jdbc.internal.Neo4jConnection;

public class DataBaseTest {

		
	public static void main(String[] args) throws SQLException{
		
		Properties props = new Properties();
		props.setProperty("user", "neo4j");
		props.setProperty("password", "neose");
		
		Neo4jConnection con = new Driver().connect("jdbc:neo4j://ubuntu.mayerb.net:7474/", props);

		// Querying
		try(Statement stmt = con.createStatement())
		{
		    ResultSet rs = stmt.executeQuery("Match (n {test:1}) Optional Match (n)-[r]-(n2) return n, type(r), n2");
		    while(rs.next())
		    {
		        System.out.println(rs.getString("n")+"  "+rs.getString("type(r)")+"  "+rs.getString("n2"));
		    }
		}
	}

}
