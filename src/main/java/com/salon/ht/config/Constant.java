package com.salon.ht.config;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final String PATTERN_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String PATTERN_TIME_FORMAT = "HH:mm";

    //email
    public static final String SUBJECT_EMAIL = "HTSalon thông báo";
    public static final String CONTENT_EMAIL_APPROVE = "Kinh gửi %s ! \n Lịch cắt tóc của bạn vào %s đến %s đã được xác nhận. Vui lòng đến đúng giờ.";
    public static final String CONTENT_EMAIL_REJECT = "Kinh gửi %s ! \n Lịch cắt tóc của bạn vào %s đến %s đã bị từ chối. Chúng tôi xin lỗi vì sự cố này.";

    public enum SERVICE_MAP {
        BOOKING_COMBO,
        BOOKING,
        COMBO
    }

    public enum ROLES {
        ROLE_USER,
        ROLE_ADMIN,
        ROLE_EMPLOYEE
    }

}
