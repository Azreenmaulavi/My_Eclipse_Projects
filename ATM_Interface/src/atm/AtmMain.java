/*NAME: AZREEN ARSHAD MAULAVI
 * BATCH CODE:2023-9899
 * ENROLLMENT NO:EBEON0523698085
 
 * PROJECT TOPIC: ATM INTERFACE USING MYSQL AND JDBC.
 * PROJECT_DESCRIPTION:This is an ATM interface project developed using Java JDBC and MySQL.
 *There are 5 different classes in this project namely AccountHolder, Account, BankTransaction, Bank and AtmMain for particular ATM of bank. 
 *This project is a console based application. If you start running the program you will be prompted with user id and user pin. 
 * If you entered it successfully then you unlock all the functionalities which exist in an ATM and if you enter wrong pin you will not get login. 
 * The operation you can perform are transaction history, withdraw, deposit, transfer and quit.
 * In this project three tables are created for handling all customer and transaction data.
 * The tables are account_holder,account and transaction  */

package atm;
import java.util.Date;
import java.util.Scanner;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.io.*;
import java.sql.Timestamp;

public class AtmMain {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN_BOLD = "\033[1;32m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String BLACK_BOLD = "\033[1;30m";
	public static final String CYAN_BOLD = "\033[1;36m";
	public static final String RED_BOLD = "\033[1;31m";
	public static final String BLUE_BOLD = "\033[1;34m";
	public static final String PURPLE_UNDERLINED = "\033[4;35m";

	public static void main(String[] args) throws IOException, ClassNotFoundException{

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Bank b = new Bank();
		b.display();
		BankTransaction bt=new BankTransaction();
		String status;

		try // we need to call outside of java program so we try to know status of if file
			// and connectivity problem throw out exception case
		{
			Class.forName("com.mysql.jdbc.Driver");// register the Driver class
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM", "root", "Azreen123#");
			// Statement stmt=con.createStatement();
			Timestamp timestamp = new Timestamp(new Date().getTime());
			System.out.print(BLACK_BOLD+"Enter your ID: "+ANSI_RESET);
			int accId = scanner.nextInt();
			System.out.print(BLACK_BOLD+"Enter your PIN: "+ANSI_RESET);
			int enteredPin = scanner.nextInt();
			int storedPin = AccountHolder.getPinForUser(accId);
			//int storedID=Account.getIdForUser(accId);
			if (storedPin != 0 && storedPin == enteredPin ) {
				System.out.println(ANSI_GREEN_BOLD + "PIN is correct. Access granted." + ANSI_RESET);
				do {
					System.out.println(RED_BOLD + "\n*******MENU*******\n" + ANSI_RESET);
					System.out.println(BLUE_BOLD + "1). Withdraw\n" + "2). Deposit\n" + "3). Transfer\n"
							+ "4). Transaction History\n" + "5). Quit\n" + ANSI_RESET);
					System.out.println(BLACK_BOLD+"Enter your choice : "+ANSI_RESET);
					int ch = scanner.nextInt();
					switch (ch) {
					case 1:
						
						
						System.out.print(BLACK_BOLD+"Enter withdrawal amount: "+ANSI_RESET);
						
						double withdrawalAmount = Double.parseDouble(br.readLine());
						String selectQuery = "SELECT balance FROM account WHERE account_holder_id = ?";
						try (PreparedStatement selectStatement = (PreparedStatement) con.prepareStatement(selectQuery)) {
							selectStatement.setInt(1, accId);
							try (ResultSet resultSet = selectStatement.executeQuery()) {
								if (resultSet.next()) {
									double currentBalance = resultSet.getDouble("balance");

									if (currentBalance >= withdrawalAmount) {
										// Perform withdrawal
										String updateQuery = "UPDATE account SET balance = ? WHERE account_holder_id= ?";
										try (PreparedStatement updateStatement = (PreparedStatement) con.prepareStatement(updateQuery)) {
											double newBalance = currentBalance - withdrawalAmount;
											updateStatement.setDouble(1, newBalance);
											updateStatement.setInt(2, accId);
											updateStatement.executeUpdate();
											status="Completed";
											System.out.println(ANSI_GREEN_BOLD + "Withdrawal successful. New balance: "
													+ ANSI_RESET + newBalance);
											if(status.equals("Completed"))
                                            bt.insertWithdrawalTransactionSuccess(timestamp, accId);
										
										}
									} else {
										System.out.println(RED_BOLD+"Insufficient balance."+ANSI_RESET);
										status="Failed";
										bt.insertWithdrawalTransactionFail(timestamp, accId);
										
									}
								} else {
									System.out.println(RED_BOLD+"Account not found."+ANSI_RESET);
									status="Failed";
									bt.insertWithdrawalTransactionFail(timestamp, accId);
								}
							}
						}
//				         
						catch (Exception e) {
						}
						break;
					case 2:
						System.out.print("Enter account number: ");
						String accountNumber1 = br.readLine();
						System.out.print("Enter deposit amount: ");
						double depositAmount = Double.parseDouble(br.readLine());

						try {
							if (BankTransaction.depositMoney(con, accountNumber1, depositAmount)) {
								System.out.println(ANSI_GREEN_BOLD + "Deposit successful." + ANSI_RESET);
								status="Completed";
								if(status.equals("Completed"))
                                    bt.insertDepositTransactionSuccess(timestamp, accId);
							
							} else {
								System.out.println(RED_BOLD + "Deposit failed." + ANSI_RESET);
								status="Failed";
								bt.insertDepositTransactionFail(timestamp, accId);
								
							}

						} catch (Exception e) {
						}
						break;
					case 3:
						System.out.print("Enter your account number: ");
						String senderAccountNumber = br.readLine();
						System.out.print("Enter recipient account number: ");
						String recipientAccountNumber = br.readLine();
						System.out.print("Enter transfer amount: ");
						double transferAmount = scanner.nextDouble();

						try {
							// Start transaction
							con.setAutoCommit(false);

							// Retrieve sender's account balance
							String senderSelectQuery = "SELECT balance FROM account WHERE acc_no = ?";
							try (PreparedStatement senderSelectStatement = (PreparedStatement) con.prepareStatement(senderSelectQuery)) {
								senderSelectStatement.setString(1, senderAccountNumber);
								try (ResultSet senderResultSet = senderSelectStatement.executeQuery()) {
									if (senderResultSet.next()) {
										double senderBalance = senderResultSet.getDouble("balance");

										if (senderBalance >= transferAmount) {
											// Deduct from sender
											String senderUpdateQuery = "UPDATE account SET balance = ? WHERE acc_no = ?";
											try (PreparedStatement senderUpdateStatement = (PreparedStatement) con.prepareStatement(senderUpdateQuery)) {
												double newSenderBalance = senderBalance - transferAmount;
												senderUpdateStatement.setDouble(1, newSenderBalance);
												senderUpdateStatement.setString(2, senderAccountNumber);
												senderUpdateStatement.executeUpdate();

												// Add to recipient
												String recipientUpdateQuery = "UPDATE account SET balance = balance + ? WHERE acc_no= ?";
												try (PreparedStatement recipientUpdateStatement = (PreparedStatement) con.prepareStatement(recipientUpdateQuery)) {
													recipientUpdateStatement.setDouble(1, transferAmount);
													recipientUpdateStatement.setString(2, recipientAccountNumber);
													recipientUpdateStatement.executeUpdate();
													System.out.println(ANSI_GREEN_BOLD+"Transfer successful."+ANSI_RESET);
													System.out.println("Sender's new balance: " + newSenderBalance);
													status="Completed";
													if(status.equals("Completed"))
			                                            bt.insertTransferTransactionSuccess(timestamp, accId);
												}
											}
										} else {
											System.out.println(RED_BOLD+"Insufficient balance."+ANSI_RESET);
											status="Failed";
											bt.insertTransferTransactionFail(timestamp, accId);
											con.rollback(); // Rollback transaction
										}
									} else {
										System.out.println(RED_BOLD+"Sender account not found."+ANSI_RESET);
										status="Failed";
										bt.insertTransferTransactionFail(timestamp, accId);
										con.rollback(); // Rollback transaction
									}
								}
							}

						} catch (Exception e) {
						}
						break;
					case 4:
						Statement stmt=(Statement) con.createStatement();
						 String sql1="select * from transaction where acc_id="+accId;
						 ResultSet rs=stmt.executeQuery(sql1);
						 System.out.println("_____________________________________________________");
						 System.out.println("T_type\t"+"T_status\t"+"Date_Time\t"+"Account_id\t");
						 System.out.println("_____________________________________________________");
						 while (rs.next())
						 {
							 System.out.println(rs.getString("t_type")+"   "+rs.getString("t_status")+"   "+rs.getTimestamp("date_time")+"   "+rs.getInt("acc_id"));
							 }
						break;
					case 5:
						System.exit(0);
						break;
					}
				} while (true);
			} else {
				System.out.println(RED_BOLD + "PIN is incorrect. Access denied." + ANSI_RESET);
			}
			con.close();
		} 
		 catch (Exception e) {
			 // if any external problem is raised the catch (exception) going to handle and convey to the developer
			 e.printStackTrace();
		}
		
	}
}
