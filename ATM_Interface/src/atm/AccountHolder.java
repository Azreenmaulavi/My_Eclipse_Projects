package atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class AccountHolder {	
	 public static int getPinForUser(int accId) throws SQLException {
	        int pin = 0;
	        try {
				Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM","root","Azreen123#");
	        String sql = "SELECT pin FROM account_holder WHERE account_holder_id=?";
	        try (PreparedStatement statement = con.prepareStatement(sql)) {
	            statement.setInt(1, accId);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    pin = resultSet.getInt("pin");
	                }
	            }
	        }
}
	        catch (Exception e) {
			
			}//register the Driver class
			return pin;
	 }
}
