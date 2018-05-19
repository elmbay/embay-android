package com.example.elmbay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.elmbay.R;
import com.example.elmbay.activity.CourseDetailActivity;
import com.example.elmbay.adapter.ExpandableCourseAdapter;
import com.example.elmbay.adapter.IViewHolderClickListener;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.SessionData;
import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Course;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.operation.GetCoursesOperation;
import com.example.elmbay.operation.GetCoursesRequest;
import com.example.elmbay.operation.GetCoursesResponseEvent;
import com.example.elmbay.operation.GetCoursesResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 *
 * Created by kgu on 5/14/18.
 */

public class CourseListFragment extends Fragment implements IViewHolderClickListener {
    private ExpandableCourseAdapter mAdapter;
    private View mSpinner;
    private TextView mInfoBanner;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_course_list, container, false);
        setupExpandableListView(top);
        mSpinner = top.findViewById(R.id.spinner);
        mInfoBanner = top.findViewById(R.id.info_banner);
        return top;
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
            checkInfoBanner();
        }
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onLessonClick(Chapter chapter, Lesson lesson) {
        SessionData sessionData = AppManager.getInstance().getSessionData();
        sessionData.setCurrentChapter(chapter);
        sessionData.setCurrentLesson(lesson);
        Intent intent = new Intent(getContext(), CourseDetailActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetCoursesResponseEvent event) {
        if (event == null || !event.hasError()) {
            onDataLoaded();
        } else {
            //TODO: show error
        }
    }

    private void setupExpandableListView(View top) {
        ExpandableListView view = top.findViewById(R.id.expendable_list);
        assert view != null;
        mAdapter = new ExpandableCourseAdapter(getContext(), this);
        view.setAdapter(mAdapter);
    }

    private boolean isReadyToLoad() {
        SessionData sessionData = AppManager.getInstance().getSessionData();
        GetCoursesResult result = sessionData.getListChaptersResult();
        return result == null
                || sessionData.getNextLoadTime() < System.currentTimeMillis() && sessionData.getInProgressChapterIndex() < 0;
    }

    private void loadData() {
        mSpinner.setVisibility(View.VISIBLE);

        SessionData sessionData = AppManager.getInstance().getSessionData();
        GetCoursesRequest request = new GetCoursesRequest();
        request.setUserToken(sessionData.getUserToken());
        request.setHighMark(sessionData.getHighMark());
        GetCoursesOperation op = new GetCoursesOperation(request);
        op.submit();
    }

    private void onDataLoaded() {
        mSpinner.setVisibility(View.GONE);

        GetCoursesResult result = AppManager.getInstance().getSessionData().getListChaptersResult();
        if (result != null && result.getCourse() != null) {
            Course course = result.getCourse();
            mAdapter.setChapters(course.getChapters());
            if (!TextUtils.isEmpty(course.getName())) {
                getActivity().setTitle(course.getName());
            }
        } else {
            mAdapter.setChapters(null);
        }
        checkInfoBanner();
    }

    private void checkInfoBanner() {
        SessionData sessionData = AppManager.getInstance().getSessionData();
        if (sessionData.getInProgressChapterIndex() < 0) {
            Log.i("Parrot", "loadTime=" + sessionData.getNextLoadTime() + " now=" + System.currentTimeMillis());
            long elapsedHour = (sessionData.getNextLoadTime() - System.currentTimeMillis()) / SessionData.HOUR_TO_MILLIS;
            if (elapsedHour <= 1) {
                elapsedHour = 1;
                mInfoBanner.setText(getContext().getString(R.string.come_back_singular, elapsedHour));
            } else {
                mInfoBanner.setText(getContext().getString(R.string.come_back_plural, elapsedHour));
            }
            mInfoBanner.setVisibility(View.VISIBLE);
        } else {
            mInfoBanner.setVisibility(View.GONE);
        }
    }
}
