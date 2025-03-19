package fi.haagahelia.sokos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Please provide a check-in date.")
    @Column(nullable = false)
    private LocalDate checkInDate;

    @Future(message = "Check-out date must be a future date.")
    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Min(value = 1, message = "At least one adult is required for booking.")
    @Column(nullable = false)
    private int numOfAdults;

    @Min(value = 0, message = "Number of children cannot be negative.")
    @Column(nullable = false)
    private int numOfChildren;

    @Column(nullable = false)
    private int totalNumOfGuest;

    @Column(unique = true)
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @PrePersist
    @PreUpdate
    private void updateTotalGuests() {
        this.totalNumOfGuest = this.numOfAdults + this.numOfChildren;
    }
}
