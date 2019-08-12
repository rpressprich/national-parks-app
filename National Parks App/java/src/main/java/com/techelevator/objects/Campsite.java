package com.techelevator.objects;

public class Campsite {

	private long siteId;
	private long campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean isHandicapAccessible;
	private int maxRvLength;
	private boolean hasUtilityHookups;

	public Campsite(long siteId, long campgroundId, int siteNumber, int maxOccupancy, boolean isHandicapAccessible,
			int maxRvLength, boolean hasUtilityHookups) {
		this.siteId = siteId;
		this.campgroundId = campgroundId;
		this.siteNumber = siteNumber;
		this.maxOccupancy = maxOccupancy;
		this.isHandicapAccessible = isHandicapAccessible;
		this.maxRvLength = maxRvLength;
		this.hasUtilityHookups = hasUtilityHookups;
	}

	public Campsite() {
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public long getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(long campgroundId) {
		this.campgroundId = campgroundId;
	}

	public int getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public boolean isHandicapAccessible() {
		return isHandicapAccessible;
	}

	public void setHandicapAccessible(boolean isHandicapAccessible) {
		this.isHandicapAccessible = isHandicapAccessible;
	}

	public int getMaxRvLength() {
		return maxRvLength;
	}

	public void setMaxRvLength(int maxRvLength) {
		this.maxRvLength = maxRvLength;
	}

	public boolean hasUtilityHookups() {
		return hasUtilityHookups;
	}

	public void setUtilityHookups(boolean hasUtilityHookups) {
		this.hasUtilityHookups = hasUtilityHookups;
	}

}
