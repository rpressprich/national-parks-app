package com.techelevator.objects;

import java.time.LocalDate;

public class Park
{
  private long id;
  private String name;
  private String location;
  private LocalDate establishDate;
  private int acreage;
  private int annualVisitors;
  private String description;

  public Park(long id, String name, String location, LocalDate establishDate, int acreage, int annualVisitors,
      String description)
  {
    this.id = id;
    this.name = name;
    this.location = location;
    this.establishDate = establishDate;
    this.acreage = acreage;
    this.annualVisitors = annualVisitors;
    this.description = description;
  }

  public Park()
  {
    this.id = 0;
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getLocation()
  {
    return location;
  }

  public void setLocation(String location)
  {
    this.location = location;
  }

  public LocalDate getEstablishDate()
  {
    return establishDate;
  }

  public void setEstablishDate(LocalDate establishDate)
  {
    this.establishDate = establishDate;
  }

  public int getAcreage()
  {
    return acreage;
  }

  public void setAcreage(int acreage)
  {
    this.acreage = acreage;
  }

  public int getAnnualVisitors()
  {
    return annualVisitors;
  }

  public void setAnnualVisitors(int annualVisitors)
  {
    this.annualVisitors = annualVisitors;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }
}
