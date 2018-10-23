package com.example.kopac.wimiplan.Plan;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.kopac.wimiplan.Plan.Groups.GroupsActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetTermsListener, RecyclerViewClickListener {
    private RecyclerView termList;
    private List<Term> YearTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        termList = findViewById(R.id.termSelect);
        new GetTerms(this).execute();
    }

    @Override
    public void OnTermsGet(List<Term> terms) {
        YearTerms = terms;
        termList.setAdapter(new TermAdapter(terms, this));
    }

    @Override
    public void OnRecyclerItemClick(View view, int position) {
        Intent intent = new Intent(this, GroupsActivity.class);
        intent.putExtra(GroupsActivity.TERM_BUNDLE_ID, YearTerms.get(position));
        startActivity(intent);
    }

    public static class GetTerms extends AsyncTask<Void, Void, List<Term>> {
        private GetTermsListener listener;
        public GetTerms(GetTermsListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Term> doInBackground(Void... voids) {
            Document document = null;
            try {
                document = Jsoup.connect("https://wimii.pcz.pl/pl/plan-zajec").timeout(30000).validateTLSCertificates(false).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert document != null;
            Elements e =  document.select(".field-item > p");
            //Log.d("jsoup", e.html());
            List<Term> terms = new ArrayList<>();
            for (Element elem: e)
            {
                //Log.d("jsoup", elem.text());

                Term t = new Term();
                t.TermName = elem.text();
                t.HyperLink = elem.select("a").attr("href");

                t.IsStationary = !t.HyperLink.toLowerCase().contains("niesta");

                Log.d("scraper", t.HyperLink);
                terms.add(t);
            }
            return terms;
        }

        @Override
        protected void onPostExecute(List<Term> terms) {
            super.onPostExecute(terms);
            listener.OnTermsGet(terms);
        }
    }
}
