package com.taskmanager.app.rest;

import com.taskmanager.app.model.Task;
import com.taskmanager.app.service.api.TaskService;
import com.taskmanager.app.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for tasks
 */
@RestController
@RequestMapping("tasks")
public class TaskRestController {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRestController.class);

    @Autowired
    private TaskService taskService;

    /**
     * Add new task
     * @param task {@link Task}
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody Task task) {
        LOGGER.info("Add new task from user: {}", task.getUserId());
        try {
            taskService.add(task);
        } catch (ServiceException e) {
            LOGGER.error("Error added new task from user: {}", task.getUserId());
        }
    }

    /**
     * Stop tasks by user
     * @param taskId taskId
     * @param userId userId
     */
    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void stop(@RequestParam(name = "taskId") String taskId,
                     @RequestParam(name = "userId") String userId){
        LOGGER.info("Stop task with id: {} by user: {}", taskId, userId);
        try {
            taskService.stop(taskId, userId);
        } catch (ServiceException e) {
            LOGGER.error("Error stopped task with id: {} by user: {}", taskId, userId);
        }
    }

    /**
     * Remove task by user
     * @param taskId taskId
     * @param userId userId
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@RequestParam(name = "taskId") String taskId,
                       @RequestParam(name = "userId") String userId){
        LOGGER.info("Remove task with id: {} by user: {}", taskId, userId);
        try {
            taskService.remove(taskId, userId);
        } catch (ServiceException e) {
            LOGGER.error("Error removed task with id: {} by user: {}", taskId, userId);
        }
    }

    /**
     * Load list of task from page
     * @param page page number
     * @param count count tasks on page
     * @param userId userId
     * @return {@link Page} of {@link Task}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<Task> load(@RequestParam(name = "page") int page,
                           @RequestParam(name = "count") int count,
                           @RequestParam(name = "userId") String userId){
        Page<Task> loadedPage = taskService.load(page, count, userId);
        LOGGER.info("Load {} tasks from page: {} with count {} by user: {}", loadedPage.getSize(), page, count, userId);
        return loadedPage;
    }

}
