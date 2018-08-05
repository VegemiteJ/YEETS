package uni.evocomp.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Logger<T> {
    ArrayList<ArrayList<T>> data;

    public Logger() {
        data = new ArrayList<>();
    }

    public void addPair(T a, T b) {
        ArrayList<T> d = new ArrayList<>();
        d.add(a);
        d.add(b);
        data.add(d);
    }

    public void saveCSV(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            for (ArrayList<T> line : data) {
                /*for (T thing : line) {
                    writer.write(thing.toString());
                    writer.write(",");
                }*/
                writer.write(line.get(0) + "," + line.get(1));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
