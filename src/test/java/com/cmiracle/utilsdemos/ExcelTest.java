package com.cmiracle.utilsdemos;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class ExcelTest {

    public static void main(String[] args) throws Exception {
        List<Row> rows = readXLSAndXLSX("/Users/a20170509002/Documents/工作资料/伊贝诗/同步用户1.xlsx");
        for(Row row : rows){
            Cell cell = row.getCell(12);
            cell.setCellType(CellType.STRING);
            String birthday = cell.getStringCellValue();
            Date date = new Date(Long.valueOf(birthday));
            System.out.println(date.getTime());
        }
    }

    /**
     * 从OSS获取文件
     * @param fileUrl
     * @return
     * @throws IOException
     */
    private static List<Row> readXLSAndXLSX(String fileUrl) throws Exception {
        List<Row> rowList = Lists.newArrayList();
        Workbook wb = null;
        InputStream is = null;
        try {
            is = new FileInputStream(fileUrl);
            wb = WorkbookFactory.create(is);
        } finally {
            if (is != null) {
                is.close();
            }
            if (wb != null) {
                wb.close();
            }
        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            int rowNum = 0;
            if (hasExcelTitle()) {
                rowNum = 1;
            }
            for (int i = rowNum; i <= sheet.getLastRowNum(); i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if (row != null) {
                    rowList.add(row);
                }
            }
        } else {
        }
        return rowList;
    }

    //默认title占一行
    public static boolean hasExcelTitle() {
        return true;
    }

    private static String getValue(Cell cell){
        if(cell == null){
            return null;
        }else{
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue().trim();
        }
    }
}
