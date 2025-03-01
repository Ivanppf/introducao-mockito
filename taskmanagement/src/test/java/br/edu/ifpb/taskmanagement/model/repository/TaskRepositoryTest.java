package br.edu.ifpb.taskmanagement.model.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.edu.ifpb.taskmanagement.model.entity.TaskEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    TaskEntity task1;

    Long taskId;

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
        taskId = returnedTask.getId();
        assertNotNull(returnedTask, "Task shouldn't be null");
        assertEquals("titulo", returnedTask.getTitle(), "Task title should be 'titulo'");
        assertFalse(returnedTask.isCompleted(), "Task complete should be false");

    }

    @Test
    @Order(2)
    void findbyid() {
        taskId = taskRepository.save(task1).getId();
        Optional<TaskEntity> returnedOptionalTask = taskRepository.findById(taskId);
        assertTrue(returnedOptionalTask.isPresent(), "Task should be present");
        TaskEntity newTask = returnedOptionalTask.get();
        assertNotNull(newTask.getId(), "Task id shouldn't be null");
        assertEquals("titulo", newTask.getTitle(), "Task title should be 'titulo'");
        assertFalse(newTask.isCompleted(), "Task completed should be false");
    }

    @Test
    @Order(3)
    void findAll() {

        TaskEntity task2 = new TaskEntity();
        task2 = new TaskEntity();
        task2.setCompleted(false);
        task2.setTitle("titulo2");
        task2.setDescription("descrição2");

        taskId = taskRepository.save(task1).getId();
        TaskEntity returnedTask2 = taskRepository.save(task2);

        List<TaskEntity> taskList = taskRepository.findAll();

        assertNotNull(taskList);
        assertEquals(2, taskList.size());
        assertNotNull(taskList.get(0).getId());
        assertNotNull(taskList.get(1).getId());
        assertEquals(taskId, taskList.get(0).getId());
        assertEquals(returnedTask2.getId(), taskList.get(1).getId());

    }

    @Test

    void delete() {
        taskRepository.save(task1);

        assertTrue(taskRepository.findById(1l).isPresent(), "Task should be present");

        taskRepository.deleteById(1L);

        assertFalse(taskRepository.findById(1l).isPresent(), "Task should be deleted");

    }

}
