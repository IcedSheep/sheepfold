package com.sheep.sheepfold.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务优先级枚举
 * 对应数据库字段：priority TINYINT (0-低, 1-中, 2-高, 3-紧急)
 */
public enum PriorityEnum {

    LOW("低", 0),
    MEDIUM("中", 1),
    HIGH("高", 2),
    URGENT("紧急", 3);

    private final String text;
    private final int value;

    PriorityEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有枚举值（数字）
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     */
    public static PriorityEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (PriorityEnum enumItem : PriorityEnum.values()) {
            if (enumItem.value == value) {
                return enumItem;
            }
        }
        return null;
    }

    /**
     * 检查值是否有效
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