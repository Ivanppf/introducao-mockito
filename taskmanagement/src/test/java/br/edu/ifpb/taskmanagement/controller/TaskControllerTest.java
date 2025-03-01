package br.edu.ifpb.taskmanagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.taskmanagement.model.entity.TaskEntity;
import br.edu.ifpb.taskmanagement.model.repository.TaskRepository;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class TaskControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    MockMvc mockMvc;

    Long taskId;

    @Test
    // @Order(1)
    void testCreateTask() throws Exception {

        TaskEntity task = new TaskEntity();
        task.setTitle("title");
        task.setDescription("description");
        task.setCompleted(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"));

    }

    @Test
    // @Order(2)
    void testGetAllTasks() throws Exception {
        TaskEntity task1 = new TaskEntity(null, "title1", false);
        task1.setDescription("description1");

        taskRepository.save(task1);

        TaskEntity task2 = new TaskEntity(null, "title2", false);
        task2.setDescription("description2");

        taskRepository.save(task2);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("title1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("title2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("description2"));

    }

    @Test
    // @Order(3)
    void testGetTaskById() throws Exception {
        TaskEntity task1 = new TaskEntity(null, "title1", false);
        task1.setDescription("description1");

        taskId = taskRepository.save(task1).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description1"));
    }

    @Test
    // @Order(4)
    void testUpdateTask() throws Exception {
        TaskEntity oldTask = new TaskEntity(null, "oldTitle", false);
        oldTask.setDescription("oldDescription");

        taskRepository.save(oldTask);

        TaskEntity newTask = new TaskEntity(null, "newTitle", false);
        newTask.setDescription("newDescription");

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("newTitle"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("newDescription"));

    }

    @Test
    void testDeleteTask() throws Exception {
        System.out.println("aaaaaaaaaaaa: " + taskRepository.count());

        TaskEntity task1 = new TaskEntity(null, "title1", false);
        task1.setDescription("description1");
        taskRepository.save(task1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertFalse(taskRepository.findById(1l).isPresent());

    }
}
