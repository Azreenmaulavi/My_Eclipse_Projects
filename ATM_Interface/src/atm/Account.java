package atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
	private int AccountId;
	private double balance;
	Account(int AccountId, double balance)
	{
		this.AccountId=AccountId;
		this.balance=balance;
	}
	public int getAccountId() {
		return AccountId;
	}
	public double getBalance() {
		return balance;
	}
	
	 // Display current balance in account
	 public static void displayBalance(int balance)
	    {
	        System.out.println("Current Balance : " + balance);
	        System.out.println();
	    }
	 
	 public static int getIdForUser(int accId) throws SQLException {
	        int ID = 0;
	        try {
				Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM","root","Azreen123#");
	        String sql = "SELECT acc_id FROM account WHERE account_holder_id=?";
	        try (PreparedStatement statement = con.prepareStatement(sql)) {
	            statement.setInt(1, accId);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    ID = resultSet.getInt("acc_id");
	                }
	            }
	        }
}
	        catch (Exception e) {
				
			}//register the Driver class
			return ID;
	 }
}
