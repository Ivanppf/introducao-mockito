package br.edu.ifpb.taskmanagement.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.Task;

import br.edu.ifpb.taskmanagement.model.entity.TaskEntity;
import br.edu.ifpb.taskmanagement.model.repository.TaskRepository;

@SpringBootTest
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    TaskEntity taskEntity;

    @BeforeEach
    void setUpTask() {
        taskEntity = new TaskEntity();
        taskEntity.setTitle("titulo");
        taskEntity.setDescription("description");
        taskEntity.setCompleted(false);
    }

    @Test
    void taskShouldBeCreatedSuccessfully() {

        when(taskRepository.save(any())).thenReturn(taskEntity);

        TaskEntity returnedTask = taskService.createTask(taskEntity);
        assertNotNull(returnedTask);

        verify(taskRepository, times(1)).save(any());

    }

    @Test
    void taskShouldBeDeletedSuccessfully() {

        when(taskRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(taskRepository).deleteById(anyLong());

        taskService.deleteTask(1l);

        verify(taskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void allTasksShouldBeRetrievedSuccessfully() {

        List<TaskEntity> taskList = new ArrayList<TaskEntity>();

        for (int i = 0; i < 5; i++) {
            TaskEntity task = new TaskEntity();
            taskEntity.setTitle("titulo" + i);
            taskEntity.setDescription("description" + i);
            taskEntity.setCompleted(false);
            taskList.add(task);
        }

        when(taskRepository.findAll()).thenReturn(taskList);

        List<TaskEntity> returnedList = taskService.getAllTasks();

        assertNotNull(returnedList);
        assertEquals(5, returnedList.size());
        assertArrayEquals(taskList.toArray(), returnedList.toArray());

        verify(taskRepository, times(1)).findAll();

    }

    @Test
    void emptyListShouldBeReturnedSuccessfully() {

        when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        List<TaskEntity> returnedList = taskService.getAllTasks();

        assertNotNull(returnedList);
        assertTrue(returnedList.isEmpty());

        verify(taskRepository, times(1)).findAll();
        

    }

    @Test
    void testGetTaskById() {

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

        TaskEntity returnedTask = taskService.getTaskById(1L);

        assertNotNull(returnedTask);
        assertEquals(returnedTask, returnedTask);

        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    void taskShouldBeUpdatedSUccessfully() {

        TaskEntity oldTask = new TaskEntity(1l, "titulo1", false);
        TaskEntity newTask = new TaskEntity(1l, "titulo2", false);

        when(taskRepository.save(any(TaskEntity.class))).thenReturn(newTask);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

        TaskEntity returnedTask = taskService.updateTask(1l, oldTask);

        assertNotNull(returnedTask);
        assertEquals(newTask, returnedTask);

        verify(taskRepository, times(1)).save(any());
    }
}
