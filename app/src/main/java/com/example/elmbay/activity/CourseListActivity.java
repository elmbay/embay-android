package com.example.elmbay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.elmbay.adapter.CourseListAdapter;
import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.Lesson;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CourseDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CourseListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = CourseListActivity.class.getName();
    private CourseListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.setup(getApplicationContext());

        setContentView(R.layout.activity_course_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getTitle());
        }

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        try {
            Lesson lessons[] = AppManager.getInstance().getSessionData().getSignInResult().getLessons();
            mAdapter = new CourseListAdapter(lessons, this);
            recyclerView.setAdapter(mAdapter);

            // Add horizontal divider between items
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
        } catch (Exception e) {
            if (AppManager.DEBUG) {
                Log.e(LOG_TAG, "Exception at accessing lessons: " + e.getMessage());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppManager.getInstance().getSessionData().setCurrentLessonIndex(position);
        Intent intent = new Intent(this, CourseDetailActivity.class);
        startActivity(intent);
    }
}
