package com.techelevator.objects;

import java.time.LocalDate;

public class Reservation
{
  private long reservationId;
  private long siteId;
  private String name;
  private LocalDate fromDate;
  private LocalDate toDate;
  private LocalDate createDate;

  public Reservation(long reservationId, long siteId, String name, LocalDate fromDate, LocalDate toDate,
      LocalDate createDate)
  {
    this.reservationId = reservationId;
    this.siteId = siteId;
    this.name = name;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.createDate = createDate;
  }

  public long getReservationId()
  {
    return reservationId;
  }

  public void setReservationId(long reservationId)
  {
    this.reservationId = reservationId;
  }

  public long getSiteId()
  {
    return siteId;
  }

  public void setSiteId(long siteId)
  {
    this.siteId = siteId;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public LocalDate getFromDate()
  {
    return fromDate;
  }

  public void setFromDate(LocalDate fromDate)
  {
    this.fromDate = fromDate;
  }

  public LocalDate getToDate()
  {
    return toDate;
  }

  public void setToDate(LocalDate toDate)
  {
    this.toDate = toDate;
  }

  public LocalDate getCreateDate()
  {
    return createDate;
  }

  public void setCreateDate(LocalDate createDate)
  {
    this.createDate = createDate;
  }
}
