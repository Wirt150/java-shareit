package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.error.ItemRequestNotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemRequest add(ItemRequest toItemRequest, long authorId) {
        userService.getById(authorId);
        return itemRequestRepository.save(toItemRequest);
    }

    @Override
    public ItemRequest getById(final long id, final long authorId) {
        userService.getById(authorId);
        ItemRequest itemRequest = itemRequestRepository.findById(id).orElseThrow(() -> new ItemRequestNotFoundException(id));
        fillItemById(itemRequest);
        return itemRequest;
    }

    @Override
    public List<ItemRequest> getAll(final long authorId) {
        userService.getById(authorId);
        return itemRequestRepository.findAllByAuthorIdOrderByCreatedAsc(authorId).stream()
                .map(this::fillItemById)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequest> getPage(final int from, final int size, final long userId) {
        userService.getById(userId);
        return itemRequestRepository.findAllByAuthorIdNotOrderByCreatedAsc(PageRequest.of(from, size), userId)
                .get()
                .map(this::fillItemById)
                .collect(Collectors.toList());
    }

    private ItemRequest fillItemById(ItemRequest itemRequest) {
        itemRequest.setItems(new ArrayList<>(itemRepository.findAllByRequestId(itemRequest.getId())));
        return itemRequest;
    }
}
