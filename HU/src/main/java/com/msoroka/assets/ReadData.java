package com.msoroka.assets;

import java.io.*;
import java.util.*;

public class ReadData {
    private String tempString;
    private List<String> result = new ArrayList<>();

    public ReadData(String fileName) {
        File file = new File(fileName);

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader((new FileReader(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                assert bufferedReader != null;
                if ((tempString = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.result.add(tempString);
        }
    }

    public List<String> getResult() {
        return this.result;
    }
}