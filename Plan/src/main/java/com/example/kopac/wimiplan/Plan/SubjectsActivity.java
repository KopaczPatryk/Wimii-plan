package com.example.kopac.wimiplan.Plan;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.kopac.wimiplan.Plan.Models.Subject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubjectsActivity extends AppCompatActivity {
    public static final String HYPERLINK_SUFFIX_BUNDLE = "hyperlinksuffix";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Term term = (Term)getIntent().getSerializableExtra(HYPERLINK_SUFFIX_BUNDLE);
        toolbar.setTitle(term.TermName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public static class GetSubjects extends AsyncTask<Void, Void, List<Subject>> {
        private GetTermsListener listener;
        public GetSubjects(GetTermsListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Subject> doInBackground(Void... voids) {
            Document document = null;
            try {
                document = Jsoup.connect("https://wimii.pcz.pl/pl/plan-zajec").timeout(30000).validateTLSCertificates(false).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert document != null;
            Elements e =  document.select(".field-item > p");
            //Log.d("jsoup", e.html());
            List<Subject> subjects = new ArrayList<>();
            for (Element elem: e)
            {
                //Log.d("jsoup", elem.text());

                Subject t = new Subject();
                t.TermName = elem.text();
                t.HyperLink = elem.select("a").attr("href");
                Log.d("scraper", t.HyperLink);
                subjects.add(t);
            }
            return subjects;
        }

        @Override
        protected void onPostExecute(List<Term> terms) {
            super.onPostExecute(terms);
            listener.OnTermsGet(terms);
        }
    }
}
