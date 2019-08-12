package com.techelevator.objects;

public class Campground {

	private long id;
	private long parkId;
	private String name;
	private int openMonth;
	private int closeMonth;
	private double fee;

	public Campground(long id, long parkId, String name, int openMonth, int closeMonth, double fee) {
		this.id = id;
		this.parkId = parkId;
		this.name = name;
		this.openMonth = openMonth;
		this.closeMonth = closeMonth;
		this.fee = fee;
	}

	public Campground() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParkId() {
		return parkId;
	}

	public void setParkId(long parkId) {
		this.parkId = parkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOpenMonth() {
		return openMonth;
	}

	public void setOpenMonth(int openMonth) {
		this.openMonth = openMonth;
	}

	public int getCloseMonth() {
		return closeMonth;
	}

	public void setCloseMonth(int closeMonth) {
		this.closeMonth = closeMonth;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

}
