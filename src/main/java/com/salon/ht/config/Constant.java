package com.salon.ht.config;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final String PATTERN_DATE_FORMAT = "dd/MM/yyyy HH:mm";

    //email
    public static final String SUBJECT_EMAIL = "HTSalon thông báo";
    public static final String CONTENT_EMAIL_APPROVE = "Kinh gửi %s ! \n Lịch cắt tóc của bạn vào %s đến %s đã được xác nhận. Vui lòng đến đúng giờ.";
    public static final String CONTENT_EMAIL_REJECT = "Kinh gửi %s ! \n Lịch cắt tóc của bạn vào %s đến %s đã bị từ chối. Chúng tôi xin lỗi vì sự cố này.";

    public enum SERVICE_MAP {
        BOOKING,
        COMBO

    }

}
