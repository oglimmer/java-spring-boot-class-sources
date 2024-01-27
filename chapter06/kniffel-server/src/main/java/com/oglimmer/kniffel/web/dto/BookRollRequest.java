package com.oglimmer.kniffel.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Book rolls into category request")
@Getter
@Setter
@ToString
public class BookRollRequest {
    @Schema(description = "Booking type to book the current roll for. Must be one of the available booking types.",
            example = "ONES",
            allowableValues = {"ONES", "TWOS", "THREES", "FOURS", "FIVES", "SIXES", "THREE_OF_A_KIND", "FOUR_OF_A_KIND", "FULL_HOUSE", "SMALL_STRAIGHT", "LARGE_STRAIGHT", "CHANCE", "KNIFFEL"})
    // one string from availableBookingTypes
    private String bookingType;
}
