package com.playground.javaslang;
import java.math.BigDecimal;

public class Item implements Comparable<Item>
{

    private String name;
    private int qty;
    private BigDecimal price;
    
    
	public Item(String name, int qty, BigDecimal price)
	{
		super();
		this.name = name;
		this.qty = qty;
		this.price = price;
	}
	
	
	public String getName()
	{
		return name;
	}
	public int getQty()
	{
		return qty;
	}
	public BigDecimal getPrice()
	{
		return price;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setQty(int qty)
	{
		this.qty = qty;
	}
	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	@Override
	public int compareTo(Item o)
	{
		// TODO Auto-generated method stub
		return Integer.compare(this.getQty(), o.getQty());
	}
    
}
