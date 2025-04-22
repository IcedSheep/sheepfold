package com.sheep.sheepfold.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sheep.sheepfold.common.ApiResponse;
import com.sheep.sheepfold.common.DeleteRequest;
import com.sheep.sheepfold.common.ErrorCode;
import com.sheep.sheepfold.common.ResultUtils;
import com.sheep.sheepfold.exception.BusinessException;
import com.sheep.sheepfold.model.dto.task.TaskAddDTO;
import com.sheep.sheepfold.model.dto.task.TaskQueryByDateDTO;
import com.sheep.sheepfold.model.User;
import com.sheep.sheepfold.model.dto.task.TaskQueryDTO;
import com.sheep.sheepfold.model.dto.task.TaskUpdateDTO;
import com.sheep.sheepfold.model.entity.Task;
import com.sheep.sheepfold.model.vo.task.TaskVO;
import com.sheep.sheepfold.service.TaskService;
import com.sheep.sheepfold.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static com.sheep.sheepfold.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskService taskService;
    @Resource
    private UserService userService;

    /**
     * 新增代办任务
     * @param addTaskDTO
     * @param request
     * @return
     */
    @PostMapping("/add")
    public ApiResponse<Long> addTask(@RequestBody TaskAddDTO addTaskDTO, HttpServletRequest request) {
        if (addTaskDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long taskId = taskService.addTask(addTaskDTO,loginUser.getId());
        return ResultUtils.success(taskId);
    }

    /**
     * 查询某用户某天的任务
     * @param queryDTO
     * @param request
     * @return
     */
    @PostMapping("/queryByDate")
    public ApiResponse<List<TaskVO>> queryTasksByDate(@RequestBody TaskQueryByDateDTO queryDTO, HttpServletRequest request) {
        if (queryDTO == null  || queryDTO.getQueryDate() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Date queryDate = queryDTO.getQueryDate();
        User loginUser = userService.getLoginUser(request);
        List<TaskVO> tasks = taskService.queryTasksByDate(queryDate,loginUser.getId());
        return ResultUtils.success(tasks);
    }

    /**
     * 更新代办事项
     * @param updateDTO
     * @return
     */
    @PostMapping("/update")
    public ApiResponse<Boolean> updateTask(@RequestBody TaskUpdateDTO updateDTO) {
        boolean success = taskService.updateTask(updateDTO);
        return ResultUtils.success(success);
    }

    /**
     * 删除代办事项
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    public ApiResponse<Boolean> deleteTask(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long taskId = deleteRequest.getId();
        return ResultUtils.success(taskService.removeById(taskId));
    }

    /**
     * 查询某年某月的任务列表
     * @param queryDTO
     * @param request
     * @return
     */
    @PostMapping("/by-month")
    public ApiResponse<List<TaskVO>> getTasksByYearOrMonth(@RequestBody TaskQueryDTO queryDTO, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(taskService.queryUserTasksByMonth(queryDTO,loginUser.getId()));
    }

    @GetMapping("/getTaskById")
    public ApiResponse<TaskVO> getTaskById(Long taskId) {
        if (taskId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"taskId不能为空");
        }
        LambdaQueryWrapper<Task> lambdaQueryWrapper = Wrappers.lambdaQuery(Task.class).eq(Task::getTaskId, taskId);
        Task task = taskService.getOne(lambdaQueryWrapper);
        if (task == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TaskVO taskVO = new TaskVO();
        BeanUtils.copyProperties(task,taskVO);
        return ResultUtils.success(taskVO);
    }

}
