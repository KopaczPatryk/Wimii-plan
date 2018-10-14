package com.example.kopac.wimiplan.Plan;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kopac.wimiplan.Plan.Models.Subject;
import com.example.kopac.wimiplan.Plan.Subjects.GetSubjectsListener;
import com.example.kopac.wimiplan.Plan.Subjects.SubjectAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SubjectsActivity extends AppCompatActivity implements GetSubjectsListener, RecyclerViewClickListener {
    public static final String HYPERLINK_SUFFIX_BUNDLE = "hyperlinksuffix";
    private RecyclerView SubjectsRecyclerView;
    private List<Subject> RetrievedSubjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        SubjectsRecyclerView = findViewById(R.id.subjects_recycler);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Term term = (Term)getIntent().getSerializableExtra(HYPERLINK_SUFFIX_BUNDLE);

//        toolbar.setTitle(term.TermName);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String hyperlink = LinkHelper.DOMAIN.concat(term.HyperLink);
        new GetSubjects(this).execute(hyperlink);
    }

    @Override
    public void OnSubjectsGet(List<Subject> subjects) {
        Collections.sort(subjects, new Comparator<Subject>() {
            @Override
            public int compare(Subject o1, Subject o2) {
                return o1.SubjectName.compareToIgnoreCase(o2.SubjectName);
            }
        });
        List<Subject> groups = new ArrayList<>();
        ArrayList<Integer> idsToRemove = new ArrayList<>();
        for (int i = 0; i < subjects.size(); i++)
        {
            if (!subjects.get(i).SubjectName.isEmpty())
            {
                groups.add(subjects.get(i));
            }
        }
        RetrievedSubjects = groups;
        SubjectsRecyclerView.setAdapter(new SubjectAdapter(groups, this));
    }

    @Override
    public void OnRecyclerItemClick(View view, int position) {
        Toast.makeText(this, RetrievedSubjects.get(position).SubjectName, Toast.LENGTH_SHORT).show();
    }

    public static class GetSubjects extends AsyncTask<String, Void, List<Subject>> {
        private GetSubjectsListener listener;
        public GetSubjects(GetSubjectsListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Subject> doInBackground(String... hyperlinks) {
            Document document = null;
            try {
                document = Jsoup.connect(hyperlinks[0]).timeout(30000).validateTLSCertificates(false).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert document != null;
            Elements elements =  document.select("td > a");
            List<Subject> subjects = new ArrayList<>();
            for (Element element: elements)
            {
                //Log.d("jsoup", elem.text());

                Subject t = new Subject("dd");
                t.SubjectName = element.text();

//                t.TermName = elem.text();
//                t.HyperLink = elem.select("a").attr("href");
//                Log.d("scraper", t.HyperLink);
                subjects.add(t);
            }
            return subjects;
        }

        @Override
        protected void onPostExecute(List<Subject> subjects) {
            super.onPostExecute(subjects);
            listener.OnSubjectsGet(subjects);
        }
    }
}
