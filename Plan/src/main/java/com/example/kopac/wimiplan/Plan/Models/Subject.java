package com.example.kopac.wimiplan.Plan.Models;

import java.io.Serializable;

public class Subject implements Serializable {
    public String HourStart;
    public String SubjectName;
    public String Teacher;
    public String Type;
    public String Room;

    public Subject(String subjectName) {
        HourStart = "8";
        SubjectName = subjectName;
    }

    public Subject() {
    }

    public void SetTestData() {
        HourStart = "8";
        SubjectName = "Sztuczna inteligencja";
        Teacher = "Woldan piotr";
        Type = "LAB";
        Room = "s514";
    }
}
