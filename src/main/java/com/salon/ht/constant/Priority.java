package com.salon.ht.constant;

import com.salon.ht.exception.BadRequestException;

import java.util.Arrays;

public enum Priority {
    /**
     * Cuộc họp thường
     */
    NORMAL(0),
    /**
     * Cuộc họp quan trọng
     */
    IMPORTANT(1);

    private final Integer value;

    Priority (Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static Priority getPriority(Integer priorityNum) {
        Priority priority = Arrays.stream(Priority.values()).filter(p -> p.getValue().equals(priorityNum))
                .findFirst().orElse(null);
        if (priority == null) {
            throw new BadRequestException("Mức độ của phòng họp sai giá trị. Vui lòng kiểm tra lại!");
        }
        return priority;
    }
}
