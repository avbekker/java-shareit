package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.exception.OnCreate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private long id;

    @NotBlank(groups = {OnCreate.class})
    private String description;

    private LocalDateTime created;

    private List<ItemDtoResponse> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequestDto that = (ItemRequestDto) o;
        return id == that.id
                && Objects.equals(description, that.description)
                && Objects.equals(created, that.created)
                && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, created, items);
    }
}
