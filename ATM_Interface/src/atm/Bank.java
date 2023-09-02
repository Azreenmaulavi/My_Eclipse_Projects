package atm;

public class Bank {
	public static final String PURPLE_UNDERLINED = "\033[4;35m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String CYAN_BOLD = "\033[1;36m";
	String bankName,branch;
	Bank()
	{
		bankName="MyBank";
		branch="Dhanori,Pune";
	}
	public void display()
	{
		System.out.println(PURPLE_UNDERLINED+"\t\t\t\t Hello Dear Customer!!\t\t\t\t\t\t\t\t\t\t\t\t"+ANSI_RESET);
		System.out.println(CYAN_BOLD+"                            Welcome to"+" "+bankName+"\n"+"                           Branch: "+branch+ANSI_RESET);
	}

}
