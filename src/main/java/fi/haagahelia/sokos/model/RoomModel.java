package fi.haagahelia.sokos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomModel {

    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
    private List<BookingModel> bookings;
}