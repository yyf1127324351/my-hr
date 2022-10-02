package com.myhr.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * @Description Excel导出工具类
 */
@Slf4j
public class ExportUtils {


    public static void buildCell(XSSFRow row, String title, XSSFWorkbook workbook) {
        List<String> list = Arrays.asList(title.split(","));
        CellStyle cellStyle = centerStyle(workbook, true);
        for (int i = 0; i < list.size(); i++) {
            buildCellSingle(row, i, list.get(i), cellStyle);
        }
    }

    public static int buildCellNew(XSSFRow row, int cellNum, String value, CellStyle cellStyle) {
        if (row != null) {
            XSSFCell cell = row.createCell(cellNum);
            cell.setCellValue(new XSSFRichTextString(getNotNullStr(value)));
            cell.setCellStyle(cellStyle);
            int newCellNum = cellNum +1;
            return newCellNum;
        }else {
            return cellNum;
        }
    }

    /**
     * 创建excel表格的单个列
     */
    public static void buildCellSingle(XSSFRow row, int cellNum, String value, CellStyle cellStyle) {
        if (row != null) {
            XSSFCell cell = row.getCell(cellNum);
            if(cell == null) {
                cell = row.createCell(cellNum);
            }
            cell.setCellValue(new XSSFRichTextString(getNotNullStr(value)));
            cell.setCellStyle(cellStyle);
        }
    }

    public static String getNotNullStr(String value) {
        return (value != null) ? value : "";
    }

    /**
     * @param workbook
     * @param bold 是否黑体
    * */
    public static CellStyle centerStyle(Workbook workbook, boolean bold) {
        return centerStyle(workbook, "Arial", (short) 11, bold);
    }

    public static CellStyle centerStyle(Workbook workbook, String fontName, short fontSize, boolean bold) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        Font font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        font.setBold(bold);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static void exportExcel(XSSFWorkbook workbook, String fileName, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.reset();
            WebUtils.downloadExcelHeader(request, response, fileName + ".xlsx");
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            workbook.close();
            os.close();
        } catch (Exception e) {
            log.error("exportExcelException:" + e.getMessage(), e);
        }
    }




    public static void exportExcelFile(XSSFWorkbook workbook, String partFileName, HttpServletRequest request, HttpServletResponse response) {
        WebApplicationContext webApplicationContext = (WebApplicationContext) SpringUtils.getApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        String basePath = servletContext.getRealPath("");
        String path = basePath;
        String fileName = partFileName + ".xlsx";
        // 临时文件
        FileOutputStream fileOut = null;

        try {
            fileOut = new FileOutputStream(path + "/" + fileName);
            workbook.write(fileOut);
            InputStream xlsStream = new FileInputStream(path + "/" + fileName);
            File file = new File(path + "/" + fileName);
            if (file.exists()) {
                file.delete();
            }
            BufferedInputStream bufferedInputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                response.reset();
                WebUtils.downloadExcelHeader(request, response, partFileName + ".xlsx");
                IOUtils.copy(xlsStream, response.getOutputStream());
            } catch (IOException e) {
                log.error("导出文件【" + partFileName + "】报错：" + e.getMessage(), e);
            } finally {
                try {
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            log.error("exportXslFileException:" + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(fileOut);
        }

    }
}
