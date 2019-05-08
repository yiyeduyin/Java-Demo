package com.cmiracle.utilsdemos.utils.csv;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReadFileExample {

    public static void main(String[] args) {
        ReadFile<String> normalReadFile = new NormalReadFile();
        List<String[]> data = normalReadFile.readFileData("/Users/a20170509002/Downloads/data/gooben/pi_gooben_category.csv");

        String outString = "%s,%s,%s,%s,%s";
        boolean firstRow = true;
        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            if (firstRow) {
                firstRow = false;
            } else {
                try {
                    String userId = row[1];
                } catch (Exception e) {
                    System.out.println("error::::" + e.getMessage());
                }
            }
        }
        System.out.println("done");
    }
}
