import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;



public class OptionMenu {
	Scanner menuInput = new Scanner(System.in);
	DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");
	HashMap<Integer, Account> data = new HashMap<Integer, Account>();
	Account currentAccount;
	boolean isLoggedIN;

	public void getLogin() throws IOException {
		boolean end = false;
		int customerNumber = 0;
		int pinNumber = 0;
		while (!end) {
			try {
				System.out.print("\nEnter your customer number: ");
				customerNumber = menuInput.nextInt();
				System.out.print("\nEnter your PIN number: ");
				pinNumber = menuInput.nextInt();
				Iterator it = data.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					Account acc = (Account) pair.getValue();
					if (data.containsKey(customerNumber) && pinNumber == acc.getPinNumber()) {
						currentAccount = acc;
						getAccountType(acc);
						end = true;
						break;
					}
				}
				if (!end) {
					System.out.println("\nWrong Customer Number or Pin Number");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Character(s). Only Numbers.");

			}
		}
	}

	public void getAccountType(Account acc) {
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nSelect the account you want to access: ");
				System.out.println(" Type 1 - Checking Account");
				System.out.println(" Type 2 - Savings Account");
				System.out.println(" Type 3 - Show All Statements");
				System.out.println(" Type 4 - View all transactions");
				System.out.println(" Type 5 - Log out");
				System.out.println(" Type 6 - Exit");
				System.out.print("\nChoice: ");

				int selection = menuInput.nextInt();

				switch (selection) {
					case 1:
						getChecking(acc);
						break;
					case 2:
						getSaving(acc);
						break;
					case 3:
						getStatementsForAllAccounts(acc);
						break;
					case 4:
						acc.printTransaction();
						break;
					case 6:
						end = true;
						break;
					default:
						System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
	}
	public void getStatementsForAllAccounts(Account acc) {
		System.out.println("\nChecking Account Balance: " + moneyFormat.format(acc.getCheckingBalance()));
		System.out.println("\nSavings Account Balance: " + moneyFormat.format(acc.getSavingBalance()));
	}

	public void getChecking(Account acc) {
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nChecking Account: ");
				System.out.println(" Type 1 - View Balance");
				System.out.println(" Type 2 - Withdraw Funds");
				System.out.println(" Type 3 - Deposit Funds");
				System.out.println(" Type 4 - Transfer Funds");
				System.out.println(" Type 5 - Exit");
				System.out.print("\nChoice: ");

				int selection = menuInput.nextInt();

				switch (selection) {
					case 1:
						System.out.println("\nChecking Account Balance: " + moneyFormat.format(acc.getCheckingBalance()));
						break;
					case 2:
						acc.getCheckingWithdrawInput();
						break;
					case 3:
						acc.getCheckingDepositInput();
						break;

					case 4:
						acc.getTransferInput("Checking");
						break;
					case 5:
						end = true;
						break;
					default:
						System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
	}

	public void getSaving(Account acc) {
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nSavings Account: ");
				System.out.println(" Type 1 - View Balance");
				System.out.println(" Type 2 - Withdraw Funds");
				System.out.println(" Type 3 - Deposit Funds");
				System.out.println(" Type 4 - Transfer Funds");
				System.out.println(" Type 5 - Exit");
				System.out.print("Choice: ");
				int selection = menuInput.nextInt();
				switch (selection) {
					case 1:
						System.out.println("\nSavings Account Balance: " + moneyFormat.format(acc.getSavingBalance()));
						break;
					case 2:
						acc.getSavingWithdrawInput();
						break;
					case 3:
						acc.getSavingDepositInput();
						break;
					case 4:
						acc.getTransferInput("Savings");
						break;
					case 5:
						end = true;
						break;
					default:
						System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
	}

	public void createAccount() throws IOException {
		int cst_no = 0;
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nEnter your customer number ");
				cst_no = menuInput.nextInt();
				Iterator it = data.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (!data.containsKey(cst_no)) {
						end = true;
					}
				}
				if (!end) {
					System.out.println("\nThis customer number is already registered");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
		System.out.println("\nEnter PIN to be registered");
		int pin = menuInput.nextInt();
		data.put(cst_no, new Account(cst_no, pin));
		System.out.println("\nYour new account has been successfuly registered!");
		System.out.println("\nRedirecting to login.............");
		getLogin();
	}

	public void getAccountData(){
		//  0       1         2           3
		// acct # , pin#, checking bal, saving bal
		try{
			BufferedReader bufferedReader = new BufferedReader(new FileReader("db.txt"));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] items = line.split(",");

				// create accounts from info read from file
				Account account = new Account(Integer.parseInt(items[0]),
						Integer.parseInt(items[1]),
						Double.parseDouble(items[2]),
						Double.parseDouble(items[3]));

				// Put data in a hashmap <Account # , Account>
				data.put(Integer.valueOf(items[0]),account);
			}
			bufferedReader.close();
		} catch (IOException e){
			System.out.println("Error accessing database! Please re-run the program!");
		}
	}

	public void saveAccountData(){
		try{
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("db.txt"));
			for (Map.Entry<Integer, Account> integerAccountEntry : data.entrySet()) {
				Account acct = (Account) ((Map.Entry) integerAccountEntry).getValue();
				bufferedWriter.write(acct.getCustomerNumber() + ",");
				bufferedWriter.write(acct.getPinNumber() + ",");
				bufferedWriter.write(acct.getCheckingBalance() + ",");
				bufferedWriter.write(acct.getSavingBalance() + "\n");
			}
			bufferedWriter.close();
		} catch (IOException e){
			System.out.println("Error accessing database! Please re-run the program!");
		}
	}

	public void getTransactionData(){
		//  0       1         2           3
		// acct # , pin#, checking bal, saving bal
		try{
			BufferedReader bufferedReader = new BufferedReader(new FileReader("transaction.txt"));
			String line;
			Account curAcc = null;
			while ((line = bufferedReader.readLine()) != null) {
				String[] items = line.split(",");

				// get current account in reading
				curAcc = data.get(Integer.parseInt(items[0]));

				// add transaction to arrayList
				curAcc.addTransaction(items[1].trim());
			}
			bufferedReader.close();
		} catch (IOException e){
			System.out.println("Error accessing database! Please re-run the program!");
		}
	}

	public void saveTransactionData(){
		try{
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transaction.txt"));
			for (Map.Entry<Integer, Account> integerAccountEntry : data.entrySet()) {
				Account acct = (Account) ((Map.Entry) integerAccountEntry).getValue();
				for(String trans : acct.getTransactions()){
					bufferedWriter.write(acct.getCustomerNumber() + ",");
					bufferedWriter.write(trans);
					bufferedWriter.newLine();
				}
			}
			bufferedWriter.close();
		} catch (IOException e){
			System.out.println("Error accessing database! Please re-run the program!");
		}
	}
	public void mainMenu() throws IOException {
		getAccountData();
		getTransactionData();
		//data.put(952141, new Account(952141, 191904, 1000, 5000));
		//data.put(123, new Account(123, 123, 20000, 50000));
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\n Type 1 - Login");
				System.out.println(" Type 2 - Create Account");
				System.out.print("\nChoice: ");
				int choice = menuInput.nextInt();
				switch (choice) {
					case 1:
						getLogin();
						end = true;
						break;
					case 2:
						createAccount();
						end = true;
						break;
					default:
						System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
		saveTransactionData();
		saveAccountData();
		System.out.println("\nThank You for using this ATM.\n");
		menuInput.close();
		System.exit(0);
	}
}
