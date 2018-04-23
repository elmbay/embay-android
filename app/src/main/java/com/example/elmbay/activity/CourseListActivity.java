package com.example.elmbay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ExpandableListView;

import com.example.elmbay.R;
import com.example.elmbay.adapter.ExpandableCourseAdapter;
import com.example.elmbay.adapter.IViewHolderClickListener;
import com.example.elmbay.event.SignInResponseEvent;
import com.example.elmbay.fragment.LoaderFragment;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.SignInOperation;
import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.model.SignInRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.setup(getApplicationContext());

        setContentView(R.layout.activity_course_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getTitle());
        }

        setupExpandableListView();

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f = new LoaderFragment();
            ft.add(R.id.course_loader, f).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
        if (AppManager.getInstance().getSessionData().getSignInResult() == null) {
            loadCourses();
        }
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onLessonClick(Lesson lesson) {
        AppManager.getInstance().getSessionData().setCurrentLesson(lesson);
        Intent intent = new Intent(this, CourseDetailActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SignInResponseEvent event) {
        if (!event.hasError()) {
            try {
                List<Chapter> chapters =AppManager.getInstance().getSessionData().getSignInResult().getChapters();
                mAdapter.setChapters(chapters);
            } catch (Exception e) {
                if (AppManager.DEBUG) {
                    Log.w(LOG_TAG, "get chapters: " + e.getMessage());
                }
            }
        }
    }

    private void setupExpandableListView() {
        ExpandableListView view = findViewById(R.id.expendable_list);
        assert view != null;
        mAdapter = new ExpandableCourseAdapter(this, this);
        view.setAdapter(mAdapter);
    }

    private void loadCourses() {
        SignInRequest request = new SignInRequest("xueshengjia", "1234", null);
        SignInOperation op = new SignInOperation(request, true);
        op.submit();
    }
}
