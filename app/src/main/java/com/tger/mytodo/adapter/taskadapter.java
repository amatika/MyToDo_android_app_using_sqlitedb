package com.tger.mytodo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.tger.mytodo.MainActivity;
import com.tger.mytodo.R;
import com.tger.mytodo.model.taskmodel;

import java.util.List;

public class taskadapter extends RecyclerView.Adapter<taskadapter.ViewHolder>
{
    private List<taskmodel> tasklist;
    private MainActivity mainActivity;

    public taskadapter(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position)
    {
      taskmodel item=tasklist.get(position);
      holder.c.setText(item.getTask());
      holder.c.setChecked(toBoolean(item.getStatus()));
    }

    public int getItemCount()
    {
        // Return the total number of items in the RecyclerView
        return tasklist.size();
    }

    private Boolean toBoolean(int n)
    {
        return n!=0;
    }

    public void setTasks(List<taskmodel> taskss)
    {
        this.tasklist=taskss;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
       CheckBox c;
       ViewHolder(View view)
       {
           super(view);
           c=view.findViewById(R.id.chk);
       }

    }

}
