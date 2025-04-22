package com.sheep.sheepfold.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务状态枚举
 * 对应数据库字段：status TINYINT (0-未开始, 1-已完成)
 */
public enum TaskStatusEnum {

    NOT_STARTED("未开始", 0),
    COMPLETED("已完成", 1);

    private final String text;
    private final int value;

    TaskStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有状态值（数字）
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     */
    public static TaskStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (TaskStatusEnum enumItem : TaskStatusEnum.values()) {
            if (enumItem.value == value) {
                return enumItem;
            }
        }
        return null;
    }

    /**
     * 检查状态值是否有效
     */
    public static boolean isValid(Integer value) {
        return getEnumByValue(value) != null;
    }

    // Getter
    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}