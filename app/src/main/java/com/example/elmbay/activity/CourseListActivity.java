package com.example.elmbay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ExpandableListView;

import com.example.elmbay.R;
import com.example.elmbay.adapter.ExpandableCourseAdapter;
import com.example.elmbay.adapter.IViewHolderClickListener;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CourseDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 * sectioned-recyclerview https://github.com/afollestad/sectioned-recyclerview/blob/master/sample/src/main/java/com/afollestad/sectionedrecyclerviewsample/MainAdapter.java
 */
public class CourseListActivity extends AppCompatActivity implements IViewHolderClickListener {
    private static final String LOG_TAG = CourseListActivity.class.getName();
    private ExpandableCourseAdapter mAdapter;

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

        setupExpandableListView();
    }

    private void setupExpandableListView() {
        ExpandableListView view = findViewById(R.id.expendable_list);
        assert view != null;
        try {
            List<Chapter> chapters = AppManager.getInstance().getSessionData().getSignInResult().getChapters();
            mAdapter = new ExpandableCourseAdapter(this, chapters, this);
            view.setAdapter(mAdapter);

            // Add horizontal divider between items
//            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                    LinearLayoutManager.VERTICAL);
//            view.addItemDecoration(dividerItemDecoration);
        } catch (Exception e) {
            if (AppManager.DEBUG) {
                Log.e(LOG_TAG, "Exception at accessing chapters: " + e.getMessage());
            }
        }
    }

    @Override
    public void onLessonClick(Lesson lesson) {
        AppManager.getInstance().getSessionData().setCurrentLesson(lesson);
        Intent intent = new Intent(this, CourseDetailActivity.class);
        startActivity(intent);
    }
}
