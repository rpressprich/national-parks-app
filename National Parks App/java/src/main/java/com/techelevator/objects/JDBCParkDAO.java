package com.techelevator.objects;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCParkDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(BasicDataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Park> getParks() // returns a list of all parks in the system
	{
		List<Park> parks = new ArrayList<Park>();
		String sqlGetParks = "SELECT * FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParks);

		while (results.next()) {
			Park thisPark = new Park(results.getLong("park_id"), results.getString("name"),
					results.getString("location"), results.getDate("establish_date").toLocalDate(),
					results.getInt("area"), results.getInt("visitors"), results.getString("description"));
			parks.add(thisPark);
		}
		return parks;
	}
}
