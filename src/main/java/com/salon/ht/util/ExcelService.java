package com.salon.ht.util;

import com.salon.ht.exception.ImportUserException;
import com.salon.ht.mapper.csv.UserCSV;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelService {
    private final static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public List<UserCSV> readData(InputStream inputStream, List<String> headers, String sheetName) {
        try {
            List<UserCSV> userCSVS = new ArrayList<>();

            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheet(sheetName);

            Row rowHeader = sheet.getRow(0);

            List<String> newHeaders = headers.stream().map(s -> s.trim().toLowerCase()).collect(Collectors.toList());

            for (int i = 0; i < headers.size(); i++) {
                Cell cell = rowHeader.getCell(i);
                String data = cell.getStringCellValue().trim().toLowerCase();
                if (!newHeaders.contains(data)) {
                    throw new ImportUserException("Tên cột '" + data + "' sai với quy định");
                }
            }

            Row row1 = sheet.getRow(1);
            if (row1 == null) {
                throw new ImportUserException("Không có dữ liệu để import. Hãy nhập dữ liệu để thực hiện import");
            } else if (row1.getCell(0) == null) {
                throw new ImportUserException("Dữ liệu truyền lên sai. Hãy xem lại dữ liệu trong file tải lên");
            } else {
                Iterator<Row> rows = sheet.rowIterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();

                    // skip header
                    if (rowNumber == 0) {
                        rowNumber++;
                        continue;
                    }

                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    UserCSV userCSV = new UserCSV();
                    int cellIdx = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();

                        switch (cellIdx) {
                            case 0:
                                String username = currentCell.getStringCellValue();
                                userCSV.setUsername(username);
                                break;
                            case 1:
                                String name = currentCell.getStringCellValue();
                                userCSV.setName(name);
                                break;
                            case 2:
                                String email = currentCell.getStringCellValue();
                                userCSV.setEmail(email);
                                break;
                            case 3:
                                String mobile = currentCell.getStringCellValue();
                                userCSV.setMobile(mobile);
                                break;
                            case 4:
                                String photo = currentCell.getStringCellValue();
                                userCSV.setPhoto(photo);
                                break;
                            case 5:
                                String department = currentCell.getStringCellValue();
                                userCSV.setDepartmentPath(department);
                                break;
                            case 6:
                                String password = currentCell.getStringCellValue();
                                userCSV.setPassword(password);
                                break;
                            case 7:
                                String jobTitle = currentCell.getStringCellValue();
                                userCSV.setPosition(jobTitle);
                                break;
                            case 8:
                                String position = currentCell.getStringCellValue();
                                userCSV.setJobTile(position);
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    userCSVS.add(userCSV);
                }
            }
            workbook.close();
            return userCSVS;
        } catch (Exception e) {
            throw new ImportUserException("Xảy ra lỗi khi đọc dữ liệu file đẩy lên: " + e.getMessage());
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelService.class);
}
