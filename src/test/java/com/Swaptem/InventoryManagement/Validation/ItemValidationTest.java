package com.Swaptem.InventoryManagement.Validation;


import com.Swaptem.InventoryManagement.Entity.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemValidationTest {

    private ItemValidation itemValidation;

    @BeforeEach
    void setup() throws Exception{
        itemValidation = new ItemValidation();
    }


    @Test
    public void ItemNameIsValidTest_Pass(){
        // Arrange
        Item item = new Item();
        item.setName("ItemEenName");

        boolean expectedResult = true;

        // Act
        boolean result = itemValidation.ItemNameIsValid(item.getName());

        // Assert
        assertEquals(expectedResult,result);
    }

    @Test
    public void ItemNameIsValidTest_Fail(){
        // Arrange
        Item item = new Item();
        item.setName("ItemEenNameIsOver25Characters");

        boolean expectedResult = false;

        // Act
        boolean result = itemValidation.ItemNameIsValid(item.getName());

        // Assert
        assertEquals(expectedResult,result);
    }


    @Test
    public void ItemDescriptionIsValidTest_Pass(){
        // Arrange
        Item item = new Item();
        item.setDescription("ItemEenDescription");

        boolean expectedResult = true;

        // Act
        boolean result = itemValidation.ItemDescriptionIsValid(item.getDescription());

        // Assert
        assertEquals(expectedResult,result);
    }


    @Test
    public void ItemDescriptionIsValidTest_Fail(){
        // Arrange
        Item item = new Item();
        item.setDescription("ItemEenDescriptionIsWayTooLongBecauseItContainsMoreThanOneHundredCharactersWhichIsNotAllowedThereforeItFails");

        boolean expectedResult = false;

        // Act
        boolean result = itemValidation.ItemDescriptionIsValid(item.getDescription());

        // Assert
        assertEquals(expectedResult,result);
    }

}
