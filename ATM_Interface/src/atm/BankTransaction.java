package atm;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.PreparedStatement;

public class BankTransaction {
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String RED = "\033[1;31m";
	public static final String PURPLE_BOLD = "\033[1;35m";
	public static void displayBalance(int balance)
	    {
	        System.out.println("Current Balance : " + balance);
	        System.out.println();
	    }
	 //Validate Account number
	 public static boolean validateAccount(Connection connection,  int enteredPin) throws SQLException {
		 Connection con1=null;
		 try
		 {
			 Class.forName("com.mysql.jdbc.Driver");
				con1=DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM","root","Azreen123#");
	        String sql = "SELECT * FROM account WHERE  pin = ?";
	    
	        try (PreparedStatement statement = (PreparedStatement) con1.prepareStatement(sql)) {
	          //  statement.setString(1, accountNumber);
	            statement.setInt(2, enteredPin);
	            System.out.println(sql);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                return resultSet.next(); // Account found if a row is returned
	                
	            }
	        }     
	    }
		 catch(ClassNotFoundException e) {}
		return false;
		
		
	 }
	   public static boolean depositMoney(Connection connection, String accountNumber, double amount) throws SQLException {
		   Connection con3=null; 
		   try {
			Class.forName("com.mysql.jdbc.Driver");
			con3=DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM","root","Azreen123#");
	        String sql = "UPDATE account SET balance = balance + ? WHERE acc_no = ?";
	        try (PreparedStatement statement = (PreparedStatement) con3.prepareStatement(sql)) {
	            statement.setDouble(1, amount);
	            statement.setString(2, accountNumber);
	            int rowsUpdated = statement.executeUpdate();
	            return rowsUpdated > 0; // Deposit successful if a row is updated
	        }
	    }
		  catch (ClassNotFoundException e) {
		  }
		return false;
	  }
	   
	   public void insertWithdrawalTransactionSuccess(Timestamp date_time,int accountId) throws SQLException, ClassNotFoundException {
	        String query = "INSERT INTO transaction(t_type,t_status,date_time,acc_id) VALUES (?,?,?,?)";
           
	        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "Azreen123#");
	             PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query)) {

	            // Set the parameters for the prepared statement
	            preparedStatement.setString(1, "Withdrawal");
	            preparedStatement.setString(2, "Completed");
	            preparedStatement.setTimestamp(3,date_time);
	            preparedStatement.setInt(4,accountId);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	            	
	                System.out.println(PURPLE_BOLD+"Withdrawal transaction details inserted successfully."+ANSI_RESET);
	            } else {
	                System.out.println(RED+"Failed to insert withdrawal transaction details."+ANSI_RESET);
	            }
                   // connection.close();
                     }
	        catch (SQLException e) {
			  }
	   }
	   
	   public void insertWithdrawalTransactionFail(Timestamp date_time,int accountId) throws SQLException, ClassNotFoundException {
	        String query = "INSERT INTO transaction (t_type,t_status,date_time,acc_id) VALUES (?,?,?,?)";
          
	        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "Azreen123#");
	             PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query)) {

	            // Set the parameters for the prepared statement
	            preparedStatement.setString(1, "Withdrawal");
	            preparedStatement.setString(2, "Failed");
	            preparedStatement.setTimestamp(3,date_time);
	            preparedStatement.setInt(4,accountId);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	            	
	                System.out.println(PURPLE_BOLD+"Withdrawal transaction details inserted successfully."+ANSI_RESET);
	            } else {
	                System.out.println(RED+"Failed to insert withdrawal transaction details."+ANSI_RESET);
	            }
                   //  connection.close();
	        }
	        catch (SQLException e) {
			  }
	   }
	   
	   public void insertDepositTransactionSuccess(Timestamp date_time,int accountId) throws ClassNotFoundException, SQLException {
	        String query = "INSERT INTO transaction (t_type,t_status,date_time,acc_id) VALUES (?,?,?,?)";
          
	        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "Azreen123#");
	             PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query)) {

	            // Set the parameters for the prepared statement
	            preparedStatement.setString(1, "Deposit");
	            preparedStatement.setString(2, "Completed");
	            preparedStatement.setTimestamp(3,date_time);
	            preparedStatement.setInt(4,accountId);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	            	
	                System.out.println(PURPLE_BOLD+"Deposit transaction details inserted successfully."+ANSI_RESET);
	            } else {
	                System.out.println(RED+"Failed to insert deposit transaction details."+ANSI_RESET);
	            }
	           // connection.close();
	        }
	        catch (SQLException e) {
			  }
	   }
	   
	   public void insertDepositTransactionFail(Timestamp date_time,int accountId) throws SQLException, ClassNotFoundException {
	        String query = "INSERT INTO transaction (t_type,t_status,date_time,acc_id) VALUES (?,?,?,?)";
         
	        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "Azreen123#");
	             PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query)) {

	            // Set the parameters for the prepared statement
	            preparedStatement.setString(1, "Deposit");
	            preparedStatement.setString(2, "Failed");
	            preparedStatement.setTimestamp(3,date_time);
	            preparedStatement.setInt(4,accountId);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	            	
	                System.out.println(PURPLE_BOLD+"Deposit transaction details inserted successfully."+ANSI_RESET);
	            } else {
	                System.out.println(RED+"Failed to insert deposit transaction details."+ANSI_RESET);
	            }
	         //connection.close();
	        }
	        catch (SQLException e) {
			  }
	   }
	   
	   
	   public void insertTransferTransactionSuccess(Timestamp date_time,int accountId) throws SQLException, ClassNotFoundException {
	        String query = "INSERT INTO transaction (t_type,t_status,date_time,acc_id) VALUES (?,?,?,?)";
        
	        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "Azreen123#");
	             PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query)) {

	            // Set the parameters for the prepared statement
	            preparedStatement.setString(1, "Transfer");
	            preparedStatement.setString(2, "Completed");
	            preparedStatement.setTimestamp(3,date_time);
	            preparedStatement.setInt(4,accountId);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	            	
	                System.out.println(PURPLE_BOLD+"Transfer transaction details inserted successfully."+ANSI_RESET);
	            } else {
	                System.out.println(RED+"Failed to insert deposit transaction details."+ANSI_RESET);
	            }
	        // connection.close();
	        }
	        catch (SQLException e) {
			  }
	   }
	   
	   public void insertTransferTransactionFail(Timestamp date_time,int accountId) throws SQLException, ClassNotFoundException {
	        String query = "INSERT INTO transaction (t_type,t_status,date_time,acc_id) VALUES (?,?,?,?)";
        
	        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "Azreen123#");
	             PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query)) {

	            // Set the parameters for the prepared statement
	            preparedStatement.setString(1, "Transfer");
	            preparedStatement.setString(2, "Failed");
	            preparedStatement.setTimestamp(3,date_time);
	            preparedStatement.setInt(4,accountId);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	            	
	                System.out.println(PURPLE_BOLD+"Transfer transaction details inserted successfully."+ANSI_RESET);
	            } else {
	                System.out.println(RED+"Failed to insert transfer transaction details."+ANSI_RESET);
	            }
	         // connection.close();
	        }
	        catch (SQLException e) {
			  }
	        
	   }
}
