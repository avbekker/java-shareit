package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i " +
            "WHERE UPPER(i.name) LIKE UPPER(concat('%', :text, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(concat('%', :text, '%')) " +
            "AND i.available = true")
    List<Item> search(String text, PageRequest pageRequest);

    @Query("SELECT i FROM Item i " +
            "WHERE i.owner.id = :userId " +
            "ORDER BY i.id")
    List<Item> findAllByOwnerId(Long userId, PageRequest pageRequest);

    List<Item> findAllByRequestId(long requestId);

    List<Item> findAllByRequestIn(List<ItemRequest> itemRequests);
}
