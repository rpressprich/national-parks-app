package com.techelevator.objects;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(BasicDataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Campground> getCampgroundsByPark(Park park) { // returns a list of grounds given a park
		List<Campground> campgrounds = new ArrayList<Campground>();
		String sqlGetCampgroundsByPark = "SELECT * FROM campground WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampgroundsByPark, park.getId());

		while (results.next()) {
			Campground thisCampground = new Campground(results.getLong("campground_id"), results.getLong("park_id"),
					results.getString("name"), results.getInt("open_from_mm"), results.getInt("open_to_mm"),
					results.getDouble("daily_fee"));
			campgrounds.add(thisCampground);
		}
		return campgrounds;
	}
}
