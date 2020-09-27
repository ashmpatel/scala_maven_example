package com.ash.example.service;

import com.jsoniter.JsonIterator;
import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.any.Any;
import com.jsoniter.spi.JsonException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetJsonFiles {

    public void listFiles(String startDir) {
        File dir = new File(startDir);
        File[] files = dir.listFiles();

        if (files != null && files.length > 0) {
            for (File file : files) {
                // Check if the file is a directory
                if (file.isDirectory()) {
                    // We will not print the directory name, just use it as a new
                    // starting point to list files from
                    listFiles(file.getAbsolutePath());
                } else {
                    // We can use .length() to get the file size
                    System.out.println(file.getName() + " (size in bytes: " + file.length()+")");
                }
            }
        }
    }

    public Set<File> listFiles2(String startDir) {
        Set<File> result = Collections.EMPTY_SET;

        File f = new File(startDir);
        if (f.exists() && f.isDirectory()) {
            // close stream automatically
            try (Stream<File> paths = Arrays.stream(new File(startDir).listFiles())) {
                result = paths.filter(File::isFile).collect(Collectors.toSet());
            }
        }
        return result;
    }


    public String filterFileForBatchId(String name, long batchId) {
        String content = "";
        long batchIdFound = 0;

        try {
            // filter for json files only
            if (name.endsWith(".json")) {
                // read the file
                content = new String(Files.readAllBytes(Paths.get(name)));

                // deserialize it to check its proper json
                Any any = null;
                boolean validJson = true;
                try {
                    any = JsonIterator.deserialize(content); // deserialize returns "Any", actual parsing is done lazily
                } catch (JsonException ex) {
                    validJson = false;
                }

                // if it is then
                if (validJson) {
                    batchIdFound = any.get("batchId").toLong();
                    // if the batchId in the JSON is not the batchId we are looking for then set result to null
                    if (batchIdFound != batchId) content = null;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public List<String> filterForBatchId(Stream<File> result, long batchId) {
        List<String> filesForBatchId= new ArrayList<>();

        result.forEach((File name) -> {
            // check if the file has the batchIs we are looking for, if so get the file contents
            String fileContents = filterFileForBatchId(name.getAbsolutePath(), batchId);
            if (fileContents != null && fileContents.length()>0) filesForBatchId.add(fileContents);

        });

        return filesForBatchId;
    }

    private String formatDateToYYYYMMDD() {
         return LocalDateTime.now().format( DateTimeFormatter.BASIC_ISO_DATE ) ;
    }

    public static void main(String[] args) {
        String startDir;

        GetJsonFiles test = new GetJsonFiles();

        /*
        URL resource = test.getClass().getClassLoader().getResource("test.txt");
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
           startDir = resource.getPath();
        }
        test.listFiles(startDir);
    }
    */
        Set<File> result = test.listFiles2("/home/ash9");
        List<String> files= test.filterForBatchId(result.stream(), 10);
        System.out.println(files.toString());
        System.out.println(test.formatDateToYYYYMMDD());

    }

}
