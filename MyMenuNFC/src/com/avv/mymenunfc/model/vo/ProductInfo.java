package com.avv.mymenunfc.model.vo;

import java.io.Serializable;

public class ProductInfo implements Serializable {

	private Integer productId;
	private String name;
	private Number price;

	public Number getPrice() {
		return this.price;
	}

	public void setPrice(Number price) {
		this.price = price;
	}

	public ProductInfo() {

	}

	public ProductInfo(Integer productId, String name) {
		super();
		this.productId = productId;
		this.name = name;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
