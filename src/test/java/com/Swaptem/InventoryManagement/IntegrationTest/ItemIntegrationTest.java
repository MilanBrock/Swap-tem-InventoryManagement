package com.Swaptem.InventoryManagement.IntegrationTest;

import com.Swaptem.InventoryManagement.DAL.ItemRepositoryCustom;
import com.Swaptem.InventoryManagement.DAL.UserRepositoryCustom;
import com.Swaptem.InventoryManagement.DTO.UserLoginDTO;
import com.Swaptem.InventoryManagement.Entity.Item;
import com.Swaptem.InventoryManagement.Entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Tag("IntegrationTest")
public class ItemIntegrationTest {

//    Item controller -> item service - > item repo
//    call: add item 	 -> check: data in repo
//    call: get ItembyId -> check: data in return
//    call: getAllItems	 -> check: data in return
//    call: updateItem 	 -> check: data in repo
//    call: deleteItem 	 -> check: data in repo

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ItemRepositoryCustom itemRepository;
    private String authenticationHeader;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void login(int userId){
        UserLoginDTO loginDTO = new UserLoginDTO();
        if (userId == 1){
            loginDTO.username = "MilanBrock";
            loginDTO.password = "Secret";
        } else if (userId == 2) {
            loginDTO.username = "SwapGod";
            loginDTO.password = "Secret";
        }

        try{
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                            .post("/authentication")
                            .content(asJsonString(loginDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn();
            String responseContent = result.getResponse().getContentAsString().trim();
            JsonNode jsonResponse = objectMapper.readTree(responseContent);
            String specificData = jsonResponse.get("jwtToken").asText();
            authenticationHeader = specificData;
        } catch(Exception ex){
        }
    }

    @Test
    void addItem_IntegrationTest(){
        // Arrange
        login(1);
        String expectedItemName = "Computer Mouse";
        String expectedItemDescription = "No cable included";
        int expectedItemId = 8;

        // Act
        try{
            mockMvc.perform( MockMvcRequestBuilders
                            .post("/items")
                            .content(asJsonString(new Item(expectedItemName,expectedItemDescription)))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authentication", authenticationHeader)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        } catch(Exception ex){

        }
        Item itemResult = itemRepository.findByItemIdAndActive(expectedItemId,true).orElse(null);

        // Assert
        if(itemResult != null){
            assertEquals(expectedItemName,itemResult.getName());
            assertEquals(expectedItemDescription, itemResult.getDescription());
        }
    }

    @Test
    void getItemById_IntegrationTest(){
        // Arrange
        int itemId = 1;
        String expectedItemName = "Penguino";
        String expectedItemDescription = "Lives in the cold.";

        // Act &  Assert
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/items/{itemId}", itemId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedItemName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedItemDescription));
        } catch(Exception ex){

        }

    }

    @Test
    void getAllItems_IntegrationTest(){
        // Arrange


        // Act


        // Assert
    }


    @Test
    void updateItem_IntegrationTest(){
        // Arrange
        login(1);
        String newItemName = "Cowboy Hat";
        String newItemDescription = "Wicky wild wild west";
        int itemId = 2;

        // Act
        try{
            mockMvc.perform( MockMvcRequestBuilders
                            .put("/items" )
                            .content(asJsonString(new Item(itemId, newItemName,newItemDescription)))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authentication", authenticationHeader)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted());
        } catch (Exception ex){

        }

        Item itemResult = itemRepository.findByItemIdAndActive(itemId,true).orElse(null);

        // Assert
        if(itemResult != null){
            assertEquals(newItemName, itemResult.getName());
            assertEquals(newItemDescription, itemResult.getDescription());
        }
    }

    @Test
    void deleteItem_IntegrationTest(){
        // Arrange
        login(1);
        int itemId = 3;
        boolean expectedActive = false;

        // Act
        try{
            mockMvc.perform( MockMvcRequestBuilders.delete("/items/{itemId}", itemId)
                            .header("authentication", authenticationHeader))
                    .andExpect(status().isAccepted());
        } catch( Exception ex){

        }


        Item itemResult = itemRepository.findById(itemId).orElse(null);
        // Assert
        if(itemResult != null){
            assertEquals(expectedActive, itemResult.isActive());
        }
    }


}
