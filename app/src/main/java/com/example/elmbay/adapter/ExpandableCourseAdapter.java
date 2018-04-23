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
            convertView = infalInflater.inflate(R.layout.chapter_row, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.chapter_id);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(chapter.getId());

        textView = (TextView) convertView.findViewById(R.id.topic);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(chapter.getTopic());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChapters.get(groupPosition).getLessons().size();
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Lesson lesson = (Lesson) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.lesson_row, null);
        }

        TextView textView = convertView.findViewById(R.id.lesson_id);
        textView.setText(lesson.getId());

        textView = convertView.findViewById(R.id.keyword);
        textView.setText(lesson.getKeyword());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onLessonClick(lesson);
            }
        });

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
        return mChapters.get(groupPosition).getLessons().get(childPosititon);
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
