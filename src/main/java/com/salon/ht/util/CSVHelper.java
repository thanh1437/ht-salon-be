package com.salon.ht.util;

import org.springframework.web.multipart.MultipartFile;

public class CSVHelper {
    public static final String CSV_TYPE = "text/csv";


    public static boolean isCSVFormat(MultipartFile file) {
        return CSV_TYPE.equals(file.getContentType());
    }
}
