package com.example.elmbay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.elmbay.R;
import com.example.elmbay.adapter.ExpandableCourseAdapter;
import com.example.elmbay.adapter.IViewHolderClickListener;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.SessionData;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.operation.ListChaptersOperation;
import com.example.elmbay.operation.ListChaptersRequest;
import com.example.elmbay.operation.ListChaptersResponseEvent;
import com.example.elmbay.operation.ListChaptersResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private View mSpinner;

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

        mSpinner = findViewById(R.id.spinner);
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
        if (isReadyToLoad()) {
            loadData();
        } else if (mAdapter.getGroupCount() == 0) {
            onDataLoaded();
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        // Don't go back to a blank page
    }

    @Override
    public void onLessonClick(Lesson lesson) {
        AppManager.getInstance().getSessionData().setCurrentLesson(lesson);
        Intent intent = new Intent(this, CourseDetailActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ListChaptersResponseEvent event) {
        if (event == null || !event.hasError()) {
            onDataLoaded();
        } else {
            //TODO: show error
        }
    }

    private void setupExpandableListView() {
        ExpandableListView view = findViewById(R.id.expendable_list);
        assert view != null;
        mAdapter = new ExpandableCourseAdapter(this, this);
        view.setAdapter(mAdapter);
    }

    private boolean isReadyToLoad() {
        SessionData sessionData = AppManager.getInstance().getSessionData();
        ListChaptersResult result = sessionData.getListChaptersResult();
        return result == null
                || sessionData.getNextLoadTime() < System.currentTimeMillis() && sessionData.getReadyChapterIndex() < 0;
    }

    private void loadData() {
        mSpinner.setVisibility(View.VISIBLE);

        SessionData sessionData = AppManager.getInstance().getSessionData();
        ListChaptersRequest request = new ListChaptersRequest();
        request.setUserToken(sessionData.getUserToken());
        request.setHighMark(sessionData.getHighMark());
        ListChaptersOperation op = new ListChaptersOperation(request);
        op.submit();
    }

    private void onDataLoaded() {
        mSpinner.setVisibility(View.GONE);

        ListChaptersResult result = AppManager.getInstance().getSessionData().getListChaptersResult();
        mAdapter.setChapters(result == null ? null : result.getChapters());
    }
}
