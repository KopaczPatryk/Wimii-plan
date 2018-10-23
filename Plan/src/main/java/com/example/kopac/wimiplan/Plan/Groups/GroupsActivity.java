package com.example.kopac.wimiplan.Plan.Groups;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
    public static final String TERM_BUNDLE_ID = "hyperlinksuffix";
    private RecyclerView GroupsRecyclerView;
    private List<Group> RetrievedGroups;
    private Term Term;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        GroupsRecyclerView = findViewById(R.id.groups_recycler);
        if (savedInstanceState != null)
        {
            Term = (Term) savedInstanceState.getSerializable(TERM_BUNDLE_ID);
        }
        else
        {
            Term = (Term)getIntent().getSerializableExtra(TERM_BUNDLE_ID);
        }
        assert Term != null;
        String hyperlink = LinkHelper.DOMAIN.concat(Term.HyperLink);
        new GetGroups(this).execute(hyperlink);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(TERM_BUNDLE_ID, Term);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Term = (Term) savedInstanceState.getSerializable(TERM_BUNDLE_ID);
        super.onRestoreInstanceState(savedInstanceState);

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
        if (Term.IsStationary)
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

            Document document = null;
            try {
                document = Jsoup.connect(strings[0]).timeout(30000).validateTLSCertificates(false).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert document != null;
            Elements rows = document.select("tbody > tr");
            SchoolWeekSchedule week = new SchoolWeekSchedule();

            for (int x = 1; x < rows.get(0).children().size(); x++) {
                SchoolDaySchedule day = new SchoolDaySchedule();
                day.DayOfWeek = rows.get(0).child(x).text().substring(0, 2);
                week.DaySchedules.add(day);
                for (int y = 1; y < rows.size() ; y++)
                {
                    Subject s = new Subject();
                    day.Subjects.add(s);

                    String hour = rows.get(y).child(0).text();
                    Log.i("scraped", hour);
                    String[] split = hour.split(" ");
                    s.HourStart = split[1].split("-")[0];

                    //subject name
                    String cellText = rows.get(y).child(x).html();
                    if (!cellText.equals("&nbsp;"))
                    {

                        //split("[\\w.]+\\.");
                        String[] cellSplit = cellText.split("<br>");
                        String subjectName = cellSplit[0];
                        s.SubjectName = subjectName.trim();
                        if (cellSplit.length > 1) {
                            s.Teacher = cellSplit[1];
                        }
                        if (cellSplit.length > 2) {
                            s.Room = cellSplit[cellSplit.length - 1];
                        }
                    }

                }
            }
            return week;
        }

        @Override
        protected void onPostExecute(SchoolWeekSchedule schoolWeekSchedule) {
            super.onPostExecute(schoolWeekSchedule);
            listener.OnSchoolWeekReceived(schoolWeekSchedule);
        }
    }
}
