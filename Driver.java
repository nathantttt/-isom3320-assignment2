package asg2;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Driver {
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		Walmart walmart = new Walmart();

		//variable to store the decision for main menu
		int i = 0;
        System.out.println("Enter the file name:");
        
        //validate the file
        boolean continueInput = true;
        String fileName = null;
		do {
			try {
				fileName = input.nextLine();
				//read CSV and create objects
				walmart.readCSV(fileName);
				continueInput = false;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Cannot find your file, please try again.");
				System.out.println("Enter the file name:");
			}
		} while (continueInput);

		//validate the first input for main menu
		printMainMenu();
		i=validateMenu();
		
		//variable to store the decision for next step
        int nextStep;

        //handle different options in the main menu
		while (i != 6) {
			if (i == 1) {
				walmart.option1PrintData();
				nextStep = validateNextStep();
				if (nextStep==1) {
					printMainMenu();
					i=validateMenu();
				} else {
					break;
				}
			} else if (i == 2) {
				walmart.option2PrintData();
				nextStep = validateNextStep();
				if (nextStep==1) {
					printMainMenu();
					i=validateMenu();
				} else {
					break;
				}
			} else if (i == 3) {
				walmart.option3PrintData();
				nextStep = validateNextStep();
				if (nextStep==1) {
					printMainMenu();
					i=validateMenu();
				} else {
					break;
				}
			} else if (i == 4) {
				walmart.option4PrintData();
				nextStep = validateNextStep();
				if (nextStep==1) {
					printMainMenu();
					i=validateMenu();
				} else {
					break;
				}

			} else if (i == 5) {
				boolean continueInput3 = true;
				String fileName2;
				System.out.println("Enter the file name:");
				do {
					try {
						
						fileName2= input.next();
						//validation for the same file input
						if(fileName2.equals(fileName)) {
							System.out.println("Input another file.");
							System.out.println("Enter the file name:");
							continue;
						}
						Walmart walmart = new Walmart();
						walmart.readCSV(fileName2);
						continueInput3 = false;
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("Cannot find your file, please try again.");
						System.out.println("Enter the file name:");
					}
				} while (continueInput3);
				printMainMenu();
				i = validateMenu();			
			} else if (i == 6) {
				break;
			}	
		 }
        input.close();
	}
	
	public static void printMainMenu() {
        System.out.println("Which report do you want?\r\n"
        		+ "1. All products sorted by Total Revenue\r\n"
        		+ "2. All products sorted by Total Quantity\r\n"
        		+ "3. All products grouped by Product Type and sorted by Total Revenue\r\n"
        		+ "4. All products grouped by Product Type and sorted by Total Quantity\r\n"
        		+ "5. Import another data\r\n"
        		+ "6. Exit");
        
	}

	public static int validateMenu() {
		int k=0;
		boolean continueInput2 = true;
		do {
			try {
				k = input.nextInt();
				if(k!=1&&k!=2&&k!=3&&k!=4&&k!=5&&k!=6) {
					System.out.println("Invalid input. Please try again.");
					continue;
				}
				continueInput2 = false;
			} catch (InputMismatchException ex) {
				// TODO Auto-generated catch block
				System.out.println("Invalid input. Please try again.");
				input.next();
			}
		} while (continueInput2);
		return k;
	}
	
	public static int validateNextStep() {
		int ns=0;
		boolean continueInput3 = true;
		do {
			try {
				ns = input.nextInt();
				if(ns!= 1&&ns!=2) {
					System.out.println("Invalid input. Please try again.");
					continue;
				}
				continueInput3 = false;
			} catch (InputMismatchException ex) {
				// TODO Auto-generated catch block
				System.out.println("Invalid input. Please try again.");
				input.next();
			}
		} while (continueInput3);
		return ns;
	}
}
