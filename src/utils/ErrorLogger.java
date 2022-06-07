package src.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ErrorLogger implements Logger {

    private final String prefixPath;
    private final String filename;
    private final Path completePath;


    public ErrorLogger(String prefixPath, String filename) {
        this.prefixPath = prefixPath;
        this.filename = filename;
        this.completePath = Paths.get(this.prefixPath, this.filename);
        this.checkPath();
    }

    private BufferedWriter openLogger() {
        File file = new File(this.completePath.normalize().toAbsolutePath().toString());
        try {
            if(!file.exists()) {
                boolean created = file.createNewFile();
                if(!created) {
                    throw new IOException("Logging file not created!");
                }
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            return new BufferedWriter(fileWriter);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    private void checkPath() {
        File file = new File(this.prefixPath);
        try {
            if(!file.exists()) {
                boolean created = file.mkdirs();
                if(!created) {
                    throw new IOException("Impossible to create output path!");
                }
            }
        } catch(IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void write(String text) {
        BufferedWriter writer = openLogger();
        if(writer == null) {
            System.out.println("Something went wrong!");
            return;
        }
        try {
            writer.write(text);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
