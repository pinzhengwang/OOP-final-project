import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Company implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Car> carsOnStock, carsSales;
	private int revenue;

	public Company() {
		readCarsFromFiles();
		calculateRevenue();
	}

	public void addCar(String type, int price, int number) {
		boolean noAdd = true;
		for (Car car : carsOnStock) {
			if (car.getType().equals(type) && car.getPrice() == price) {
				car.setNumber(car.getNumber() + number);
				noAdd = false;
				break; // avoid concurrent modification
			}
		}

		if (noAdd) {
			Car newCar = new Car(type, price, number, true);
			carsOnStock.add(newCar);
		}
		saveCarsToFile();
	}

	public void printRevenue() {
		System.out.println("Total revenue is $" + revenue);
	}

	public void browseCar(boolean mode) {
		List<Car> cars = (mode) ? carsOnStock : carsSales;
		cars.stream().sorted(Comparator.comparing(Car::getType)).forEach(System.out::println);
		if (!mode) {
			System.out.println("The total revenue is $" + revenue);
		}
	}

	private void saveCarsToFile() {
		saveListToFile(carsOnStock, "onstock.bin");
		saveListToFile(carsSales, "sales.bin");
	}

	public boolean deleteCar(int id, int number) {
		List<Car> toRemove = new ArrayList<>();
		boolean found = false;

		for (Car car : carsOnStock) {
			if (car.getStockId() == id) {
				found = true; // Car with the specified ID is found
				if (car.getNumber() > number) {
					car.setNumber(car.getNumber() - number); // Deduct the specified number from stock
					carsSales.add(new Car(car.getType(), car.getPrice(), number, false)); // Add the sold number to
																							// sales
				} else {
					toRemove.add(car); // Prepare to remove the car from stock
					carsSales.add(new Car(car.getType(), car.getPrice(), car.getNumber(), false)); // Add the entire car
																									// to
				}
				break; // Exit after handling, assuming unique IDs
			}
		}

		carsOnStock.removeAll(toRemove); // Remove the car from stock after iteration to prevent
											// ConcurrentModificationException
		saveCarsToFile(); // Save changes to file

		return found; // Return true if car was found, otherwise false
	}

	private void saveListToFile(List<Car> list, String fileName) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
			oos.writeObject(list);
			if (fileName.equals("onstock.bin"))
				oos.writeInt(Car.getStockCount());
			else
				oos.writeInt(Car.getSalesCount());
		} catch (IOException e) {
			System.err.println("Error saving cars to file: " + e.getMessage());
		}
	}

	private void readCarsFromFiles() {
		carsOnStock = readListFromFile("onstock.bin");
		carsSales = readListFromFile("sales.bin");
		calculateRevenue();
	}

	@SuppressWarnings("unchecked")
	private List<Car> readListFromFile(String fileName) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
			List<Car> toReturn = (List<Car>) ois.readObject();
			if (fileName.equals("onstock.bin"))
				Car.setStockCount(ois.readInt());
			else
				Car.setSalesCount(ois.readInt());
			return toReturn;
		} catch (FileNotFoundException e) {
			System.out.println("No existing data found for " + fileName + ". Starting fresh.");
			return new ArrayList<>();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error reading cars from file: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	private void calculateRevenue() {
		revenue = carsSales.stream().mapToInt(Car::getPrice).sum();
	}
}
