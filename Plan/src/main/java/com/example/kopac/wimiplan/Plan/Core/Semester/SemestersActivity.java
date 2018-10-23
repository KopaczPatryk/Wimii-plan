package com.example.kopac.wimiplan.Plan.Core.Semester;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.kopac.wimiplan.Plan.Adapters.SemestersAdapter;
import com.example.kopac.wimiplan.Plan.Core.Group.GroupsActivity;
import com.example.kopac.wimiplan.Plan.Models.Semester;
import com.example.kopac.wimiplan.Plan.R;
import com.example.kopac.wimiplan.Plan.RecyclerViewClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SemestersActivity extends AppCompatActivity implements GetSemestersListener, RecyclerViewClickListener {
    private RecyclerView termList;
    private List<Semester> yearSemesters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        termList = findViewById(R.id.termSelect);
        new GetTerms(this).execute();
    }

    @Override
    public void OnSemestersGet(List<Semester> semesters) {
        yearSemesters = semesters;
        termList.setAdapter(new SemestersAdapter(semesters, this));
    }

    @Override
    public void OnRecyclerItemClick(View view, int position) {
        Intent intent = new Intent(this, GroupsActivity.class);
        intent.putExtra(GroupsActivity.TERM_BUNDLE_ID, yearSemesters.get(position));
        startActivity(intent);
    }

    public static class GetTerms extends AsyncTask<Void, Void, List<Semester>> {
        private GetSemestersListener listener;
        public GetTerms(GetSemestersListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Semester> doInBackground(Void... voids) {
            Document document = null;
            try {
                document = Jsoup.connect("https://wimii.pcz.pl/pl/plan-zajec").timeout(30000).validateTLSCertificates(false).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert document != null;
            Elements e =  document.select(".field-item > p");
            //Log.d("jsoup", e.html());
            List<Semester> semesters = new ArrayList<>();
            for (Element elem: e)
            {
                //Log.d("jsoup", elem.text());

                Semester t = new Semester();
                t.TermName = elem.text();
                t.HyperLink = elem.select("a").attr("href");

                t.IsStationary = !t.HyperLink.toLowerCase().contains("niesta");

                Log.d("scraper", t.HyperLink);
                semesters.add(t);
            }
            return semesters;
        }

        @Override
        protected void onPostExecute(List<Semester> semesters) {
            super.onPostExecute(semesters);
            listener.OnSemestersGet(semesters);
        }
    }
}
