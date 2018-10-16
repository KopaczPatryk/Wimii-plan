package com.example.kopac.wimiplan.Plan.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SchoolDaySchedule implements Serializable {
    //public String DayOfWeek;
    public List<Subject> Subjects;

    public SchoolDaySchedule () {
        Subjects = new ArrayList<>();
    }

    public void SetTestData () {
        //DayOfWeek = "Pon";
        Subject s = new Subject();
        s.SetTestData();
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
    }
}
