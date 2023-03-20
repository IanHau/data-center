package com.ian.utils;

import com.ian.exception.ServiceException;
import com.migozi.conversion.Conversion;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author ianhau
 */
@Component
public class WorkbookUtils {
    @Autowired
    private Conversion conversion;

    /**
     * 读取单元格的值
     *
     * @param row          row
     * @param cellNum      cell number
     * @param clz          类型
     * @param defaultValue 默认值
     * @param <T>          读取的值类型
     * @return 读取的值
     */
    public <T> T getCellValue(XSSFRow row, int cellNum, Class<T> clz, T defaultValue) {
        return getCellValue(row.getCell(cellNum), clz, defaultValue);
    }

    /**
     * 读取单元格的值
     *
     * @param clz          类型
     * @param defaultValue 默认值
     * @param <T>          读取的值类型
     * @return 读取的值
     */
    @SuppressWarnings("unchecked")
    public <T> T getCellValue(XSSFCell cell, Class<T> clz, T defaultValue) {
        if (cell == null) {
            return defaultValue;
        }
        if (clz.equals(Date.class)) {
            return (T) cell.getDateCellValue();
        }

        switch (cell.getCellType()) {
            case _NONE:
            case FORMULA:
            case ERROR:
                return conversion.convertTo(cell.getRawValue(), clz);
            case BLANK:
                return defaultValue;
            case STRING:
                return conversion.convertTo(cell.getStringCellValue(), clz);
            case BOOLEAN:
                return conversion.convertTo(cell.getBooleanCellValue(), clz);
            case NUMERIC:
                if (clz.equals(String.class)) {
                    return (T) cell.getRawValue();
                }
                return conversion.convertTo(cell.getNumericCellValue(), clz);
        }
        return defaultValue;
    }

    /**
     * 设置单元格的值
     */
    public void setCell(XSSFRow row, int cellNum, CellType cellType, Object value, String format) {
        setCell(row.getCell(cellNum), cellType, value, format);
    }

    /**
     * 设置单元格的值
     */
    public void setCell(XSSFRow row, int cellNum, CellType cellType, Object value) {
        setCell(row.getCell(cellNum), cellType, value, null);
    }

    /**
     * 设置单元格的值
     */
    public void setCell(XSSFCell cell, CellType cellType, Object value) {
        setCell(cell, cellType, value, null);
    }

    /**
     * 设置单元格的值，值为String类型
     */
    public void setCell(XSSFCell cell, Object value) {
        setCell(cell, CellType.STRING, conversion.toString(value));
    }

    /**
     * 设置单元格的值
     */
    public void setCell(XSSFCell cell, CellType cellType, Object value, String format) {
        if (value == null) {
            cell.setCellValue((String) null);
        }

        switch (cellType) {
            case _NONE:
            case FORMULA:
            case ERROR:
                throw new ServiceException("500", "not supported cell type: " + cellType);
            case BLANK:
                cell.setCellValue("");
                return;
            case STRING:
                cell.setCellValue(conversion.convertTo(value, String.class, format));
                return;
            case BOOLEAN:
                cell.setCellValue(conversion.convertTo(value, Boolean.class, format));
                return;
            case NUMERIC:
                cell.setCellValue(conversion.convertTo(value, Double.class, format));
        }
    }

    /**
     * 设置边框样式
     */
    public void applyBorderStyle(CellStyle... styles) {
        if (styles == null || styles.length == 0) {
            return;
        }
        for (CellStyle style : styles) {
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
        }
    }

    /**
     * 设置对齐
     */
    public void applyAlignmentStyle(HorizontalAlignment horizontalAlignment, CellStyle... styles) {
        applyAlignmentStyle(horizontalAlignment, VerticalAlignment.CENTER, styles);
    }

    /**
     * 设置对齐
     */
    public void applyAlignmentStyle(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, CellStyle... styles) {
        if (styles == null || styles.length == 0) {
            return;
        }
        for (CellStyle style : styles) {
            style.setAlignment(horizontalAlignment);
            style.setVerticalAlignment(verticalAlignment);
        }
    }

    /**
     * 设置字体样式
     */
    public void applyFontStyle(Workbook workbook, int fontSize, CellStyle... styles) {
        applyFontStyle(workbook, fontSize, false, styles);
    }

    /**
     * 设置字体样式
     */
    public void applyFontStyle(Workbook workbook, int fontSize, boolean bold, CellStyle... styles) {
        applyFontStyle(workbook, fontSize, bold, 0, styles);
    }

    /**
     * 设置字体样式
     */
    public void applyFontStyle(Workbook workbook, int fontSize, int color, CellStyle... styles) {
        applyFontStyle(workbook, fontSize, false, color, styles);
    }

    /**
     * 设置字体样式
     */
    public void applyFontStyle(Workbook workbook, int fontSize, boolean bold, int color, CellStyle... styles) {
        if (styles == null || styles.length == 0) {
            return;
        }
        Font font = workbook.createFont();
        font.setBold(bold);
        font.setColor((short) color);
        font.setFontHeightInPoints((short) fontSize);
        for (CellStyle style : styles) {
            style.setFont(font);
        }
    }

    /**
     * 设置单元格样式
     */
    public void applyStyles(XSSFRow row, int columnNum, CellStyle[] styles) {
        for (int i = 0; i < columnNum; i++) {
            XSSFCell cell = row.getCell(i);

            if (cell != null) {
                cell.setCellStyle(styles[i]);
            }
        }
    }

    /**
     * 设置excel导出流
     */
    public void setExcelResponse(HttpServletResponse response, String excelName) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8"));
        try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建workbook
     */
    public XSSFWorkbook createWorkbook() {
        return new XSSFWorkbook();
    }

    /**
     * 创建sheet
     */
    public XSSFSheet createSheet(XSSFWorkbook workbook, String sheetName) {
        return !StringUtils.isEmpty(sheetName)
                ? workbook.createSheet(sheetName)
                : workbook.createSheet();
    }

    /**
     * 合并单元格
     */
    public void mergeCell(XSSFSheet sheet, int rowStart, int rowEnd, int columnStart, int columnEnd) {
        CellRangeAddress range = new CellRangeAddress(rowStart, rowEnd, columnStart, columnEnd);
        sheet.addMergedRegion(range);
    }

    /**
     * 合并单元格
     */
    public void mergeCellVertical(XSSFSheet sheet, int rowStart, int rowEnd, int column) {
        mergeCell(sheet, rowStart, rowEnd, column, column);
    }

    /**
     * 合并单元格
     */
    public void mergeCellHorizontal(XSSFSheet sheet, int row, int columnStart, int columnEnd) {
        mergeCell(sheet, row, row, columnStart, columnEnd);
    }

    /**
     * 将表格转换成List<Map<String,Object>
     * properties实体类的属性名
     * fclz实体类Class对象
     */
    public List<Map<String, Object>> sheetToList(XSSFSheet sheet, String[] properties, Class fclz) {
        int num = sheet.getLastRowNum();
        List<Map<String, Object>> strains = new ArrayList<>(num);
        XSSFRow header = sheet.getRow(0);
        int cellNumber = header.getPhysicalNumberOfCells();
        Class clz;
        Field[] declaredFields = fclz.getDeclaredFields();
        for (int i = 1; i <= num; i++) {
            Map<String, Object> map = new HashMap();
            XSSFRow row = sheet.getRow(i);
            for (int index = 0; index < cellNumber; index++) {
                for (Field declaredField : declaredFields) {
                    if (declaredField.getName().equals(properties[index])) {
                        //判断字段类型
                        Type genericType = declaredField.getGenericType();
                        //判断是否是可有泛型的类型，可获取泛型参数,例如List<User> Map
                        if (genericType instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            clz = ((Class) pt.getRawType());
                            Object value = getCellValue(row, index, clz, null);
                            map.put(properties[index], value);
                            //是否为不确定类型的属性，例如：T
                        } else {
                            clz = (Class) genericType;
                            Object value = getCellValue(row, index, clz, null);
                            map.put(properties[index], value);
                        }
                    }
                }
            }
            strains.add(map);
        }
        return strains;
    }


    public static void writeToResponse(HttpServletResponse response, XSSFWorkbook workbook) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(DateUtils.getDateStr(new Date()) + ".xlsx", "UTF-8"));

        workbook.write(response.getOutputStream());
        response.flushBuffer();
        response.getOutputStream().close();
    }
}
