package com.techelevator.objects;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampsiteDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampsiteDAO(BasicDataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Campsite> getCampsiteByCampground(Campground campground, DateRange dateRange) {
		// returns a list of sites from a campground
		List<Campsite> campsites = new ArrayList<Campsite>();
		String sqlGetCampsiteByCampground = "SELECT * FROM site WHERE campground_id = ? EXCEPT SELECT site.site_id, "
				// The EXCEPT allows us to exclude sites that are already reserved in the time
				// period
				+ "campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities FROM site JOIN "
				+ "reservation ON site.site_id = reservation.site_id WHERE to_date > ? AND from_date < ? ORDER BY site_id LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampsiteByCampground, campground.getId(),
				dateRange.getStartDate(), dateRange.getEndDate());

		while (results.next()) {
			Campsite thisCampsite = new Campsite(results.getLong("site_id"), results.getLong("campground_id"),
					results.getInt("site_number"), results.getInt("max_occupancy"), results.getBoolean("accessible"),
					results.getInt("max_rv_length"), results.getBoolean("utilities"));
			campsites.add(thisCampsite);
		}
		return campsites;
	}

	public List<Campsite> getCampsiteByCampgroundAdvanced(Campground campground, DateRange dateRange,
			Campsite campsite) {
		List<Campsite> campsites = new ArrayList<Campsite>();
		String sqlGetCampsiteByCampground = "SELECT * FROM site "
				+ "WHERE campground_id = ? AND max_occupancy >= ? AND (accessible OR NOT ?) "
				// The (accessible/utilities OR NOT ?) statements allow us to only choose sites
				// with those reqs, if need
				// be, or all sites if not
				+ "AND (max_rv_length >= ?) AND (utilities OR NOT ?) "
				+ "EXCEPT SELECT site.site_id, campground_id, site_number, max_occupancy, "
				// The EXCEPT allows us to exclude sites that are already reserved in the time
				// period
				+ "accessible, max_rv_length, utilities FROM site JOIN reservation ON site.site_id = reservation.site_id "
				+ "WHERE to_date > ? AND from_date < ? ORDER BY site_id LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampsiteByCampground, campground.getId(),
				campsite.getMaxOccupancy(), campsite.isHandicapAccessible(), campsite.getMaxRvLength(),
				campsite.hasUtilityHookups(), dateRange.getStartDate(), dateRange.getEndDate());

		while (results.next()) {
			Campsite thisCampsite = new Campsite(results.getLong("site_id"), results.getLong("campground_id"),
					results.getInt("site_number"), results.getInt("max_occupancy"), results.getBoolean("accessible"),
					results.getInt("max_rv_length"), results.getBoolean("utilities"));
			campsites.add(thisCampsite);
		}
		return campsites;
	}

	public String getNameFromCampground(Campsite campsite) { // Given a campsite, pull the relevant data from the
																// grounds it's on
		String name = "";
		String sqlGetNameFromCampground = "SELECT campground.name FROM campground JOIN site ON campground.campground_id = site.campground_id WHERE site.site_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetNameFromCampground, campsite.getSiteId());
		if (result.next()) {
			name = result.getString("name");
		}
		return name;
	}

	public int getIdFromCampground(Campsite campsite) {
		int iD = 0;
		String sqlGetIdFromCampground = "SELECT campground.campground_id FROM campground JOIN site ON campground.campground_id = site.campground_id WHERE site.site_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetIdFromCampground, campsite.getSiteId());
		if (result.next()) {
			iD = result.getInt("campground_id");
		}
		return iD;
	}

	public double getFeeFromCampground(Campsite campsite) {
		double fee = 0;
		String sqlGetFeeFromCampground = "SELECT daily_fee FROM campground JOIN site ON campground.campground_id = site.campground_id WHERE site.site_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetFeeFromCampground, campsite.getSiteId());
		if (result.next()) {
			fee = result.getDouble("daily_fee");
		}
		return fee;
	}
}
