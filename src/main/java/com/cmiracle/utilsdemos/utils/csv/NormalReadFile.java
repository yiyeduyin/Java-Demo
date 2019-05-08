package com.cmiracle.utilsdemos.utils.csv;

import java.util.List;

public class NormalReadFile extends ReadFile<String> {

    @Override
    public List<String> transformTo(List<String[]> list) {
        System.out.println("NormalReadFile");
        return null;
    }

}
