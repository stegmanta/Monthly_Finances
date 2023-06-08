public class Transaction {
	//data elements
	private String transactionType; //debit or credit
	private String date; // ##-##-####
	private double amt;
	private String ID;
	private String transactionName;
	private String transactionMemo;
	private static int numTransactions;
	//constructor
	public Transaction(String type, String date, double amt, String ID, String transactionName, String transactionMemo){
		this.transactionType = type;
		this.date = date;
		this.amt = amt;
		this.ID = ID;
		this.transactionName = transactionName;
		this.transactionMemo = transactionMemo;
		numTransactions++;
	}
	
	//getters
	public String getType() {
		return this.transactionType;
	}
	public String getDate() {
		return this.date;
	}
	public double getAmt() {
		return this.amt;
	}
	public String getID() {
		return this.ID;
	}
	public String getTransactionName() {
		return this.transactionName;
	}
	public String getTransactionMemo() {
		return this.transactionMemo;
	}
	public static int getNumTransactions() {
		return numTransactions;
	}
}