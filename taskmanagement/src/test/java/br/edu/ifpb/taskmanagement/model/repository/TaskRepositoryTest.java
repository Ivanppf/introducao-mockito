package br.edu.ifpb.taskmanagement.model.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.ifpb.taskmanagement.model.entity.TaskEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    TaskEntity task1;

    @BeforeEach
    void setUpTask() {
        task1 = new TaskEntity();
        task1.setCompleted(false);
        task1.setTitle("titulo");
        task1.setDescription("descrição");
    }

    @Test
    @Order(1)
    void testSave() {
        TaskEntity returnedTask = taskRepository.save(task1);
        assertNotNull(returnedTask, "Task shouldn't be null");
        assertEquals("titulo", returnedTask.getTitle(), "Task title should be 'titulo'");
        assertFalse(returnedTask.isCompleted(), "Task complete should be false");

    }

    @Test
    @Order(2)
    void findbyid() {
        taskRepository.save(task1);
        Optional<TaskEntity> returnedTask = taskRepository.findById(1L);
        assertTrue(returnedTask.isPresent(), "Task shoult be present");
        TaskEntity newTask = returnedTask.get();
        assertNotNull(newTask.getId(), "Task id shouldn't be null");
        assertEquals("titulo", newTask.getTitle(), "Task title should be 'titulo'");
        assertFalse(newTask.isCompleted(), "Task completed should be false");
    }

    @Test
    @Order(3)
    void findAll() {

    }

    @Test

    void delete() {
        taskRepository.save(task1);
        taskRepository.deleteById(1L);

    }

}
