package com.example.elmbay.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
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
import com.example.elmbay.manager.CourseManager;
import com.example.elmbay.manager.SessionData;
import com.example.elmbay.manager.UserManager;
import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Course;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.operation.GetChapterOperation;
import com.example.elmbay.operation.GetChapterRequest;
import com.example.elmbay.operation.GetChapterResponseEvent;
import com.example.elmbay.operation.GetCourseOperation;
import com.example.elmbay.operation.GetCourseRequest;
import com.example.elmbay.operation.GetCourseResponseEvent;
import com.example.elmbay.operation.GetCourseResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.elmbay.manager.CourseManager.COURSE_STATUS_LOCKED;

/**
 *
 * Created by kgu on 5/14/18.
 */

public class CourseListFragment extends Fragment implements IViewHolderClickListener {
    private ExpandableCourseAdapter mAdapter;
    private ExpandableListView mExpandableListView;
    private View mSpinner;
    private View mInfoBanner;
    private TextView mInfoText;
    private AlertDialog mDialog;
    private int mGroupPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_course_list, container, false);
        setupExpandableListView(top);
        mExpandableListView = top.findViewById(R.id.expendable_list);
        mSpinner = top.findViewById(R.id.spinner);
        mInfoBanner = top.findViewById(R.id.info_banner);
        mInfoText = top.findViewById(R.id.info_text);
        View mRefresh = top.findViewById(R.id.refresh);
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReadyToLoad()) {
                    loadCourses();
                }
            }
        });
        return top;
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
        if (isReadyToLoad()) {
            loadCourses();
        } else if (mAdapter.getGroupCount() == 0) {
            onCoursesLoaded();
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
    public void onDestroy() {
        super.onDestroy();
        mDialog = null;
    }

    @Override
    public void onChapterClick(@NonNull Chapter chapter) {
        if (chapter.getStatus() != COURSE_STATUS_LOCKED && chapter.getLessons() == null) {
            loadLessons(chapter);
        }
    }

    @Override
    public void onLessonClick(@NonNull Chapter chapter, @NonNull Lesson lesson) {
        if (lesson.getStatus() != COURSE_STATUS_LOCKED) {
            CourseManager courseManager = AppManager.getInstance().getSessionData().getCourseManager();
            courseManager.setChapterSelected(chapter);
            courseManager.setLessonSelected(lesson);
            Intent intent = new Intent(getContext(), CourseDetailActivity.class);
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetCourseResponseEvent event) {
        if (event == null || !event.hasError()) {
            onCoursesLoaded();
        } else {
            mSpinner.setVisibility(View.GONE);
            showDialog(event.getError().getMessageId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetChapterResponseEvent event) {
        if (event == null || !event.hasError()) {
            onLessonsLoaded();
        } else {
            mSpinner.setVisibility(View.GONE);
            showDialog(event.getError().getMessageId());
        }
    }

    private void setupExpandableListView(View top) {
        ExpandableListView view = top.findViewById(R.id.expendable_list);
        assert view != null;
        mAdapter = new ExpandableCourseAdapter(getContext(), this);
        view.setAdapter(mAdapter);
    }

    private boolean isReadyToLoad() {
        CourseManager courseManager = AppManager.getInstance().getSessionData().getCourseManager();
        GetCourseResult result = courseManager.getCourseResult();
        return result == null
                || courseManager.getNextLoadTime() < System.currentTimeMillis() && courseManager.getInProgressChapterIndex() < 0;
    }

    private void loadCourses() {
        mSpinner.setVisibility(View.VISIBLE);

        UserManager userManager = AppManager.getInstance().getSessionData().getUserManager();
        GetCourseRequest request = new GetCourseRequest();
        request.setUserToken(userManager.getUserToken());
        request.setHighMark(userManager.getHighMark());
        GetCourseOperation op = new GetCourseOperation(request);
        op.submit();
    }

    private void loadLessons(Chapter chapter) {
        mSpinner.setVisibility(View.VISIBLE);

        GetChapterRequest request = new GetChapterRequest();
        request.setChapterId(chapter.getId());
        GetChapterOperation op = new GetChapterOperation(request);
        op.submit();
    }

    private void onCoursesLoaded() {
        mSpinner.setVisibility(View.GONE);

        CourseManager courseManager = AppManager.getInstance().getSessionData().getCourseManager();
        GetCourseResult result = courseManager.getCourseResult();
        if (result != null && result.getCourse() != null) {
            Course course = result.getCourse();
            if (!TextUtils.isEmpty(course.getName())) {
                getActivity().setTitle(course.getName());
            }
            mAdapter.setChapters(course.getChapters());
            if (courseManager.getInProgressChapterIndex() >= 0) {
                mGroupPosition = courseManager.getInProgressChapterIndex();
                mExpandableListView.setSelectedGroup(mGroupPosition);
                if (courseManager.getInProgressLessonIndex() >= 0) {
                    onLessonClick(courseManager.getChapterSelected(), courseManager.getLessonSelected());
                } else {
                    loadLessons(course.getChapters().get(mGroupPosition));
                }
            }
        } else {
            mAdapter.setChapters(null);
        }
        checkInfoBanner();
    }

    private void onLessonsLoaded() {
        // Lessons have been set in the parent chapter by CourseManager
        mSpinner.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
    }

    private void checkInfoBanner() {
        CourseManager courseManager = AppManager.getInstance().getSessionData().getCourseManager();
        if (courseManager.getInProgressChapterIndex() >= 0) {
            mInfoBanner.setVisibility(View.GONE);
        } else {
            long elapsedMinutes = (courseManager.getNextLoadTime() - System.currentTimeMillis()) / SessionData.MINUTE_TO_MILLIS;
            SimpleDateFormat dateFormat = new SimpleDateFormat(getContext().getString(R.string.date_format));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, (int)elapsedMinutes);
            mInfoText.setText(getContext().getString(R.string.come_back, dateFormat.format(calendar.getTime())));
            mInfoBanner.setVisibility(View.VISIBLE);
        }
    }

    private void showDialog(int stringId) {
        if (mDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Material_Light_Dialog));
            builder.setMessage(stringId)
                    .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // retry
                            loadCourses();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().onBackPressed();
                        }
                    });
            mDialog = builder.create();
        } else {
            mDialog.setMessage(getContext().getString(stringId));
        }
        mDialog.show();
    }
}
