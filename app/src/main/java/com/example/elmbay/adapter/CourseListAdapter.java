package com.example.elmbay.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.Lesson;

/**
 * Created by kgu on 4/17/18.
 */

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {
    private static final String LOG_TAG = CourseListAdapter.class.getName();
    private AdapterView.OnItemClickListener mItemClickListener;
    private Lesson mLessons[];

    public CourseListAdapter(Lesson[] lessons, AdapterView.OnItemClickListener onItemClickListener) {
        mLessons = lessons;
        mItemClickListener = onItemClickListener;
    }

    @Override
    public CourseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
        return new CourseListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CourseListViewHolder holder, int position) {
        try {
            holder.mIdView.setText(mLessons[position].getId());
            holder.mSubject.setText(mLessons[position].getSubject());
        } catch (Exception e) {
            if (AppManager.DEBUG) {
                Log.e(LOG_TAG, "Exception at Lessons[" + position + "]:" + e.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mLessons == null ? 0 : mLessons.length;
    }

    class CourseListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mIdView;
        final TextView mSubject;

        CourseListViewHolder(View view) {
            super(view);

            mIdView = view.findViewById(R.id.id_text);
            mSubject = view.findViewById(R.id.content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(null, itemView, getAdapterPosition(), getItemId());
        }
    }
}
