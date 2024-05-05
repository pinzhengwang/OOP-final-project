import java.io.Serializable;

public class Car implements Serializable {
	
	private String type;
	private int number;
	private int price;
	private int stockId, salesId;
	private static int stockCount = 0, salesCount = 0;
	boolean pos;
	private static final long serialVersionUID = 1L;
	
	public Car(String type, int price, int number, boolean pos) {
		if(pos)
			this.stockId = ++stockCount;
		else
			this.salesId = ++salesCount;
		this.pos = pos;
		this.type = type;
		this.number = number;
		this.price = price;
	}


	public int getPrice() {
		return price;
	}


	public String getType() {
		return type;
	}

	public int getNumber() {
		return number;
	}
	public static int getStockCount() {
		return stockCount;
	}
	public static void setStockCount(int salesCount) {
		Car.stockCount = salesCount;
	}
	public int getStockId() {
		return stockId;
	}
	public static int getSalesCount() {
		return salesCount;
	}
	public static void setSalesCount(int salesCount) {
		Car.salesCount = salesCount;
	}
	public int getSalesId() {
		return salesId;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	public String toString() {
		if(pos)
			return String.format("%-5d | %-30s | %- 8d | %-7d%n", stockId, type, price, number);
		else
			return String.format("%-5d | %-30s | %- 8d | %-7d%n", salesId, type, price, number);

	}
}
