package edu.nlp.arabic;

import edu.nlp.arabic.analyzer.PolaritySentenceAnalyzer;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 *  Analyzer app entry point.
 *
 * @author Salah Abu Msameh
 */
public class ArabicSentenceAnalyzerApp {

    /**
     * main method.
     * @param args
     */
    public static void main(String[] args) {

        if(args.length == 0) {
            System.err.println();
            System.exit(0);
        }

        String lexiconFilePath = null;
        String sentencesFilePath = null;
        String resultFilePath = null;

        for(int i = 0; i < args.length; i++) {

            if("-l".equals(args[i])) {
                lexiconFilePath = args[++i];
            } else if("-s".equals(args[i])) {
                sentencesFilePath = args[++i];
            } else if("-r".equals(args[i])) {
                resultFilePath = args[++i];
            }
        }

        if(lexiconFilePath == null) {
            exitError("no lexicon file specified");
        }

        if(sentencesFilePath == null) {
            exitError("no sentences file specified");
        }

        if(resultFilePath == null) {
            exitError("no result file path specified");
        }

        validateFile(lexiconFilePath, true);
        validateFile(sentencesFilePath, true);
        validateFile(resultFilePath, false);

        //init analyzer
        infoMessage("preparing lexicon terms...");
        PolaritySentenceAnalyzer analyzer = initPolarityAnalyzer(lexiconFilePath);

        //load sentences file
        infoMessage("loading sentences file");
        analyzeSentences(analyzer, sentencesFilePath, resultFilePath);
    }

    /**
     * validate file.
     *
     * @param filePath
     * @param file
     */
    private static void validateFile(final String filePath, final boolean file) {

        try {
            Path path = Paths.get(filePath);

            if(!Files.exists(path)) {
                exitError("No such file or directory > " + filePath);
            }

            if(file && !Files.isRegularFile(path)) {
                exitError("file path is not a regular file > " + filePath);
            } else if(!file && !Files.isDirectory(path)) {
                exitError("file path is not a directory > " + filePath);
            }
        } catch (Exception ex) {
            exitError(ex.getMessage());
        }

    }

    /**
     * analyze given sentences file.
     *
     * @param analyzer          analyzer instance
     * @param sentencesFilePath sentences file path to be analyzed
     * @param resultFilePath    the file to write analyses into
     */
    private static void analyzeSentences(PolaritySentenceAnalyzer analyzer, String sentencesFilePath,
                                         String resultFilePath) {

        try {
            XSSFWorkbook sentencesWorkbook = new XSSFWorkbook(sentencesFilePath);
            XSSFSheet sheet = sentencesWorkbook.getSheetAt(0);

            AtomicReference<Integer> sentenceIndex = new AtomicReference<Integer>();
            AtomicReference<Integer> polarityIndex = new AtomicReference<Integer>();

            //loop against 1rt row
            XSSFRow headerRow = sheet.getRow(0);
            IntStream.range(0, headerRow.getPhysicalNumberOfCells())
                    .forEach(cellIndex -> {

                        String cellValue = headerRow.getCell(cellIndex).getStringCellValue();

                        if(cellValue.equalsIgnoreCase("Sentence")
                                || cellValue.equalsIgnoreCase("Review")
                                || cellValue.equalsIgnoreCase("Comment")) {
                            sentenceIndex.set(cellIndex);
                        } else if(cellValue.equalsIgnoreCase("polarity")) {
                            polarityIndex.set(cellIndex);
                        }
                    });

            XSSFWorkbook resultWorkbook = new XSSFWorkbook();
            XSSFSheet resultSheet = resultWorkbook.createSheet();
            addHeaderRow(resultSheet.createRow(0), resultWorkbook.createCellStyle(),
                    resultWorkbook.createFont());

            IntStream.range(1, sheet.getPhysicalNumberOfRows())
                    .forEach(rowIndex -> {

                        XSSFRow row = sheet.getRow(rowIndex);
                        String sentence = row.getCell(sentenceIndex.get()).getStringCellValue().trim();
                        String polarityValue = row.getCell(polarityIndex.get()).getStringCellValue().trim();

                        String polarity = analyzer.analyze(sentence);
                        XSSFRow resultRow = resultSheet.createRow(rowIndex);
                        resultRow.createCell(0).setCellValue(sentence);
                        resultRow.createCell(1).setCellValue(polarity);
                        resultRow.createCell(2).setCellValue(polarityValue);
                    });

            String resultFilename = "result-" + new Date().getTime() + ".xlsx";
            FileOutputStream fileOutputStream = new FileOutputStream(new File(resultFilePath, resultFilename));
            resultWorkbook.write(fileOutputStream);

            fileOutputStream.close();
            sentencesWorkbook.close();

        } catch (IOException e) {
            exitError(e.getMessage());
        }
    }

    /**
     *  @param row
     * @param style
     * @param font
     */
    private static void addHeaderRow(final XSSFRow row, CellStyle style, XSSFFont font) {

        font.setBold(true);
        font.setColor(IndexedColors.DARK_BLUE.index);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        XSSFCell cell0 = row.createCell(0);
        cell0.setCellValue("Sentence");
        cell0.setCellStyle(style);

        XSSFCell cell1 = row.createCell(1);
        cell1.setCellValue("System Polarity");
        cell1.setCellStyle(style);

        XSSFCell cell2 = row.createCell(2);
        cell2.setCellValue("Human Polarity");
        cell2.setCellStyle(style);
        row.setRowStyle(style);
    }

    /**
     * initialize polarity analyzer.
     *
     * @param lexiconFilePath file contains lexicon terms
     * @return
     */
    private static PolaritySentenceAnalyzer initPolarityAnalyzer(String lexiconFilePath) {

        try {
            XSSFWorkbook lexiconWorkbook = new XSSFWorkbook(lexiconFilePath);
            XSSFSheet sheet = lexiconWorkbook.getSheetAt(0);

            AtomicReference<Integer> termIndex = new AtomicReference<Integer>();
            AtomicReference<Integer> polarityIndex = new AtomicReference<Integer>();

            //loop against 1rt row
            XSSFRow headerRow = sheet.getRow(0);
            IntStream.range(0, headerRow.getPhysicalNumberOfCells())
                    .forEach(cellIndex -> {

                        String cellValue = headerRow.getCell(cellIndex).getStringCellValue();

                        if(cellValue.contains("term") || cellValue.contains("Term")) {
                            termIndex.set(cellIndex);
                        } else if(cellValue.equalsIgnoreCase("polarity")) {
                            polarityIndex.set(cellIndex);
                        }
                    });

            if(termIndex.get() == null) {
                exitError("Unable to find term column, please make sure the terms column header is Term");
            }

            if(polarityIndex.get() == null) {
                exitError("Unable to find polarity column, please make sure the polarity column header is Polarity");
            }

            Map<String, String> oneLexiconTerms = new HashMap<String, String>();
            Map<String, String> twoLexiconTerms = new HashMap<String, String>();
            Map<String, String> threeLexiconTerms = new HashMap<String, String>();
            Map<String, String> fourLexiconTerms = new HashMap<String, String>();

            //loop through actual content
            IntStream.range(1, sheet.getPhysicalNumberOfRows())
                    .forEach(rowIndex -> {

                        XSSFRow row = sheet.getRow(rowIndex);
                        String termValue = row.getCell(termIndex.get()).getStringCellValue().trim();

                        if(termValue.length() == 0) {
                            return;
                        }

                        String polarityValue = Optional.ofNullable(row.getCell(polarityIndex.get()))
                                .map(cell -> cell.getStringCellValue().trim())
                                .orElse("");

                        if(polarityValue.length() == 0) {
                            return;
                        }

                        switch(termValue.split("\\s+").length) {
                            case 1: {
                                oneLexiconTerms.put(termValue, polarityValue);
                                break;
                            }
                            case 2: {
                                twoLexiconTerms.put(termValue, polarityValue);
                                break;
                            }
                            case 3: {
                                threeLexiconTerms.put(termValue, polarityValue);
                                break;
                            }
                            case 4: {
                                fourLexiconTerms.put(termValue, polarityValue);
                                break;
                            }
                        }
                    });

            lexiconWorkbook.close();
            return new PolaritySentenceAnalyzer(oneLexiconTerms, twoLexiconTerms, threeLexiconTerms, fourLexiconTerms);

        } catch (IOException e) {
            exitError(e.getMessage());
        }

        return null;
    }

    /**
     *
     * @param msg
     */
    private static void infoMessage(String msg) {
        System.err.println("Info - " + msg);
    }

    /**
     *
     * @param errorMsg
     */
    private static void exitError(String errorMsg) {
        System.err.println("Error - " + errorMsg);
        System.exit(0);
    }
}
