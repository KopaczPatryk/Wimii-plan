package com.example.kopac.wimiplan.Plan.Groups;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.kopac.wimiplan.Plan.LinkHelper;
import com.example.kopac.wimiplan.Plan.Models.Group;
import com.example.kopac.wimiplan.Plan.Models.SchoolDaySchedule;
import com.example.kopac.wimiplan.Plan.Models.SchoolWeekSchedule;
import com.example.kopac.wimiplan.Plan.Models.Subject;
import com.example.kopac.wimiplan.Plan.R;
import com.example.kopac.wimiplan.Plan.RecyclerViewClickListener;
import com.example.kopac.wimiplan.Plan.Term;
import com.example.kopac.wimiplan.Plan.Timetable.TimetableActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupsActivity extends AppCompatActivity implements GetGroupsListener, GetSchoolWeekListener, RecyclerViewClickListener {
    public static final String HYPERLINK_SUFFIX_BUNDLE = "hyperlinksuffix";
    //public static final String ARG_STATIONARY = "studiumtype";
    private RecyclerView GroupsRecyclerView;
    private List<Group> RetrievedGroups;
    private boolean IsStationary = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        GroupsRecyclerView = findViewById(R.id.groups_recycler);
        Term term = (Term)getIntent().getSerializableExtra(HYPERLINK_SUFFIX_BUNDLE);
        IsStationary = term.IsStationary;
        String hyperlink = LinkHelper.DOMAIN.concat(term.HyperLink);
        new GetGroups(this).execute(hyperlink);
    }

    @Override
    public void OnGroupsGet(List<Group> groups) {
        Collections.sort(groups, new Comparator<Group>() {
            @Override
            public int compare(Group o1, Group o2) {
                return o1.GroupName.compareToIgnoreCase(o2.GroupName);
            }
        });
        List<Group> processedGroups = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++)
        {
            if (!groups.get(i).GroupName.isEmpty())
            {
                processedGroups.add(groups.get(i));
            }
        }
        RetrievedGroups = processedGroups;
        GroupsRecyclerView.setAdapter(new GroupAdapter(processedGroups, this));
    }

    @Override
    public void OnRecyclerItemClick(View view, int position) {
        Group g = RetrievedGroups.get(position);
        String fullLink;
        if (IsStationary)
        {
            fullLink = LinkHelper.STATIONARY_TTS + "/" + RetrievedGroups.get(position).Hyperlink;
        }
        else {
            fullLink = LinkHelper.NONSTATIONARY_TTS + "/" + RetrievedGroups.get(position).Hyperlink;
        }
        new GetSchoolWeekSchedule(this).execute(fullLink);
//        Toast.makeText(this, RetrievedGroups.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnSchoolWeekReceived(SchoolWeekSchedule timetable) {
        Intent intent = new Intent(this, TimetableActivity.class);
        intent.putExtra(TimetableActivity.ARG_TIMETABLE, timetable);
        startActivity(intent);

    }

    public static class GetGroups extends AsyncTask<String, Void, List<Group>> {
        private GetGroupsListener listener;
        public GetGroups (GetGroupsListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Group> doInBackground(String... hyperlinks) {
            Document document = null;
            try {
                document = Jsoup.connect(hyperlinks[0]).timeout(30000).validateTLSCertificates(false).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert document != null;
            Elements elements =  document.select("td > a");
            List<Group> groups = new ArrayList<>();
            for (Element element: elements)
            {
                Group g = new Group();
                g.GroupName = element.text();
                g.Hyperlink = element.attr("href");
                groups.add(g);
            }
            return groups;
        }

        @Override
        protected void onPostExecute(List<Group> subjects) {
            super.onPostExecute(subjects);
            listener.OnGroupsGet(subjects);
        }
    }
    public static class GetSchoolWeekSchedule extends AsyncTask<String, Void, SchoolWeekSchedule> {
        private GetSchoolWeekListener listener;
        public GetSchoolWeekSchedule( GetSchoolWeekListener listener) {
            this.listener = listener;
        }
        @Override
        protected SchoolWeekSchedule doInBackground(String... strings) {
            SchoolWeekSchedule weekTT = new SchoolWeekSchedule();

            Document document = null;
            try {
                document = Jsoup.connect(strings[0]).timeout(30000).validateTLSCertificates(false).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert document != null;
            Elements rows = document.select("tbody > tr");

            for (int col = 1; col < rows.get(0).children().size(); col++) {
                //get hour
                SchoolDaySchedule day = new SchoolDaySchedule();

                for (int i = 1; i < rows.size(); i++) {
                    Log.i("scraped", rows.get(i).child(0).text());
                    Subject s = new Subject();
                    String hour = rows.get(i).child(0).text();
                    String[] split = hour.split(" ");
                    s.HourStart = split[1].split("-")[0];
                    day.Subjects.add(s);
                }
                weekTT.Schedules.add(day);
            }

            return weekTT;
        }

        @Override
        protected void onPostExecute(SchoolWeekSchedule schoolWeekSchedule) {
            super.onPostExecute(schoolWeekSchedule);
            listener.OnSchoolWeekReceived(schoolWeekSchedule);
        }
    }
}
