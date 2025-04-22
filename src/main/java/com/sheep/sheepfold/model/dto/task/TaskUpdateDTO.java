package com.sheep.sheepfold.model.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaskUpdateDTO implements Serializable {


    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务描述
     */
    private String description;


    /**
     * 优先级：0-低, 1-中, 2-高, 3-紧急
     */
    private Integer priority;

    /**
     * 开始时间(精确到时分)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date beginTime;

}
