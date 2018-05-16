package com.example.elmbay.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.elmbay.R;
import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;

import java.util.List;
import java.util.Locale;

import static com.example.elmbay.manager.SessionData.COURSE_STATUS_INPROGRESS;
import static com.example.elmbay.manager.SessionData.COURSE_STATUS_LOCKED;

/**
 * Created by kgu on 4/20/18.
 *
 * See http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 * For expandable RecyclerView see https://github.com/bignerdranch/expandable-recycler-view/tree/master/expandablerecyclerview/src/main/java/com/bignerdranch/expandablerecyclerview
 */

public class ExpandableCourseAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<Chapter> mChapters;
    private IViewHolderClickListener mItemClickListener;

    public ExpandableCourseAdapter(Context context, IViewHolderClickListener itemClickListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
    }

    public void setChapters(List<Chapter> chapters) {
        mChapters = chapters;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mChapters == null ? 0 : mChapters.size();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Chapter chapter = (Chapter) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_chapter, null);
        }
        TextView textView = convertView.findViewById(R.id.chapter_id);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(String.format(Locale.getDefault(),"%d", chapter.getId()));

        textView = convertView.findViewById(R.id.topic);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(chapter.getTopic());

        View checkMark = convertView.findViewById(R.id.status);
        View rowView = convertView.findViewById(R.id.chapter_row);
        switch(chapter.getStatus()) {
            case COURSE_STATUS_LOCKED:
                rowView.setAlpha((float) 0.2);
                checkMark.setVisibility(View.GONE);
                break;
            case COURSE_STATUS_INPROGRESS:
                rowView.setAlpha((float) 1.0);
                checkMark.setVisibility(View.GONE);
                break;
            default:
                rowView.setAlpha((float) 1.0);
                checkMark.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Lesson> lessons = mChapters.get(groupPosition).getLessons();
        return lessons == null ? 0 : lessons.size();
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Lesson lesson = (Lesson) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_lesson, null);
        }

        TextView textView = convertView.findViewById(R.id.lesson_id);
        textView.setText(String.format(Locale.getDefault(),"%d", lesson.getId()));

        textView = convertView.findViewById(R.id.keyword);
        textView.setText(lesson.getKeyword());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onLessonClick(lesson);
            }
        });

        View checkMark = convertView.findViewById(R.id.status);
        View rowView = convertView.findViewById(R.id.lesson_row);
        switch(lesson.getStatus()) {
            case COURSE_STATUS_LOCKED:
                rowView.setAlpha((float) 0.2);
                checkMark.setVisibility(View.GONE);
                break;
            case COURSE_STATUS_INPROGRESS:
                rowView.setAlpha((float) 1.0);
                checkMark.setVisibility(View.GONE);
                break;
            default:
                rowView.setAlpha((float) 1.0);
                checkMark.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mChapters.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        List<Lesson> lessons = mChapters.get(groupPosition).getLessons();
        return lessons == null ? null : lessons.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
