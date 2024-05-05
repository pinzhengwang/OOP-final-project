
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Buyer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient List<Car> carsOnStock;
	private transient List<Car> carsSales;

	public Buyer() {
		readCarsFromFiles();
	}

	public boolean buyCar(int id, int number) {
		boolean found = false;
		List<Car> toRemove = new ArrayList<>();
		for (Car car : carsOnStock) {
			if (car.getStockId() == id) {
				found = true; // Car with the specified ID is found
				if (car.getNumber() > number) {
					car.setNumber(car.getNumber() - number); // Deduct the specified number from stock
					carsSales.add(new Car(car.getType(), car.getPrice(), number,false)); // Add the sold number to sales
				} else{
					toRemove.add(car); // Prepare to remove the car from stock
					carsSales.add(new Car(car.getType(), car.getPrice(), car.getNumber(),false)); // Add the entire car to
				}
				if(car.getNumber() < number) {
					System.out.println("There's only "+number+" cars left, and have been bought");
				}
				break; // Exit after handling, assuming unique IDs
			}
		}
		carsOnStock.removeAll(toRemove);
		saveCarsToFile();
		return found;
	}

	public List<Car> searchByType(String type) {
		return carsOnStock.stream().filter(car -> car.getType().toLowerCase().contains(type.toLowerCase()))
				.collect(Collectors.toList());
	}

	public List<Car> searchByPrice(int lb, int hb) {
		return carsOnStock.stream().filter(car -> car.getPrice() >= lb && car.getPrice() <= hb)
				.collect(Collectors.toList());
	}

	public void browseCar() {
		carsOnStock.stream().sorted(Comparator.comparing(Car::getType)).forEach(System.out::println);
	}
	private void saveCarsToFile() {
		saveListToFile(carsOnStock, "onstock.bin");
		saveListToFile(carsSales, "sales.bin");
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
}
