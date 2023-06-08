//this is my program to automate my process of calculating monthly finances
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.util.ArrayList;
import javax.swing.JOptionPane;
public class MonthlyFinances {
	
	//imports file and stores in array list
	public static void importFile(ArrayList<Transaction> transactionList) {
		//variable declaration
		Scanner scr = null;
		File file = null;
		int selection;
		Transaction initTransaction;
		JFileChooser fileChooser = new JFileChooser();
		//prompt file selection
		selection = fileChooser.showOpenDialog(null);
		if(selection == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		else {
			System.out.println("User hit cancel, quitting program");
			System.exit(0);
		}
		try {
			scr = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//store Debits and Credits
		int i = 0;
		
		scr.nextLine();//this line skips the string put at the beginning of the file automatically
		while(scr.hasNextLine()) {
			String[] line = scr.nextLine().split(","); // ["DEBIT", "11-07-2022", "-0.37", "fitID",Comment 1, comment 2]
			//fill in transaction with String array
			initTransaction = new Transaction(line[0], line[1], Double.parseDouble(line[2]), line[3], line[4], line[5]);
			transactionList.add(initTransaction);
			i++;
		}
		//check
//		for(i =0; i < transactionList.size(); i++) {
//			System.out.println(transactionList.get(i).getAmt());
//		}
		scr.close();
	}
	//calculate total debit transactions (doesn't count as spent if transfered to another account)
	public static double calcTotalSpent(ArrayList<Transaction> transactionList) {
		double totalSpent = 0;
		for(int i = 0; i < Transaction.getNumTransactions(); i++) {
			if(transactionList.get(i).getAmt() < 0 && !transactionList.get(i).getTransactionName().equals("Withdrawal Home Banking To Share")) {
				totalSpent += Math.abs(transactionList.get(i).getAmt());
			}
			//print out withdrawals to other accounts
//			if(transactionList.get(i).getTransactionName().equals("Withdrawal Home Banking To Share")) {
//			System.out.println("--------------");
//			System.out.println("Withdrawal on " + transactionList.get(i).getDate() + "\n $" + transactionList.get(i).getAmt());
//			System.out.println("Memo: " + transactionList.get(i).getTransactionMemo());
//			
//			}
		}
		return totalSpent;
	}
	//calculate total credit transactions
	public static double calcTotalMadeOrGiven(ArrayList<Transaction> transactionList) {
		double totalMadeOrGiven = 0.0;
		for(int i = 0; i < Transaction.getNumTransactions(); i++) {
			if(transactionList.get(i).getAmt() > 0) {
				totalMadeOrGiven += transactionList.get(i).getAmt();
			}
		}
		
		return totalMadeOrGiven;
	}
	//calculate money not spent on rent
	public static double calcSpentNoRent(ArrayList<Transaction> transactionList) {
		//FIXME: figure out how to find how many rent payments I completed
		double rentTotal = 0;
		double spentNoRent = 0;
		for(int i = 0; i < Transaction.getNumTransactions(); i++) {
			//TODO: eventually for mass adoption allow argument to except memo for rent
			if(transactionList.get(i).getTransactionMemo().equals("Withdrawal ACH District at Camp TYPE: Rent CO: District at Camp NAME: Tayler Stegman")) {
				rentTotal += Math.abs(transactionList.get(i).getAmt());	
				//testing rent algorithm
//				System.out.println("--------");
//				System.out.println("Rent for " + transactionList.get(i).getDate() + "\n $" + transactionList.get(i).getAmt());
			}	
		}
		spentNoRent = calcTotalSpent(transactionList) - rentTotal;
		return spentNoRent;
	}
	//calculates amount given for car
	public static double calcAmtGivenForCar(ArrayList<Transaction> transactionList) {
		double amtGiven = 0;
		for(int i = 0; i < Transaction.getNumTransactions(); i++) {
			if(((transactionList.get(i).getTransactionName().equals("Deposit ACH BRIAN STEGMAN TYPE: ") || transactionList.get(i).getTransactionName().equals("Deposit ACH ALLY BANK TYPE: $TRA")))) {
				amtGiven += transactionList.get(i).getAmt();
				//test car algorithm
//				System.out.println("------" + "\nDate " + transactionList.get(i).getDate());
//				System.out.println(transactionList.get(i).getAmt());
			}
		}
		return amtGiven;
	}
	//develop output
	public static String output(ArrayList<Transaction> transactionList) {
		String output = "";
		//setup basic output
		output = transactionList.get(0).getDate() + " to " + transactionList.get(Transaction.getNumTransactions() - 1).getDate() 
			+ " total spent: $" + String.format("%.2f",calcTotalSpent(transactionList))  + "\nSpent not including rent : $" + String.format("%.2f", calcSpentNoRent(transactionList)) 
			+ "\nMoney made/given : $" + String.format("%.2f", calcTotalMadeOrGiven(transactionList)); 
		//calculate gain or loss
		double sum = calcTotalMadeOrGiven(transactionList) - calcTotalSpent(transactionList);
		//calculate loss/gain after car
		double NOIAfterCar = sum - calcAmtGivenForCar(transactionList);
		if(sum < 0) {
			sum = Math.abs(sum);
			output += "\nLoss: $" + String.format("%.2f", sum);
		}
		else if(sum > 0) {
			output += "\nGain: $" + String.format("%.2f", sum);
		}
		else {
			output += "\nBroke even (no loss or gain)";
		}
		//output how much given for car and gain or loss minus that number
		output += "\n\n\nTotal given for car: $" + String.format("%.2f",calcAmtGivenForCar(transactionList)) 
		+ "\n NOI without car money $" + String.format("%.2f",NOIAfterCar);
		return output;
	}
	
	public static void main(String[] args) {
		ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
		importFile(transactionList);
		
		JOptionPane.showMessageDialog(null, output(transactionList));
		
		
		System.exit(0);
	}//end main

}//end of class 
