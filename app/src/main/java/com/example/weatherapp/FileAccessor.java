package com.example.weatherapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileAccessor {

    Context context;

    public FileAccessor(Context context) {
        this.context = context;
    }

    public List<String> readFile() {

        List<String> citiesList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = context.openFileInput("Cities.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String lines;
            while((lines = bufferedReader.readLine()) != null) {
                citiesList.add(lines);
            }
            System.out.println(citiesList);
            return citiesList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveCity(String text) {

        List<String> citiesList = readFile();
        citiesList.add(text);

        try {
            FileOutputStream fileOutputStream = context.openFileOutput("Cities.txt", Context.MODE_PRIVATE);
            for(String string : citiesList) {
                fileOutputStream.write((string + "\n").getBytes());
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDefaultFile(String text) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("Cities.txt", Context.MODE_PRIVATE);
            fileOutputStream.write((text + "\n").getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void overwriteFile(List<String> places) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("Cities.txt", Context.MODE_PRIVATE);
            for(String string : places) {
                fileOutputStream.write((string + "\n").getBytes());
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
