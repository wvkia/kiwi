package com.wvkia.tools.kiwi.tools.readExcel;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ExcelReadUtils {


    /**
     * 读取excel为workbook
     * @param fileName
     * @return
     */
    public static Workbook getWorkBook(String fileName) {
        FileInputStream excelFile = null;
        try {
            excelFile = new FileInputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("文件不存在");
        }
        Workbook workbook = null;
        try {
             workbook = new XSSFWorkbook(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("解析excel出错");
        }
        return workbook;

    }
    /**
     * 读取全部数据
     * @param workbook
     * @param sheetName
     * @return
     */
    public static List<List<String>> readAllLines(Workbook workbook, String sheetName) {
        Sheet datatypeSheet = getSheet(workbook, sheetName);
        if (null == datatypeSheet) {
            return Lists.newArrayList();
        }
        int maxNum = getMaxColumn(datatypeSheet);
        Iterator<Row> iterator = datatypeSheet.iterator();

        List<List<String>> valueList = Lists.newArrayList();
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            List<String> columnValues = readLine(currentRow);
            fillValuesByMaxColumnNum(maxNum,columnValues);
            valueList.add(columnValues);
        }
        return valueList;
    }
    /**
     * 获取根据一个sheet名获取数据
     * @param workbook
     * @param sheetName
     * @return
     */
    public static Sheet getSheet(Workbook workbook, String sheetName) {
        Sheet datatypeSheet = null;

        int numerOfSheet = workbook.getNumberOfSheets();
        for (int i = 0; i < numerOfSheet; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (StringUtils.contains(sheet.getSheetName(), sheetName)) {
                datatypeSheet = sheet;
                break;
            }
        }
        return datatypeSheet;
    }

    /**
     * 获取一个sheet的最大列数
     * @param sheet
     * @return
     */
    private static int getMaxColumn(Sheet sheet) {
        int maxNum = 0;
        Iterator<Row> iterator = sheet.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            i++;
            Row currentRow = iterator.next();
            int num=currentRow.getLastCellNum();
            maxNum = Math.max(maxNum, num);
        }
        return maxNum;
    }


    /**
     * 根据最大列数进行填充
     * @param maxNum
     * @param list
     */
    private static void fillValuesByMaxColumnNum(int maxNum,List<String> list) {
        if (list.size() < maxNum) {
            int descr = maxNum - list.size();
            for (int i = 0; i < descr; i++) {
                list.add(null);
            }
        }
    }

    /**
     * 读取一行
     * @param currentRow
     * @return
     */
    public static List<String> readLine(Row currentRow) {
        List<String> strings = Lists.newArrayList();

        int num = currentRow.getLastCellNum();
        for (int i = 0; i < num; i++) {
            Cell currentCell = currentRow.getCell(i);
            if (null == currentCell) {
                strings.add(null);
                continue;
            }
            String value = readCellValue(currentCell);
            strings.add(value);
        }
        return strings;
    }

    public static String readCellValue(Cell cell) {
        String value = null;
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            double d=cell.getNumericCellValue();
            value = String.valueOf((int)d);
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            value = cell.getStringCellValue();
        }
        return value;
    }
}