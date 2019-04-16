import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
    private String fileName;
    private File file;
    private BufferedReader bufferedReader = null;
    private String tempString;
    private List<String> result = new ArrayList<String>();

    public ReadData(String fileName) {
        this.fileName = fileName;
    }

    public void readFile() throws IOException {
        this.file = new File(this.fileName);

        try {
            bufferedReader = new BufferedReader((new FileReader(this.file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while ((tempString = bufferedReader.readLine()) != null) {
            result.add(tempString);
        }
    }

    public List<String> getResult() {
        return result;
    }
}