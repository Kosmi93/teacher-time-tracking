package org.example;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.apache.poi.ss.usermodel.CellType.*;

public class Main {
    private static ArrayList<Integer> dataList = new ArrayList<>();
    private static final String fileLocation ="D:\\disk\\java\\uchet-vremeni-teahcers\\untitled\\src\\main\\resources\\1\\";
    public static void main(String[] args) throws IOException {
        ArrayList<Integer> newListToWatch = new ArrayList<>();
        File folder = new File(fileLocation);

        File[] files = folder.listFiles();
        assert files != null;
        //создаем нулевой масив по количеству файлов
        // для хранения часов, номер файла==дню
        for (File f: files){
            newListToWatch.add(0);
            dataList.add(0);
        }


        Map<TeachingUnit,List<Integer>> teachers = new HashMap<>();
        for (File file : files) {
            if (file.isFile()) {
                if(file.getName().contains("xlsx"))//исправить добавление
                {
                    Map<TeachingUnit,Integer> tempMap = new HashMap<>();
                    tempMap.putAll(toMat(file.getName()));
                    //tempMap содержит список препод+дисц+группа и сумму часов за день?
                    // нужно пробежаться по полученному Мапу
                    // и добавить препода в основной или добавить часов преподу если он есть
                    int deyNumber =Integer.parseInt(file.getName().replaceAll("\\D",""));
                    for (TeachingUnit t: tempMap.keySet()){
                        if (teachers.containsKey(t)){// если препод есть добавим ему часы в нужный день
                            List<Integer> tempListWatch= teachers.get(t);
                            tempListWatch.set(deyNumber-1,tempMap.get(t));
                            teachers.put(t,tempListWatch);
                        } else { //если препода нет добавим его и установим ему часы за день
                            List<Integer> tempListWatch= (List<Integer>) newListToWatch.clone();
                            tempListWatch.set(deyNumber-1,tempMap.get(t));
                            teachers.put(t,tempListWatch);
                        }
                    }
                }
            }
        }
        saveBook(teachers);
        System.out.println("Готово");
    }

    private static void saveBook(Map<TeachingUnit,List<Integer>> map) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Persons");
        sheet.setColumnWidth(0, 800);
        sheet.setColumnWidth(1, 800);



        // устанавливаем стиль, хз зачем
        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Times New Roma");
        font.setFontHeightInPoints((short) 13);
        headerStyle.setFont(font);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        Row headerTable = sheet.createRow(0);
        Cell headerCellTable = headerTable.createCell(0);
        headerCellTable.setCellValue("ФИО");
        headerCellTable.setCellStyle(headerStyle);
        headerCellTable = headerTable.createCell(1);
        headerCellTable.setCellValue("Дисциплина");
        headerCellTable.setCellStyle(headerStyle);
        headerCellTable = headerTable.createCell(2);
        headerCellTable.setCellValue("Группа");
        headerCellTable.setCellStyle(headerStyle);
        int numberCell = 3;
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm"));
        cellStyle.setRotation((short) 90);
        for(Integer day:dataList){
            headerCellTable = headerTable.createCell(numberCell);
            headerCellTable.setCellValue(day);
            headerCellTable.setCellStyle(cellStyle);
            numberCell++;

        }
        headerCellTable = headerTable.createCell(numberCell);
        headerCellTable.setCellValue("Итого");
        headerCellTable.setCellStyle(headerStyle);

        // пробегаем по всему мапу и записываем его в книгу
        int i = 1;

        for(TeachingUnit teachings:map.keySet()){
            sheet.autoSizeColumn(i);
            Row header = sheet.createRow(i);
            Cell headerCell = header.createCell(0);
            headerCell.setCellValue(teachings.getTeacher());
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue(teachings.getDiscipline());
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(2);
            headerCell.setCellValue(teachings.getGroups());
            headerCell.setCellStyle(headerStyle);
            int j = 3;

            for (Integer integer: map.get(teachings)){
                headerCell = header.createCell(j);
                if (integer>0){
                    headerCell.setCellValue(integer);
                }
                headerCell.setCellStyle(headerStyle);
                j++;
            }

           i++;
        }
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }


    private static Map<TeachingUnit,Integer> toMat(String fileName) throws IOException{

       try (FileInputStream fileXsls = new FileInputStream(new File(fileLocation+"\\"+fileName)))
       {
        Workbook workbook = new XSSFWorkbook(fileXsls);
        Sheet sheet = workbook.getSheetAt(0); // берем первый лист
        Map<TeachingUnit,Integer> teachers = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            List<String> str=new ArrayList<>();
            if(i>1|| i==0)
                for (Cell cell : row) {
                    if (cell.getCellType()==STRING)
                        str.add(cell.getRichStringCellValue().getString());
                    if (cell.getCellType()==NUMERIC)
                        str.add("" +Math.round(cell.getNumericCellValue()));
                }
            if(i==0){
                dataList.set(Integer.parseInt(fileName.replaceAll("\\D",""))-1, Integer.parseInt(str.get(4)));
            }
            if(str.size()>0 && i>1){
                teachers.put(new TeachingUnit(str.get(0),str.get(1),str.get(2)),Integer.parseInt(str.get(3)));
            }

            i++;
        }
        return  teachers;
       }
    }
}