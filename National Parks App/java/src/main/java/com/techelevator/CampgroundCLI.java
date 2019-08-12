package com.techelevator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.objects.Campground;
import com.techelevator.objects.Campsite;
import com.techelevator.objects.DateRange;
import com.techelevator.objects.JDBCCampgroundDAO;
import com.techelevator.objects.JDBCCampsiteDAO;
import com.techelevator.objects.JDBCParkDAO;
import com.techelevator.objects.JDBCReservationDAO;
import com.techelevator.objects.Park;
import com.techelevator.projects.view.Menu;

public class CampgroundCLI {
  private Menu menu; // Takes in an array of options and allows the user to choose the id of the
                     // choice they want
  private Scanner input;

  private static final String MAIN_MENU_OPTION_EXIT = "Exit";
  private static final String PARK_MENU_OPTION_LIST_CAMPGROUNDS = "View Campgrounds";
  private static final String PARK_MENU_OPTION_RESERVATIONS = "List Upcoming Reservations";
  private static final String PARK_MENU_OPTION_SEARCH_CAMPSITES = "Search Available Campsites Across Campgrounds";
  private static final String PARK_MENU_OPTION_ADVANCED_SEARCH_CAMPSITES = "ADVANCED Search Available Campsites Across Campgrounds";
  private static final String SUB_MENU_OPTION_EXIT = "Return to Previous Screen";
  private static final String[] PARK_MENU_OPTIONS = { PARK_MENU_OPTION_LIST_CAMPGROUNDS, PARK_MENU_OPTION_RESERVATIONS,
      PARK_MENU_OPTION_SEARCH_CAMPSITES, PARK_MENU_OPTION_ADVANCED_SEARCH_CAMPSITES, SUB_MENU_OPTION_EXIT };
  private static final String CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION = "Search For Available Reservation";
  private static final String CAMPGROUND_MENU_OPTION_ADVANCED_SEARCH = "ADVANCED Search For Available Reservations and Concerns";
  private static final String[] CAMPGROUND_MENU_OPTIONS = { CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION,
      CAMPGROUND_MENU_OPTION_ADVANCED_SEARCH, SUB_MENU_OPTION_EXIT };
  private static final String[] MONTHS = { "January", "February", "March", "April", "May", "June", "July", "August",
      "September", "October", "November", "December" };
  private static JDBCParkDAO jdbcParkDAO;
  private static JDBCCampgroundDAO jdbcCampgroundDAO;
  private static JDBCCampsiteDAO jdbcCampsiteDAO;
  private static JDBCReservationDAO jdbcReservationDAO;
  private List<Park> parks;

  public static void main(String[] args) {
    BasicDataSource dataSource = new BasicDataSource();
    jdbcParkDAO = new JDBCParkDAO(dataSource);
    jdbcCampgroundDAO = new JDBCCampgroundDAO(dataSource);
    jdbcCampsiteDAO = new JDBCCampsiteDAO(dataSource);
    jdbcReservationDAO = new JDBCReservationDAO(dataSource);
    Menu menu = new Menu(System.in, System.out);
    // Takes in an array of options and allows the user to choose the
    // id of the choice they want
    dataSource.setUrl("jdbc:postgresql://localhost:5432/nationalparks");
    dataSource.setUsername("postgres");
    dataSource.setPassword("postgres1");

    CampgroundCLI application = new CampgroundCLI(dataSource, menu);
    application.run();
  }

  public CampgroundCLI(DataSource datasource, Menu menu) {
    this.menu = menu;
  }

  public void run() {
    input = new Scanner(System.in);
    parks = new ArrayList<Park>(jdbcParkDAO.getParks()); // creates a list of all parks in the database
    String[] mainMenuOptions = getParkNamesArrayPlusExit(parks); // Holds the options for the top menu
    while (true) {
      System.out.print("** View Parks Interface **\n");
      System.out.print("\nSelect a Park for Further Details");
      String choice = (String) menu.getChoiceFromOptions(mainMenuOptions);
      // The menu only allows users to choose from the prescripted options
      for (Park park : parks) {
        if (choice == park.getName()) {
          printParkInfo(park); // sysouts all information related to the park
          while (true) {
            choice = (String) menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
            if (choice == PARK_MENU_OPTION_LIST_CAMPGROUNDS) {
              printCampgroundsMenu(park);
              // Lists campgrounds and allows the user to reserve a range of dates
            } else if (choice == PARK_MENU_OPTION_RESERVATIONS) {
              jdbcReservationDAO.showUpcomingReservations();
              // Lists reservations within the next 30 days
            } else if (choice == PARK_MENU_OPTION_SEARCH_CAMPSITES) {
              searchCampsites(park, false);
              // Allows the user to see the first 5 available campsites from each
              // campground within the park
            } else if (choice == PARK_MENU_OPTION_ADVANCED_SEARCH_CAMPSITES) {
              searchCampsites(park, true);
              // Allows the user to see the first 5 available campsites from each
              // campground within the park with added requirements
            } else if (choice == SUB_MENU_OPTION_EXIT) {
              break;
            }
          }
        }
      }
      if (choice == MAIN_MENU_OPTION_EXIT) {
        break;
      }
    }
    input.close();
  }

  private String[] getParkNamesArrayPlusExit(List<Park> parks) {
    String[] mainMenuOptions = new String[parks.size() + 1];
    // Holds the park names and exit String for the main menu
    for (int i = 0; i < parks.size(); i++) {
      mainMenuOptions[i] = parks.get(i).getName();
      // Adds the name from each of the parks
    }
    mainMenuOptions[parks.size()] = MAIN_MENU_OPTION_EXIT;
    // Adds the exit String to the end of the array

    return mainMenuOptions;
  }

  private void printParkInfo(Park park) // sysouts all info related to the park
  {
    System.out.print("** Park Information Screen **\n");
    System.out.println(park.getName() + " National Park");
    System.out.printf("%-17s: %s\n", "Location:", park.getName());
    System.out.printf("%-17s: %s\n", "Established:", park.getEstablishDate());
    System.out.printf("%-17s: %d\n", "Area:", park.getAcreage());
    System.out.printf("%-17s: %s\n", "Annual Visitors:", park.getAnnualVisitors());
    System.out.println("\n" + park.getDescription() + "\n");
  }

  private void printCampgroundsMenu(Park park) { // The campground menu allows users to search for available campsites
    List<Campground> campgrounds = jdbcCampgroundDAO.getCampgroundsByPark(park);
    printCampgroundInfo(campgrounds, park.getName());
    while (true) {
      String choice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
      if (choice == CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION) {
        listReservations(campgrounds, false);
        // Outputs 5 available sites and allows the user to reserve one over a range of
        // dates
      } else if (choice == CAMPGROUND_MENU_OPTION_ADVANCED_SEARCH) {
        listReservations(campgrounds, true);
        // Outputs 5 available sites and allows the user to reserve one over a range of
        // dates with added requirements
      } else if (choice == SUB_MENU_OPTION_EXIT) {
        break;
      }
    }
  }

  private void printCampgroundInfo(List<Campground> campgrounds, String parkName) { // sysouts all campground info
    System.out.println("\n** Park Campgrounds **");
    System.out.println(parkName + " National Park Campgrounds"); // returns National Parks data
    System.out.printf("\t%-35s %-12s %-12s %s", "Name", "Open", "Close", "Daily Fee");

    int campgroundCount = 0;
    for (Campground campground : campgrounds) {
      campgroundCount++;
      System.out.printf("\n%s\t%-35s %-12s %-12s $%.2f", "#" + campgroundCount, campground.getName(),
          MONTHS[campground.getOpenMonth() - 1], MONTHS[campground.getCloseMonth() - 1], campground.getFee());
    }
  }

  private void listReservations(List<Campground> campgrounds, boolean isAdvanced) {
    while (true) {
      int desiredCampgroundId = getValidCampgroundChoice(campgrounds); // Asks the user to input a campground
                                                                       // number or 0 to exit

      if (desiredCampgroundId == 0) {
        break;
      } else {
        desiredCampgroundId--; // Arrays start at 0, so we have to subtract one from the user's input
        DateRange dateRange = getRangeOfDatesFromUser(); // Specifies which dates the user is planning on
                                                         // staying
        if (isCampgroundOpen(campgrounds.get(desiredCampgroundId), dateRange)) {
          // Given a range of dates, returns true if the campground is open in that range
          List<Campsite> campsites = null;
          if (isAdvanced) {
            Campsite campsite = getDesiredOptionsForCampsite(); // Holds the specific requirements the user
                                                                // needs
                                                                // i.e. Handicap Access, RV Max Length, Max
                                                                // Occupancy, and Utility Hookups
            campsites = jdbcCampsiteDAO.getCampsiteByCampgroundAdvanced(campgrounds.get(desiredCampgroundId), dateRange,
                campsite);
            // Returns a List of the campsites which fit those requirements and are
            // available in that range of dates
          } else {
            campsites = jdbcCampsiteDAO.getCampsiteByCampground(campgrounds.get(desiredCampgroundId), dateRange);
            // Returns a List of campsites available within that range of dates
          }

          if (campsites.size() == 0) {
            System.out.print("No campsites available within requested range of dates, would you like to enter a "
                + "different one (y/n)? ");
            if (input.nextLine().toLowerCase().charAt(0) == 'n') {
              break; // If the user enters a string, starting with n, they return to the campground
                     // menu
            }
          } else {
            printCampsiteInfo(campsites, dateRange);

            makeReservation(campsites, dateRange);
            break;
          }
        } else { // if the campground is off-season
          System.out.println("The campground is closed to camping for the off-season.\n");
        }
      }
    }
  }

  private int getValidCampgroundChoice(List<Campground> campgrounds) {
    int desiredId = -1;
    while (desiredId < 0 || desiredId > campgrounds.size()) { // disallows the user from picking anything but a
                                                              // valid option
      System.out.print("Which campground # (0 to cancel)?");
      desiredId = getValidIntegerFromUser(); // Forces the user to input a valid int
      if (desiredId < 0 || desiredId > campgrounds.size()) {
        System.out.println("Not a valid choice.");
      }
    }
    return desiredId;
  }

  private int getValidIntegerFromUser() {
    int integer = 0;
    boolean isInvalid = true;
    while (isInvalid) {
      try {
        integer = Integer.parseInt(input.nextLine());
        isInvalid = false;
      } catch (NumberFormatException e) { // If the input isn't a number, tell the user to try again
        System.out.println("Invalid Input");
      }
    }
    return integer;
  }

  private DateRange getRangeOfDatesFromUser() {
    DateRange dateRange = new DateRange(); // Holds a start date and an end date
    System.out.print("What is the arrival date? yyyy-mm-dd ");
    while (true) {
      dateRange.setStartDate(getValidDateFromUser()); // Asks the user to input a date until it gets a valid input
      if (dateRange.getStartDate().isAfter((LocalDate.now().minusDays(1)))) { // The user is not allowed to
                                                                              // reserve spots from before today
        break;
      } else {
        System.out.println("Please enter a date that is after today.");
      }
    }
    System.out.print("What is the departure date? yyyy-mm-dd ");
    while (true) {
      dateRange.setEndDate(getValidDateFromUser());
      if (dateRange.getEndDate().isAfter(dateRange.getStartDate().minusDays(1))) { // If the user decides to leave
                                                                                   // before they show up, just don't
                                                                                   // let them do that
        break;
      } else {
        System.out.println("Please enter a date that is after your potential arrival date.");
      }
    }
    return dateRange;
  }

  private LocalDate getValidDateFromUser() {
    LocalDate date = null;
    boolean isInvalid = true;
    while (isInvalid) {
      try {
        date = LocalDate.parse(input.nextLine()); // Forces the user to follow the format and enter a valid date
        isInvalid = false;
      } catch (DateTimeParseException e) { // If the input wasn't a date, tell the user to try again
        System.out.println("Invalid Input");
      }
    }
    return date;
  }

  private boolean isCampgroundOpen(Campground campground, DateRange dateRange) {
    boolean isCampgroundOpen = false;
    int startMonth = dateRange.getStartDate().getMonthValue(); // Holds the month of the user's arrival
    int endMonth = dateRange.getEndDate().getMonthValue(); // Holds the month of the user's departure
    int campgroundOpenMonth = campground.getOpenMonth();
    int campgroundCloseMonth = campground.getCloseMonth();

    if (campgroundOpenMonth == 1 && campgroundCloseMonth == 12) { // If the campground is open year-round, we know
                                                                  // the answer
      isCampgroundOpen = true;
    } else if (campgroundOpenMonth <= campgroundCloseMonth) { // If the campground isn't open during New Year's Day,
                                                              // things are simpler
      isCampgroundOpen = (campgroundOpenMonth <= startMonth && campgroundCloseMonth >= endMonth
      // If the range of dates happens within the open season...
          && dateRange.getStartDate().getYear() == dateRange.getEndDate().getYear());
      // And the dates happen over the same year, we're in the clear
    } else { // If the campground is open over the new Year, things get complicated
      // Although there wasn't a campground that was, Rob thought this was necessary
      // for completeness
      if (dateRange.getStartDate().getYear() == dateRange.getEndDate().getYear()) { // If the user date range is
                                                                                    // within the same year then
        isCampgroundOpen = ((startMonth >= campgroundOpenMonth && endMonth >= campgroundOpenMonth)
            || (startMonth <= campgroundCloseMonth && endMonth <= campgroundCloseMonth));
        // Check if their range is either all after the opening month, or all before the
        // closing month
      } else if (dateRange.getStartDate().getYear() + 1 == dateRange.getEndDate().getYear()) { // If the user's
                                                                                               // trip takes
                                                                                               // place over
                                                                                               // the New Year
        isCampgroundOpen = (startMonth >= campgroundOpenMonth && endMonth <= campgroundCloseMonth);
        // Check if they arrive after the park opens, and leave before it closes
      }
    }

    return isCampgroundOpen;
  }

  private void printCampsiteInfo(List<Campsite> campsites, DateRange dateRange) { // Prints out all info related to
                                                                                  // campgrounds
    System.out.println("\nResults Matching Your Search Criteria");
    System.out.printf("%-8s %-10s %-11s %-13s %-7s %s\n", "Site No.", "Max Occup.", "Accessible?", "Max RV Length",
        "Utility", "Cost");

    for (Campsite campsite : campsites) {
      System.out.printf("%-8d %-10d %-11s %-13s %-7s $%.2f\n", campsite.getSiteId(), campsite.getMaxOccupancy(),
          campsite.isHandicapAccessible() ? "Yes" : "No",
          (campsite.getMaxRvLength() == 0) ? "N/A" : "" + campsite.getMaxRvLength(),
          campsite.hasUtilityHookups() ? "Yes" : "No", jdbcCampsiteDAO.getFeeFromCampground(campsite)
              * (dateRange.getEndDate().toEpochDay() - dateRange.getStartDate().toEpochDay() + 1));
      // The fee is calculated by subtracting the two dates and adding 1 to make sure
      // the day of arrival is counted and then multiplying by the campground fee
    }
  }

  private int getValidCampsiteChoice(List<Campsite> campsites) {
    int campsiteId = 0;
    boolean isAValidChoice = false;
    while (!isAValidChoice) {
      System.out.print("Please choose a campsite to reserve (0 to cancel)?");
      campsiteId = getValidIntegerFromUser();
      for (Campsite campsite : campsites) {
        if ((long) campsiteId == campsite.getSiteId() || campsiteId == 0) { // if the id entered doesn't match
                                                                            // one that was shown to the user..
          isAValidChoice = true;
        }
      }
      if (!isAValidChoice) {
        System.out.println("Please choose an ID from the ones presented."); // tell the user they messed up
      }
    }
    return campsiteId;
  }

  private void searchCampsites(Park park, boolean isAdvancedSearch) {
    List<Campground> campgrounds = jdbcCampgroundDAO.getCampgroundsByPark(park);
    List<Campsite> campsites = new ArrayList<Campsite>();
    DateRange dateRange = getRangeOfDatesFromUser();
    Campsite campsite = null;
    if (isAdvancedSearch) { // if the user chose advanced search, then...
      campsite = getDesiredOptionsForCampsite(); // If the search had added reqs, ask the user about the reqs
    }

    for (Campground campground : campgrounds) {
      if (isCampgroundOpen(campground, dateRange)) // checks each campground to see if it's
        // open during the specified date range
        if (isAdvancedSearch) {
          campsites.addAll(jdbcCampsiteDAO.getCampsiteByCampgroundAdvanced(campground, dateRange, campsite));
          // adds all campsites that are not reserved during that time period and match
          // the specified reqs
        } else {
          campsites.addAll(jdbcCampsiteDAO.getCampsiteByCampground(campground, dateRange));
          // adds all campsites that are not reserved during that time period
        }
    }

    if (campsites.size() == 0) {
      System.out.println("There are no campsites available in the requested date range. ");
    } else {
      printCampsiteInfoWithCampgroundInfo(campsites, dateRange);

      makeReservation(campsites, dateRange);
    }
  }

  private void printCampsiteInfoWithCampgroundInfo(List<Campsite> campsites, DateRange dateRange) {
    // Campground information is necessary for park wide searches, to help the user
    // find the right grounds and site
    // Prints out all info related to campsite campgrounds
    System.out.println("\nResults Matching Your Search Criteria");
    System.out.printf("%-30s %-11s %-9s %-11s %-12s %-14s %-8s %s\n", "Campgr. Name", "Campgr. ID", "Site No.",
        "Max Occup.", "Accessible?", "Max RV Length", "Utility", "Cost");

    for (Campsite campsite : campsites) {
      System.out.printf("%-30s %-11d %-9d %-11d %-12s %-14s %-8s $%.2f\n",
          jdbcCampsiteDAO.getNameFromCampground(campsite), jdbcCampsiteDAO.getIdFromCampground(campsite),
          campsite.getSiteId(), campsite.getMaxOccupancy(), campsite.isHandicapAccessible() ? "Yes" : "No",
          (campsite.getMaxRvLength() == 0) ? "N/A" : "" + campsite.getMaxRvLength(),
          campsite.hasUtilityHookups() ? "Yes" : "No", jdbcCampsiteDAO.getFeeFromCampground(campsite)
              * (dateRange.getEndDate().toEpochDay() - dateRange.getStartDate().toEpochDay() + 1));
    }
  }

  private Campsite getDesiredOptionsForCampsite() { // Asks the user to input various requirements of theirs
    Campsite campsite = new Campsite();
    System.out.print("Do you require a wheelchair accessible campsite? y/n ");
    campsite.setHandicapAccessible(input.nextLine().toLowerCase().charAt(0) == 'y');
    System.out.print("How many guests on the reservation? ");
    campsite.setMaxOccupancy(getValidIntegerFromUser());
    System.out.print("What size RV parking in feet do you need? Enter 0 for no parking needed: ");
    campsite.setMaxRvLength(getValidIntegerFromUser());
    System.out.print("Do you require a utility hookup? y/n ");
    campsite.setUtilityHookups(input.nextLine().toLowerCase().charAt(0) == 'y');

    return campsite;
  }

  private void makeReservation(List<Campsite> campsites, DateRange dateRange) {
    int campsiteId = getValidCampsiteChoice(campsites); // Asks the user to choose a campsite
    if (campsiteId != 0) { // If campsiteId is 0, then user is skipping reserving a spot
      System.out.print("What name should the reservation be made under? ");
      String reservationName = input.nextLine();

      jdbcReservationDAO.makeReservation(reservationName, campsiteId, dateRange);
      // Saves a reservation to the database
    }
  }
}