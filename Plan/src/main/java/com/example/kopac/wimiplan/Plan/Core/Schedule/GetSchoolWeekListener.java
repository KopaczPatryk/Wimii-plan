package com.example.kopac.wimiplan.Plan.Core.Schedule;

import com.example.kopac.wimiplan.Plan.Models.SchoolWeekSchedule;

public interface GetSchoolWeekListener {
    void OnSchoolWeekReceived(SchoolWeekSchedule timetable);
}
