package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(long ownerId);

    List<Item> search(@Param("text") String text);

    Optional<Item> findByIdAndOwnerId(long id, long ownerId);
}
