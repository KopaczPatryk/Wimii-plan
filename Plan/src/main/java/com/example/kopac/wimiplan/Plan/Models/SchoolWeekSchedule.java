package com.example.kopac.wimiplan.Plan.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SchoolWeekSchedule implements Serializable {
    public List<SchoolDaySchedule> Schedules;

    public SchoolWeekSchedule () {
        Schedules = new ArrayList<>();
    }
}
