package com.cmiracle.utilsdemos.utils.csv;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteFile {

    private static CSVWriter csvWriter;

    static {
        try {
            csvWriter = new CSVWriter(new FileWriter("/Users/a20170509002/Downloads/ttt.csv"));
            String[] contents = {"entCode"};
            csvWriter.writeNext(contents);
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<String[]> contents = new ArrayList<>();
        String[] content1 = {"ddd"};
        String[] content2 = {"ggg"};
        contents.add(content1);
        contents.add(content2);
        writeCsv(contents);
    }



    public static void writeCsv(List<String[]> contents) {
        try {
            csvWriter = new CSVWriter(new FileWriter("/Users/a20170509002/Downloads/ttt.csv", true));
            contents.forEach(strings -> {
                csvWriter.writeNext(strings);
            });
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
