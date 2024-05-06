
import java.util.*;

import java.util.Scanner;

public class Menu{
	static Scanner scanner = new Scanner(System.in);
	private static Buyer buyer = new Buyer();
	private static Company company = new Company();

	public static void main(String[] args) {
		System.out.println("Please select your role:");
		System.out.println("1) Buyer mode");
		System.out.println("2) Company mode");

		String choice = scanner.nextLine();
		String option = "";
		boolean exit = false;
		switch (choice) {
		case "1":
			while (!exit) {
				System.out.println("Here are options for you:");
				System.out.println("1) Browse all cars");
				System.out.println("2) Search for a car");
				System.out.println("3) Quit");
				System.out.println("Enter your choice");
				option = scanner.nextLine();
				switch (option) {
				case "1":
					browseCar(false);
					break;
				case "2":
					searchCar();
					break;
				case "3":
					exit = true;
					System.out.println("Exiting program.");
					break;
				default:
					System.out.println("Invalid option. Please try again.");
				}
			}
			break;

		case "2":
			while (!exit) {
				System.out.println("Here are options for you:");
				System.out.println("1) Add cars");
				System.out.println("2) Browse all cars");
				System.out.println("3) Get total revenue");
				System.out.println("4) Quit");
				System.out.println("Enter your choice");

				option = scanner.nextLine();
				switch (option) {
				case "1":
					addCar();
					break;
				case "2":
					browseCar(true);
					break;
				case "3":
					getRevenue(false);
					break;
				case "4":
					exit = true;
					System.out.println("Exiting program.");
					break;
				default:
					System.out.println("Invalid option. Please try again.");
				}
			}
			break;
		default:
			System.out.println("Invalid role selection. Please enter a valid choice.");
			break;
		}
		scanner.close();
	}

	private static void addCar() {
		System.out.println("Please enter the type of car you want to add:");
		String type = scanner.nextLine();
		System.out.println("Please enter the price of car you want to add:");
		int price = Integer.parseInt(scanner.nextLine());
		System.out.println("Please enter the number of car you want to add:");
		int number = Integer.parseInt(scanner.nextLine());
		company.addCar(type, price, number);
		System.out.println("Add successfully....Press Enter to go back to the Main Window");
		scanner.nextLine();
	}

	private static void searchCar() {
		System.out.println("Search by:");
		System.out.println("1. Type ");
		System.out.println("2. Price");
		String choice = scanner.nextLine();
		String query;
		int lb, hb;
		List<Car> results = null;
		switch (choice) {
		case "1":
			System.out.println(" Enter Type: ");
			query = scanner.nextLine();
			results = buyer.searchByType(query);
			break;
		case "2":
			System.out.println(" Please enter the low end price: ");
			lb = Integer.parseInt(scanner.nextLine());
			System.out.println(" Please enter the high end price");
			hb = Integer.parseInt(scanner.nextLine());
			results = buyer.searchByPrice(lb, hb);
			break;
		default:
			System.out.println("Invalid search option.");
			return;
		}

		if (results.isEmpty()) {
			System.out.println("No Cars found.");
		} else {
			System.out.println("Search Results:");
			System.out.printf("%-5s | %-30s | %-8s | %-7s%n", "ID", "Type", "Price", "Number");
			results.forEach(System.out::println);
			System.out.println("Choose one of these options:");
			System.out.println("1. To Buy a car");
			System.out.println("2. Back to main Window");
			System.out.print("Enter Your Choice: ");
			String deleteChoice = scanner.nextLine();
			if (deleteChoice.equals("1")) {
				buyCar();
			}
		}
		System.out.print("... Press Enter to go back to main window");
		scanner.nextLine();
	}

	private static void browseCar(boolean mode) {
		System.out.println("Here is a list of all carsOnStock available:");
		System.out.printf("%-5s | %-30s | %-8s | %-7s%n", "ID", "Type", "Price", "Number");
		if (mode)
			company.browseCar(mode);
		else
			buyer.browseCar();
		scanner.nextLine();
		if (!mode) {
			System.out.println("Choose one of these options:");
			System.out.println("1. To Buy a car");
			System.out.println("2. Back to main Window");
			System.out.print("Enter Your Choice: ");
			String deleteChoice = scanner.nextLine();
			if (deleteChoice.equals("1")) {
				buyCar();
			}
		} else {
			System.out.println("Choose one of these options:");
			System.out.println("1. To Delete a car");
			System.out.println("2. Back to main Window");
			System.out.print("Enter Your Choice: ");
			String deleteChoice = scanner.nextLine();
			if (deleteChoice.equals("1")) {
				deleteCar();
			}
		}
		System.out.println("\n Enter to go back to the Main Window");
		scanner.nextLine();
	}

	private static void getRevenue(boolean mode) {
		company.browseCar(mode);
	}

	private static void buyCar() {
		System.out.println("Enter the ID of the car to buy: ");
		int id = scanner.nextInt();
		System.out.println("Enter number of cars to buy");
		int number = scanner.nextInt();
		if (buyer.buyCar(id, number)) {
			System.out.println("Car bought successful.");
		} else {
			System.out.println("Car bought unsuccessful.");
		}
	}

	private static void deleteCar() {
		System.out.println("Enter the ID of the car to delete: ");
		int id = scanner.nextInt();
		System.out.println("Enter number of cars to delete");
		int number = scanner.nextInt();
		if (company.deleteCar(id, number)) {
			System.out.println("Car deleted successful.");
		} else {
			System.out.println("Car deleted unsuccessful.");
		}
	}

}
