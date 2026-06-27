package com.sthouts.backend.service;

import com.sthouts.backend.dto.MenuItemDto;
import com.sthouts.backend.model.MenuItem;
import com.sthouts.backend.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public List<MenuItemDto> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public MenuItemDto createMenuItem(MenuItemDto menuItemDto) {
        MenuItem menuItem = mapToEntity(menuItemDto);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return mapToDto(savedMenuItem);
    }

    public MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        
        menuItem.setName(menuItemDto.getName());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setCategory(menuItemDto.getCategory());
        
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return mapToDto(updatedMenuItem);
    }

    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new IllegalArgumentException("Menu item not found");
        }
        menuItemRepository.deleteById(id);
    }

    private MenuItemDto mapToDto(MenuItem menuItem) {
        return MenuItemDto.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .price(menuItem.getPrice())
                .category(menuItem.getCategory())
                .build();
    }

    private MenuItem mapToEntity(MenuItemDto menuItemDto) {
        return MenuItem.builder()
                .name(menuItemDto.getName())
                .price(menuItemDto.getPrice())
                .category(menuItemDto.getCategory())
                .build();
    }
}
