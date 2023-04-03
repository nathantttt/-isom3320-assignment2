package asg2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Product implements Comparable<Product>{
	
	private String name;
	private double price;
	private ArrayList<SalesChannel> salesChannels;
	private int rank;

	public Product(String name, double price) {
		this.name=name;
		this.price=price;
		this.salesChannels= new ArrayList<SalesChannel>();
		this.rank=-1;
	}
	
	public void addSalesChannel(SalesChannel sc) {
		this.salesChannels.add(sc);
	}
	
	public SalesChannel[] getSalesChannel() {
		return this.salesChannels.toArray(new SalesChannel[this.salesChannels.size()]);
	}
	
	public double calculateProductTotalRevenue() {
		double totalRevenue = 0;
		
		for (SalesChannel salesChannel : this.getSalesChannel()) {
			totalRevenue += (double)salesChannel.getSalesQuantity() * this.getPrice();
		}
		
		return totalRevenue;
	}
	
	public int calculateProductSalesVolume() {
		int salesVolume = 0;

		for (SalesChannel salesChannel : this.getSalesChannel()) {
			salesVolume += salesChannel.getSalesQuantity();
		}

		return salesVolume;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	@Override
	public int compareTo(Product p) {
		// TODO Auto-generated method stub
			double thisTotalRevenue = this.calculateProductTotalRevenue();
			double pTotalRevenue = p.calculateProductTotalRevenue();
			
			if (thisTotalRevenue > pTotalRevenue) {
				return -1;
			} else if (thisTotalRevenue == pTotalRevenue) {
				return 0;
			} else {
				return 1;
			}
		}
	}
	
	
	

