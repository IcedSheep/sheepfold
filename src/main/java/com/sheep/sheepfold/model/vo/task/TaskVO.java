package com.sheep.sheepfold.model.vo.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class TaskVO {
    private Long taskId;
    private Long userId;
    private String title;
    private String description;
    private Integer status;
    private Integer priority;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;
}