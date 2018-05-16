package com.example.elmbay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.elmbay.R;
import com.example.elmbay.activity.CourseDetailActivity;
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
 *
 * Created by kgu on 5/14/18.
 */

public class CourseListFragment extends Fragment implements IViewHolderClickListener {
    private ExpandableCourseAdapter mAdapter;
    private View mSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_course_list, container, false);
        setupExpandableListView(top);
        mSpinner = top.findViewById(R.id.spinner);
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
        Intent intent = new Intent(getContext(), CourseDetailActivity.class);
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

    private void setupExpandableListView(View top) {
        ExpandableListView view = top.findViewById(R.id.expendable_list);
        assert view != null;
        mAdapter = new ExpandableCourseAdapter(getContext(), this);
        view.setAdapter(mAdapter);
    }

    private boolean isReadyToLoad() {
        SessionData sessionData = AppManager.getInstance().getSessionData();
        ListChaptersResult result = sessionData.getListChaptersResult();
        return result == null
                || sessionData.getNextLoadTime() < System.currentTimeMillis() && sessionData.getInProgressChapterIndex() < 0;
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
