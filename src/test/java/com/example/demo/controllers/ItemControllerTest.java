package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository repository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController(repository);

        Item item1 = getItem(1L, "test_item_01");
        Item item2 = getItem(2L, "test_item_XX");
        Item item3 = getItem(3L, "test_item_XX");

        when(repository.findById(1L)).thenReturn(Optional.of(item1));
        when(repository.findByName(item2.getName())).thenReturn(Lists.list(item2, item3));
        when(repository.findAll()).thenReturn(Lists.list(item1, item2, item3));
    }

    @Test
    public void whenFindAllItems_thenAllItemsAreReturned() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        int codeValue = responseEntity.getStatusCodeValue();
        List<Item> items = responseEntity.getBody();

        assertEquals(codeValue, HttpStatus.OK.value());
        assertNotNull(items);
        assertEquals(items.size(), 3);
    }

    @Test
    public void whenFindItemById_thenItemIsReturned() {
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        int codeValue = responseEntity.getStatusCodeValue();
        Item item = responseEntity.getBody();

        assertEquals(codeValue, HttpStatus.OK.value());
        assertNotNull(item);
        assertEquals(item.getId().longValue(), 1L);
    }

    @Test
    public void whenFindItemByNonExistentId_thenNotFoundErrorIsReturned() {
        ResponseEntity<Item> responseEntity = itemController.getItemById(4L);
        int codeValue = responseEntity.getStatusCodeValue();

        assertEquals(codeValue, HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void whenFindItemsByName_thenItemsReturned() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("test_item_XX");
        int codeValue = responseEntity.getStatusCodeValue();
        List<Item> items = responseEntity.getBody();

        assertEquals(codeValue, HttpStatus.OK.value());
        assertNotNull(items);
        assertEquals(items.size(), 2);
    }

    @Test
    public void whenFindItemsByNonExistentName_thenNotFoundErrorIsReturned() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("");
        int codeValue = responseEntity.getStatusCodeValue();

        assertEquals(codeValue, HttpStatus.NOT_FOUND.value());
    }

    private static Item getItem(long id, String name) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        return item;
    }
}
