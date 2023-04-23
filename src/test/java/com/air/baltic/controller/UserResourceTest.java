package com.air.baltic.controller;

import com.air.baltic.model.UserDTO;
import com.air.baltic.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {
    private static final String TEST_DATA_PATH = "src/test/resources/testData";
    private static final String API_URL = "/users";

    @Mock
    private UserService mockService;
    @InjectMocks
    private UserResource controller;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createUser() throws Exception {
        var request = getContentFromFile("/input/newUser.json");

        mockMvc.perform(
                        post(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        verify(mockService).addUser(any(UserDTO.class));
    }

    @Test
    void createUserFailed() throws Exception {
        var request = getContentFromFile("/input/notValidUser.json");

        mockMvc.perform(
                        post(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(mockService);
    }

    @Test
    void findUserById() throws Exception {
        var request = getContentFromFile("/input/findUser.json");
        var response = response();

        when(mockService.findUserById(any(Long.class))).thenReturn(Optional.of(response));

        mockMvc.perform(
                        get(API_URL+ "/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(mockService).findUserById(any(Long.class));
    }

    @Test
    void findUserByIdReturnNotFound() throws Exception {
        var request = getContentFromFile("/input/findUser.json");

        mockMvc.perform(
                        get(API_URL+ "/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(mockService).findUserById(any(Long.class));
    }

    @Test
    void fetchAllUsers() throws Exception {
        mockMvc.perform(
                        get(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(List.of().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(mockService).fetchAllUsers();
    }

    @Test
    void updateUser() throws Exception {
        var request = getContentFromFile("/input/updatedUser.json");

        mockMvc.perform(
                        put(API_URL + "/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(mockService).updateUser(any(UserDTO.class), eq(1L));
    }

    @Test
    void updateUserFailedNoProvidedId() throws Exception {
        var request = getContentFromFile("/input/updatedUser.json");

        mockMvc.perform(
                        put(API_URL + "/{id}", "")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andReturn();

        verifyNoInteractions(mockService);
    }

    @Test
    void deleteUserById() throws Exception {
        mockMvc.perform(
                        delete(API_URL + "/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(mockService).deleteUser(1L);
    }

    @Test
    void deleteUserFailedWithoutId() throws Exception {
        mockMvc.perform(
                        delete(API_URL + "/{id}", ""))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(mockService);
    }

    private UserDTO response() {
        return UserDTO.builder()
                .firstName("Olegs")
                .lastName("Kuzmins")
                .email("ol-kuz@inbox.lv")
                .build();
    }

    private static String getContentFromFile(String path) throws IOException {
        File file = new File(TEST_DATA_PATH + path);
        JsonNode jsonNode = objectMapper.readTree(file);
        return objectMapper.writeValueAsString(jsonNode);
    }
}