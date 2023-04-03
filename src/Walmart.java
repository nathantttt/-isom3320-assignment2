package asg2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class Walmart{
	
    private ArrayList<ProductType> productTypeList ;
	private ArrayList<Product> productList;
	private ArrayList<Double> productSalesChannelTotalRevenue;
	private ArrayList<Integer> productSalesChannelSalesVolume;
	
	public Walmart() {
		this.productTypeList= new ArrayList<ProductType>();
		this.productList= new ArrayList<Product>();
		this.productSalesChannelTotalRevenue = new ArrayList<Double>();
		this.productSalesChannelSalesVolume = new ArrayList<Integer>();
	}
	
	public ArrayList<Product> getProductList(){
		return this.productList;
	}

	public void readCSV(String fileName) throws FileNotFoundException {

		DecimalFormat myFormatter = new DecimalFormat("#0.00");
		
	    // do the first scanning to determine the number of rows and columns of the 3D Array to store the split elements 
	    // so that it would be dynamic
	    // and to print the header
		File file = new File(fileName);     
	    Scanner scanner = new Scanner(file);  
	    String line;
	      
	    // move the scanner to the header of the file
		line = scanner.nextLine();
		
		// create an Array to store the split elements of the header
		String[] header = new String[line.split(",").length];
		header = line.split(",");

	    // read the number of commas in the header
	    int countComma = 0;
	    for (int x=0; x<line.length(); x++) {
	      if (line.substring(x, x+1).equals(",")) {  
	        countComma++;
	      }
	    }
	    
	    // the number of columns of the dataframe = number of commas + 1
	    int cols = countComma + 1;

	    // get the total number of rows except the header
	    int rows = 0;
	    while(scanner.hasNextLine()){
	      rows++;
	      line = scanner.nextLine(); 
	    }

	    scanner.close();
	       
	    // declare a 3D String Array with a size of the determined rows and columns to store all the product sales info except the header
	    String[][] productInfo = new String[rows][cols];
	    
	    // do the second scanning to split each line, trim each element and store them into the 3D Array
	    Scanner scanner2 = new Scanner(file);
	    
	    // move the scanner to the header and start the for-loop scanning from the second line, which is the body of the product sales info
	    line = scanner2.nextLine(); 
	    
	    // declare a temporary String Array with the size of determined columns
	    String[] stringArray1 = new String[cols];
	    
		for (int x = 0; x < rows; x++) {
			line = scanner2.nextLine();
			stringArray1 = line.split(",");
			for (int y = 0; y < productInfo[x].length; y++) {
				// trim each element and assign them into the corresponding position of the 3D array
				productInfo[x][y] = stringArray1[y].trim();
			}
		}

	    scanner2.close();
	    
	    //create a temp array to store the product types(including those duplicated)
	    String [] productType = new String[productInfo.length]; 
	    
	    for (int i1 =0; i1<productInfo.length; i1++) {
	    	productType[i1]=productInfo[i1][0];
	    }
	    
	    // sort the product type for comparsion later
	    Arrays.sort(productType);
	    
	    //create a 3D Array with the same size of productInfo, but it will only be used to contain the distinct product types
	    String [][] distinctTypeRev = new String[productType.length][productInfo[0].length]; 
	    
	    for (int i1=0; i1<productType.length-1; i1++){ 
	    	if (productType[i1].equals(productType[i1+1])){
	    	} else {
	    		distinctTypeRev[i1][0]=productType[i1];
	    	}
	    }
	    
	    // store the last product type in productType since the for-loop does not consider the last index of productType for comparsion
	    // or the last few indexes are of the same type
	    distinctTypeRev[productType.length-1][0]=productType[productType.length-1];
	    
	    //create a list of ProductType and fill it with distinct ProductType objects
	    ArrayList<ProductType> distinct= new ArrayList<ProductType>();

		for (int x = 0; x < distinctTypeRev.length; x++) {
			if (distinctTypeRev[x][0] != null) {
				distinct.add(new ProductType(distinctTypeRev[x][0]));
			}
		}

		//create Product objects and SalesChannel objects
	    for (ProductType pt : distinct) {
			for (int x = 0; x < productInfo.length; x++) {
				Product product = new Product(productInfo[x][1], Double.parseDouble(myFormatter.format(Double.parseDouble(productInfo[x][2]))));
				for (int y = 3; y < productInfo[x].length; y++) {
				    	SalesChannel salesChannel= new SalesChannel(header[y],Integer.parseInt(productInfo[x][y]));
				    	product.addSalesChannel(salesChannel);
				}
				if (productInfo[x][0].equals(pt.getTypeName())) {
					pt.addProduct(product);
				}
			}
			this.productTypeList.add(pt);
		}
	    
	    //add the products into the productList for option 1 and 2 as they do not require grouping by product type
		for(ProductType pt: this.productTypeList) {
			for(Product p: pt.getProducts()) {
				this.productList.add(p);
			}
		}
	}
    

	public void option1PrintData() {
		//reset the array for calculating each SalesChannel's total revenue
		this.productSalesChannelTotalRevenue = new ArrayList<Double>();
		
		//sort and rank
		Collections.sort(this.productList);
		this.rankProductByTotalRevenue();
		
		//print the data
		System.out.printf("%-30s ", "Product");
		for (int x = 0; x < this.productList.get(0).getSalesChannel().length; x++) {
			System.out.printf("%-15s ", this.productList.get(0).getSalesChannel()[x].getChannelName());
		}
		
		System.out.printf("%-15s ", "Total");
		System.out.printf("%-15s \n", "Rank");
		
		for(int i=0; i<this.productList.size();i++) {
			
			Product p = this.productList.get(i);
			
			System.out.printf("%-30s ", p.getName());
			
			for(int j=0; j<p.getSalesChannel().length;j++) {			
				SalesChannel sc = p.getSalesChannel()[j];
				System.out.printf("%-15.2f ", (double)sc.getSalesQuantity()*p.getPrice());
				//calculate each SalesChannel's total revenue by adding the individual revenue
				if(i==0) {
					this.productSalesChannelTotalRevenue.add((double)sc.getSalesQuantity()*p.getPrice());
				}else {
					this.productSalesChannelTotalRevenue.set(j, this.productSalesChannelTotalRevenue.get(j)+(double)sc.getSalesQuantity()*p.getPrice());
				}
			}
			System.out.printf("%-15.2f ", this.productList.get(i).calculateProductTotalRevenue());
			System.out.printf("%-15d \n", this.productList.get(i).getRank());
		}
		System.out.printf("%30s ", "Total:");

		double productsTotalRevenue = 0;
		for(Double d:this.productSalesChannelTotalRevenue) {
			productsTotalRevenue+=d;
			System.out.printf("%-15.2f ", d);
		}
		
		System.out.printf("%-15.2f ", productsTotalRevenue);
		System.out.println();
		System.out.printf("%30s ", "Average by Product:");
		
		double productsAverageRevenue =0;
		for(Double d:this.productSalesChannelTotalRevenue) {
			System.out.printf("%-15.2f ", d/this.productList.size());
			productsAverageRevenue+=d/this.productList.size();
		}

		System.out.printf("%-15.2f ", productsAverageRevenue);
		
		System.out.println();
		System.out.println();
		System.out.println("Next Step:\r\n"
				+ "1. Back\r\n"
				+ "2. Exit");
	}
	

	public void option2PrintData() {
		//reset the array for calculating each SalesChannel's sales volume
		this.productSalesChannelSalesVolume = new ArrayList<Integer>();
		
		//rank and sort
		this.sortProductBySalesVolume(this.productList);
		this.rankProductBySalesVolume(this.productList);
		
		
		System.out.printf("%-30s ", "Product");
		for (int x = 0; x < this.productList.get(0).getSalesChannel().length; x++) {
			System.out.printf("%-15s ", this.productList.get(0).getSalesChannel()[x].getChannelName());
		}
		
		System.out.printf("%-15s ", "Total");
		System.out.printf("%-15s \n", "Rank");
		
		//print the data
		for(int i=0; i<this.productList.size();i++) {
			
			Product p = this.productList.get(i);
			
			System.out.printf("%-30s ", p.getName());
			
			for(int j=0; j<p.getSalesChannel().length;j++) {
				
				SalesChannel sc = p.getSalesChannel()[j];	
				System.out.printf("%-15d ", sc.getSalesQuantity());
				//calculate each SalesChannel's sales volume by adding the individual sales volume
				if(i==0) {
					this.productSalesChannelSalesVolume.add(sc.getSalesQuantity());
				}else {
					this.productSalesChannelSalesVolume.set(j, this.productSalesChannelSalesVolume.get(j)+sc.getSalesQuantity());
				}
			}
			System.out.printf("%-15d ", this.productList.get(i).calculateProductSalesVolume());
			System.out.printf("%-15d \n", this.productList.get(i).getRank());
		}
		System.out.printf("%30s ", "Total:");

		int productsSalesVolume = 0;
		for(int d:this.productSalesChannelSalesVolume) {
			productsSalesVolume+=d;
			System.out.printf("%-15d ", d);
		}
		
		System.out.printf("%-15d ", productsSalesVolume);
		System.out.println();
		System.out.printf("%30s ", "Average by Product:");
		
		double productsAverageSalesVolume =0;
		for(int d:this.productSalesChannelSalesVolume) {
			System.out.printf("%-15.2f ", (double)d/this.productList.size());
			productsAverageSalesVolume+=(double)d/this.productList.size();
		}

		System.out.printf("%-15.2f ", productsAverageSalesVolume);
		
		System.out.println();
		System.out.println();
		System.out.println("Next Step:\r\n"
				+ "1. Back\r\n"
				+ "2. Exit");
	}
	

	public void option3PrintData() {		
		
		//rank and sort product types
		Collections.sort(this.productTypeList);
		this.rankProductTypeByTotalRevenue();

		System.out.printf("%-30s ", "Product");
		for (int x = 0; x < this.productList.get(0).getSalesChannel().length; x++) {
			System.out.printf("%-15s ", this.productList.get(0).getSalesChannel()[x].getChannelName());
		}
		
		System.out.printf("%-15s ", "Total");
		System.out.printf("%-15s \n", "Rank");
		
		//print the data
		for(int x=0;x<this.productTypeList.size();x++) {
			
			ProductType pt= this.productTypeList.get(x);
			
			//reset the array for calculating product type's sub total revenue in each sales channel
			pt.resetArrayList();
			
			//rank and sort products in each product type
			Collections.sort(pt.getProducts());
			this.rankProductByTotalRevenue(pt.getProducts());
			
			System.out.printf("%-30s ", pt.getTypeName());

			for(int i= 0;i<pt.getProducts().size();i++) {
		
				Product p = pt.getProducts().get(i);
				
				//calculate product type's sub total revenue in each sales channel
				if (i == 0) {
					for (int j = 0; j < p.getSalesChannel().length; j++) {
						pt.getProductTypeSalesChannelTotalRevenue().add((double)p.getSalesChannel()[j].getSalesQuantity()*p.getPrice());
					}
				} else {
					for (int j = 0; j < p.getSalesChannel().length; j++) {
						pt.getProductTypeSalesChannelTotalRevenue().set(j, pt.getProductTypeSalesChannelTotalRevenue().get(j)+(double)p.getSalesChannel()[j].getSalesQuantity()*p.getPrice());
					}
				}
			}
			
			//print each product type's sub total revenue in each sales channel
			for(double i: pt.getProductTypeSalesChannelTotalRevenue()) {
				System.out.printf("%-15.2f ", i);
			}
			
			System.out.printf("%-15.2f ", pt.calculateProductTypeTotalRevenue());
			System.out.printf("%-15d \n", pt.getRank());
			
			for (Product p: pt.getProducts()) {	
				System.out.printf("%-32s ", ": "+p.getName());
				for (SalesChannel sc: p.getSalesChannel()) {
					System.out.printf("%-15.2f ", (double)sc.getSalesQuantity()*p.getPrice());
				}
				System.out.printf("%-15.2f ", p.calculateProductTotalRevenue());
				System.out.printf("%-15d \n", p.getRank());
			}
			if (x!=this.productTypeList.size()-1) {
				System.out.println();
			}
		}
		System.out.printf("%30s ", "Total:");

		
		//reset the sales channel total revenue array each time
		ArrayList<Double> salesChannelTotalTotalRevenue = new ArrayList<Double>();
		
		//calculate each product type's total revenue in each sales channel
		for (int i=0; i < this.productTypeList.size(); i++) {
			ProductType pt=this.productTypeList.get(i);
			for (int j = 0; j < pt.getProductTypeSalesChannelTotalRevenue().size(); j++) {
				if(i==0) {
					salesChannelTotalTotalRevenue.add(pt.getProductTypeSalesChannelTotalRevenue().get(j));
				} else {
					salesChannelTotalTotalRevenue.set(j,salesChannelTotalTotalRevenue.get(j)+pt.getProductTypeSalesChannelTotalRevenue().get(j));
				}
			}
		}
		
		//print each product type's total revenue in each sales channel
		double TotalRevenue=0;
		for(double d: salesChannelTotalTotalRevenue) {
			TotalRevenue+= d;
			System.out.printf("%-15.2f ", d);
		}
		System.out.printf("%-15.2f ", TotalRevenue);
		
		System.out.println();
		
		//print the averages
		System.out.printf("%30s ", "Average by Product:");
		
		for(Double d:salesChannelTotalTotalRevenue) {
			System.out.printf("%-15.2f ", d/this.productList.size());
		}
		System.out.printf("%-15.2f ", TotalRevenue/this.productList.size());
		System.out.println();
		System.out.println();
		System.out.println("Next Step:\r\n"
				+ "1. Back\r\n"
				+ "2. Exit");
		
	}

	public void option4PrintData() {
		
		//sort and rank each protuct type
		this.sortProductTypeBySalesVolume(this.productTypeList);
		this.rankProductTypeBySalesVolume();
		
		System.out.printf("%-30s ", "Product");
		for (int x = 0; x < this.productList.get(0).getSalesChannel().length; x++) {
			System.out.printf("%-15s ", this.productList.get(0).getSalesChannel()[x].getChannelName());
		}
		
		System.out.printf("%-15s ", "Total");
		System.out.printf("%-15s \n", "Rank");
		
		//print the data
		for(int x=0;x<this.productTypeList.size();x++) {
			ProductType pt=this.productTypeList.get(x);
			pt.resetArrayList();
			
			this.sortProductBySalesVolume(pt.getProducts());
			this.rankProductBySalesVolume(pt.getProducts());
			
			System.out.printf("%-30s ", pt.getTypeName());
			
			
			for(int i= 0;i<pt.getProducts().size();i++) {
				Product p = pt.getProducts().get(i);
				
				//calculate product type's sub sales volume in each sales channel
				if (i == 0) {
					for (int j = 0; j < p.getSalesChannel().length; j++) {
						pt.getProductTypeSalesChannelSalesVolume().add(p.getSalesChannel()[j].getSalesQuantity());
					}
				} else {
					for (int j = 0; j < p.getSalesChannel().length; j++) {
						pt.getProductTypeSalesChannelSalesVolume().set(j, pt.getProductTypeSalesChannelSalesVolume().get(j)+p.getSalesChannel()[j].getSalesQuantity());
					}	
				}
			}
			//print product type's sub sales volume in each sales channel
			for(int i: pt.getProductTypeSalesChannelSalesVolume()) {
				System.out.printf("%-15d ", i);
			}
			System.out.printf("%-15d ", pt.calculateProductTypeSalesVolume());
			System.out.printf("%-15d \n", pt.getRank());
			
			
			for (Product p: pt.getProducts()) {	
				System.out.printf("%-32s ", ": "+p.getName());
				for (SalesChannel sc: p.getSalesChannel()) {
					System.out.printf("%-15d ", sc.getSalesQuantity());
				}
				System.out.printf("%-15d ", p.calculateProductSalesVolume());
				System.out.printf("%-15d \n", p.getRank());
			}
			if (x!=this.productTypeList.size()-1) {
				System.out.println();
			}
		}
		System.out.printf("%30s ", "Total:");
		//reset the sales channel sales volume array each time
		ArrayList<Integer> salesChannelTotalSalesVolume = new ArrayList<Integer>();
		
		//calculate each product type's sales volume in each sales channel
		for (int i=0; i < this.productTypeList.size(); i++) {
			ProductType pt=this.productTypeList.get(i);
			for (int j = 0; j < pt.getProductTypeSalesChannelSalesVolume().size(); j++) {
				if(i==0) {
					salesChannelTotalSalesVolume.add(pt.getProductTypeSalesChannelSalesVolume().get(j));
				} else {
					salesChannelTotalSalesVolume.set(j,salesChannelTotalSalesVolume.get(j)+pt.getProductTypeSalesChannelSalesVolume().get(j));
				}
			}
		}
		
		//print each product type's total sales volume in each sales channel
		int totalSalesVolume=0;
		for(int d: salesChannelTotalSalesVolume) {
			totalSalesVolume+= d;
			System.out.printf("%-15d ", d);
		}
		System.out.printf("%-15d ", totalSalesVolume);
		
		System.out.println();
		System.out.printf("%30s ", "Average by Type:");
		
		//print the averages
		for(int d:salesChannelTotalSalesVolume) {
			System.out.printf("%-15.2f ", (double)d/this.productTypeList.size());
		}
		System.out.printf("%-15.2f ", (double)totalSalesVolume/this.productTypeList.size());
		System.out.println();
		System.out.println();
		System.out.println("Next Step:\r\n"
				+ "1. Back\r\n"
				+ "2. Exit");
	}

	public void rankProductBySalesVolume() {
		int rank=1;
		
		for (Product product: productList) {
			int productIndex = productList.indexOf(product);
		
		
		if(productIndex==0) {
			product.setRank(rank);
		}else if (product.calculateProductSalesVolume()==productList.get(productIndex-1).calculateProductSalesVolume()) {
			product.setRank(rank);
		}else {
			rank=productIndex+1;
			product.setRank(rank);				
		}
		
		}
	}
	
	public void rankProductBySalesVolume(ArrayList<Product> pList) {
		int rank=1;
		
		for (Product product: pList) {
			int productIndex = pList.indexOf(product);
		
		
		if(productIndex==0) {
			product.setRank(rank);
		}else if (product.calculateProductSalesVolume()==pList.get(productIndex-1).calculateProductSalesVolume()) {
			product.setRank(rank);
		}else {
			rank=productIndex+1;
			product.setRank(rank);				
		}
		
		}
	}
	

	public void rankProductByTotalRevenue() {
		int rank = 1;

		for (Product product : productList) {
			int productIndex = productList.indexOf(product);

			if (productIndex == 0) {
				product.setRank(rank);
			} else if (product.calculateProductTotalRevenue() == productList.get(productIndex - 1)
					.calculateProductTotalRevenue()) {
				product.setRank(rank);
			} else {
				rank = productIndex + 1;
				product.setRank(rank);
			}

		}
	}
	
	public void rankProductByTotalRevenue(ArrayList<Product> pList) {
		int rank = 1;

		for (Product product : pList) {
			int productIndex = pList.indexOf(product);

			if (productIndex == 0) {
				product.setRank(rank);
			} else if (product.calculateProductTotalRevenue() == pList.get(productIndex - 1)
					.calculateProductTotalRevenue()) {
				product.setRank(rank);
			} else {
				rank = productIndex + 1;
				product.setRank(rank);
			}

		}
	}
	
	
	public void rankProductTypeByTotalRevenue() {
		int rank = 1;

		for (ProductType productType : productTypeList) {
			int productTypeIndex = productTypeList.indexOf(productType);

			if (productTypeIndex == 0) {
				productType.setRank(rank);
			} else if (productType.calculateProductTypeTotalRevenue() == productTypeList.get(productTypeIndex - 1)
					.calculateProductTypeTotalRevenue()) {
				productType.setRank(rank);
			} else {
				rank = productTypeIndex + 1;
				productType.setRank(rank);
			}

		}
	}

	public void rankProductTypeBySalesVolume() {
		int rank=1;
		
		for (ProductType productType: productTypeList) {
			int productTypeIndex = productTypeList.indexOf(productType);
		
		
		if(productTypeIndex==0) {
			productType.setRank(rank);
		}else if (productType.calculateProductTypeSalesVolume()==productTypeList.get(productTypeIndex-1).calculateProductTypeSalesVolume()) {
			productType.setRank(rank);
		}else {
			rank=productTypeIndex+1;
			productType.setRank(rank);				
		}
		
		}
	}
	
	public void sortProductBySalesVolume(ArrayList<Product> list) {
		list.sort(new Comparator<Product>() {
			
			@Override
			public int compare(Product p1, Product p2) {
				// TODO Auto-generated method stub
				double p1SalesVolume = p1.calculateProductSalesVolume();
				double p2SalesVolume = p2.calculateProductSalesVolume();

				if (p1SalesVolume > p2SalesVolume) {
					return -1;
				} else if (p1SalesVolume == p2SalesVolume) {
					return 0;
				} else {
					return 1;
				}

			}

		});
	}
	
	public void sortProductTypeBySalesVolume(ArrayList<ProductType> list) {
		list.sort(new Comparator<ProductType>() {
			
			@Override
			public int compare(ProductType pt1, ProductType pt2) {
				// TODO Auto-generated method stub
				double pt1SalesVolume = pt1.calculateProductTypeSalesVolume();
				double pt2SalesVolume = pt2.calculateProductTypeSalesVolume();

				if (pt1SalesVolume > pt2SalesVolume) {
					return -1;
				} else if (pt1SalesVolume == pt2SalesVolume) {
					return 0;
				} else {
					return 1;
				}

			}

		});
	}
	
	
	public ArrayList<Double> getProductSalesChannelTotalRevenue() {
		return productSalesChannelTotalRevenue;
	}

	public void setProductSalesChannelTotalRevenue(ArrayList<Double> productSalesChannelTotalRevenue) {
		this.productSalesChannelTotalRevenue = productSalesChannelTotalRevenue;
	}

	public ArrayList<Integer> getProductSalesChannelSalesVolume() {
		return productSalesChannelSalesVolume;
	}

	public void setProductSalesChannelSalesVolume(ArrayList<Integer> productSalesChannelSalesVolume) {
		this.productSalesChannelSalesVolume = productSalesChannelSalesVolume;
	}
	

}
