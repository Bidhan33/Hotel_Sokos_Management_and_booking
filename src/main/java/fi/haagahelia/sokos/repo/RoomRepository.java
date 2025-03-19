package fi.haagahelia.sokos.repo;

import fi.haagahelia.sokos.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Fetches all distinct room types available in the database.
     *
     * @return List of unique room types.
     */
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    /**
     * Finds available rooms of a specific type that are not booked within a given date range.
     *
     * @param checkInDate  The start date of the booking.
     * @param checkOutDate The end date of the booking.
     * @param roomType     The type of room requested.
     * @return List of available rooms that match the criteria.
     */
    @Query("""
        SELECT r FROM Room r 
        WHERE LOWER(r.roomType) LIKE LOWER(CONCAT('%', :roomType, '%')) 
        AND r.id NOT IN (
            SELECT bk.room.id FROM Booking bk 
            WHERE (:checkInDate < bk.checkOutDate AND :checkOutDate > bk.checkInDate)
        )
    """)
    List<Room> findAvailableRoomsByDatesAndTypes(
        @Param("checkInDate") LocalDate checkInDate, 
        @Param("checkOutDate") LocalDate checkOutDate, 
        @Param("roomType") String roomType
    );

    /**
     * Retrieves all rooms that are currently available (not booked).
     *
     * @return List of rooms that have no active bookings.
     */
    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM Booking b)")
    List<Room> getAllAvailableRooms();
}
