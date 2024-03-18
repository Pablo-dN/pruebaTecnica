package org.example.avoris.dto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.example.avoris.validation.annotation.EachPositive;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public final class SearchRequest {

  @NotBlank(message = "hotelId is required")
  @NotNull(message = "hotelId cannot be null")
  @Pattern(regexp = "^[A-Za-z0-9]{7}$", message = "Invalid hotelId format")
  private final String hotelId;
  @NotNull(message = "checkIn is required")
  @Future(message = "checkIn date must be in the future")
  @DateTimeFormat(pattern = "dd/MM/yyyy")  @JsonFormat(pattern = "dd/MM/yyyy")
  private final LocalDate checkIn;
  @NotNull(message = "checkOut is required")
  @Future(message = "checkOut date must be in the future")
  @DateTimeFormat(pattern = "dd/MM/yyyy")  @JsonFormat(pattern = "dd/MM/yyyy")
  private final LocalDate checkOut;
  @NotNull(message = "ages cannot be null")
  @Size(min = 1, message = "At least one age must be provided")
  @EachPositive(message = "Each age must be a positive number")
  private final List<Integer> ages;

  @JsonCreator
  public SearchRequest(@JsonProperty("hotelId") String hotelId,
      @JsonProperty("checkIn") LocalDate checkIn,
      @JsonProperty("checkOut") LocalDate checkOut,
      @JsonProperty("ages") List<Integer> ages) {
    this.hotelId = hotelId;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
    this.ages = ages;
  }

  public String getHotelId() {
    return hotelId;
  }

  public LocalDate getCheckIn() {
    return checkIn;
  }

  public LocalDate getCheckOut() {
    return checkOut;
  }

  public List<Integer> getAges() {
    return (ages != null) ? Collections.unmodifiableList(ages) : null;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SearchRequest that = (SearchRequest) o;
    return Objects.equals(hotelId, that.hotelId) &&
        Objects.equals(checkIn, that.checkIn) &&
        Objects.equals(checkOut, that.checkOut) &&
        Objects.equals(ages, that.ages);
  }
}
