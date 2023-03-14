package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByCreatorIdOrderByRequestTimeAsc(Long userId);

    @Query(value = "SELECT r " +
            "FROM ItemRequest r " +
            "WHERE r.creatorId <> :userId")
    List<ItemRequest> findAllByCreatorIdNotLike(Long userId, Pageable pageable);
}
