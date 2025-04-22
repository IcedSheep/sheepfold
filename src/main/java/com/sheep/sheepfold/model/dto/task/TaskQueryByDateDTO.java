package com.sheep.sheepfold.model.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaskQueryByDateDTO implements Serializable {


    private static final long serialVersionUID = 2839142003301497907L;

    /**
     * 要查询的日期，如2025-04-21
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date queryDate;

}
