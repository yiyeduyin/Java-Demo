package com.cmiracle.utilsdemos.utils.csv;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public abstract class ReadFile<T> {

    public final List<String[]> readFileData(String fileName){
        List<String[]> list;
        CSVReader csvReader = null;
        InputStreamReader inputStreamReader = null;
        InputStream csv = null;
        try {
            csv = new FileInputStream(fileName);
            inputStreamReader = new InputStreamReader(csv, "UTF-8");
            csvReader = new CSVReader(inputStreamReader);
            list = csvReader.readAll();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (csvReader != null) {
                    csvReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (csv != null) {
                    csv.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public abstract List<T> transformTo(List<String[]> list);
}
