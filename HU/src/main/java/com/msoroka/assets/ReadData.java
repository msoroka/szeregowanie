package com.msoroka.assets;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
    private File file;
    private BufferedReader bufferedReader = null;
    private String tempString;
    private List<String> result = new ArrayList<String>();

    public ReadData(String fileName) {
        this.file = new File(fileName);

        try {
            bufferedReader = new BufferedReader((new FileReader(this.file)));
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
