package fi.haagahelia.sokos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "bookings")
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Room type is required.")
    @Size(min = 3, message = "Room type must be at least 3 characters long.")
    @Column(nullable = false)
    private String roomType;

    @NotNull(message = "Room price cannot be null.")
    @Min(value = 0, message = "Room price must be at least 0.")
    @Column(nullable = false)
    private BigDecimal roomPrice;

    private String roomPhotoUrl;

    @NotNull(message = "Room description is required.")
    @Size(min = 10, message = "Room description must be at least 10 characters long.")
    @Column(nullable = false, length = 500)
    private String roomDescription;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}
