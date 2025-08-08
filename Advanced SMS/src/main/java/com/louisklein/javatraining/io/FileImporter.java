package com.louisklein.javatraining.io;

import com.louisklein.javatraining.dao.StudentDAO;
import com.louisklein.javatraining.model.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileImporter {

    public static List<Student> importStudents(String filePath, StudentDAO studentDAO) {
        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("ID")) {
                    continue; // skip header/empty lines
                }

                String[] parts = line.split(",");

                if (parts.length != 4) {
                    System.err.println("Skipping bad line: " + line);
                    continue;
                }

                try {
                    String name = parts[0].trim();
                    int age = Integer.parseInt(parts[1].trim());
                    String email = parts[2].trim();
                    int grade = Integer.parseInt(parts[3].trim());

                    Student student = new Student(name, age, email, grade);
                    students.add(student);
                    studentDAO.addStudent(student);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping line due to parse error: " + line);
                }
            }

            System.out.println("Imported " + students.size() + " students from " + filePath);

        } catch (IOException e) {
            System.err.println("Error reading from file: " + filePath);
            e.printStackTrace();
        }
        return students;
    }
}
