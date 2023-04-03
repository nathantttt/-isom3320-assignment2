package asg2;

import java.util.ArrayList;

public class ProductType implements Comparable<ProductType>{

	private String typeName;
	private ArrayList<Product> products;
	private int rank;
	private ArrayList<Double> productTypeSalesChannelTotalRevenue;
	private ArrayList<Integer> productTypeSalesChannelSalesVolume;
	
	public ProductType(String name) {
		this.typeName= name;
		this.products= new ArrayList<Product>();
		this.productTypeSalesChannelTotalRevenue= new ArrayList<Double>();
		this.productTypeSalesChannelSalesVolume= new ArrayList<Integer>();
	}
	
	public void addProduct(Product product) {
		this.products.add(product);
	}
	
	public ArrayList<Product> getProducts(){
		return this.products;
	}
	
	public double calculateProductTypeTotalRevenue() {
		double totalRevenue = 0;
		
		for (Product product : this.getProducts()) {
			totalRevenue += product.calculateProductTotalRevenue();
		}
		
		return totalRevenue;
	}
	
	public int calculateProductTypeSalesVolume() {
		int salesVolume = 0;

		for (Product product : this.getProducts()) {
			salesVolume += product.calculateProductSalesVolume();
		}

		return salesVolume;
	}
	
	public void resetArrayList() {
		this.productTypeSalesChannelTotalRevenue= new ArrayList<Double>();
		this.productTypeSalesChannelSalesVolume= new ArrayList<Integer>();
	}
	
	@Override
	public int compareTo(ProductType pt) {
		// TODO Auto-generated method stub
		double thisTotalRevenue = this.calculateProductTypeTotalRevenue();
		double pTotalRevenue = pt.calculateProductTypeTotalRevenue();

		if (thisTotalRevenue > pTotalRevenue) {
			return -1;
		} else if (thisTotalRevenue == pTotalRevenue) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public ArrayList<Double> getProductTypeSalesChannelTotalRevenue() {
		return productTypeSalesChannelTotalRevenue;
	}

	public void setSalesChannelTotal(ArrayList<Double> productTypeSalesChannelTotalRevenue) {
		this.productTypeSalesChannelTotalRevenue = productTypeSalesChannelTotalRevenue;
	}

	public ArrayList<Integer> getProductTypeSalesChannelSalesVolume() {
		return productTypeSalesChannelSalesVolume;
	}

	public void setProductTypeSalesChannelSalesVolume(ArrayList<Integer> productTypeSalesChannelSalesVolume) {
		this.productTypeSalesChannelSalesVolume = productTypeSalesChannelSalesVolume;
	}
	
}
