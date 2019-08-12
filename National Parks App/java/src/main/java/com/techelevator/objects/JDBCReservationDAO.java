package com.techelevator.objects;

import java.time.LocalDate;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCReservationDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(BasicDataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void makeReservation(String name, int campsiteId, DateRange dateRange) // Given a name, an id, and a range of
																					// dates, insert a new reservation
																					// into the database
	{
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('reservation_reservation_id_seq')");
		long nextReservationId = 0;
		if (nextIdResult.next()) {
			nextReservationId = nextIdResult.getLong("nextval"); // Returns the next available reservation id
		} else {
			System.out.println("Something went wrong getting your reservation ID");
		}

		String sqlMakeReservation = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) VALUES (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlMakeReservation, nextReservationId, campsiteId, name, dateRange.getStartDate(),
				dateRange.getEndDate(), LocalDate.now());

		System.out.println("The reservation has been made and the confirmation id is " + nextReservationId);
	}

	public void showUpcomingReservations() { // Prints all reservations that start in the next 30 days
		String sqlShowUpcomingReservations = "SELECT reservation.reservation_id, site.site_id, campground.campground_id, campground.name AS campground_name, reservation.name AS reservation_name, reservation.from_date, reservation.to_date "
				+ "FROM reservation JOIN site ON reservation.site_id = site.site_id "
				+ "JOIN campground ON campground.campground_id = site.campground_id "
				+ "WHERE reservation.from_date <= ? AND reservation.from_date >= ? "
				+ "ORDER BY reservation.from_date, reservation.reservation_id, reservation.site_id";
		// Specifies that all starting dates must be today or later, and 30 days from
		// now or before
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlShowUpcomingReservations, LocalDate.now().plusDays(30),
				LocalDate.now());
		System.out.printf("%-7s %-8s %-11s %-35s %-30s %-15s %-15s\n", "Res. ID", "Site ID", "Campgr. ID",
				"Campgr. Name", "Res. Name", "Res. Start Date", "Res. End Date");
		while (results.next()) {
			System.out.printf("%-7d %-8d %-11d %-35s %-30s %-15s %-15s\n", results.getLong("reservation_id"),
					results.getLong("site_id"), results.getLong("campground_id"), results.getString("campground_name"),
					results.getString("reservation_name"), results.getDate("from_date"), results.getDate("to_date"));
		}
	}
}
