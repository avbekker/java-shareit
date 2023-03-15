package ru.practicum.shareit.bookingTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.storage.BookingRepository;

@DataJpaTest
@Rollback(value = false)
public class BookingRepositoryTests {

    @Autowired
    BookingRepository bookingRepository;


}
