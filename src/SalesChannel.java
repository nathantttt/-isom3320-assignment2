package asg2;

import java.util.ArrayList;

public class SalesChannel {
	
	private String channelName;
	private int salesQuantity;
	
	public SalesChannel(String channelName, int quantity) {
		this.setChannelName(channelName);
		this.setSalesQuantity(quantity);
		
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getSalesQuantity() {
		return salesQuantity;
	}

	public void setSalesQuantity(int salesQuantity) {
		this.salesQuantity = salesQuantity;
	}

}
