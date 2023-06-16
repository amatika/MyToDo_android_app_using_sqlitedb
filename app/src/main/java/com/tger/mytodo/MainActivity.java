package com.tger.mytodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tger.mytodo.adapter.taskadapter;
import com.tger.mytodo.dbutils.dbhandler;
import com.tger.mytodo.model.taskmodel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    RelativeLayout r;
    BottomSheetDialog bd;
    FloatingActionButton fabb;
    RecyclerView rv;
    taskadapter tad;
    List<taskmodel> tasklist;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this hides the action bar in the splash screen activity
        getSupportActionBar().hide();
        tasklist=new ArrayList<>();
        tasklist=new dbhandler(MainActivity.this).getAllTasks();
        rv = findViewById(R.id.recv);
        fabb=findViewById(R.id.fab);
        r=findViewById(R.id.rl);
        bd=new BottomSheetDialog(this);
     
        rv.setLayoutManager(new LinearLayoutManager(this));
        tad=new taskadapter(this);
        rv.setAdapter(tad);

        tad.setTasks(tasklist);
        createDialog();
        fabb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bd.show();
            }
        });
      //bd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        bd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);


    }


    public void createDialog()
    {
       View view = getLayoutInflater().inflate(R.layout.new_task, null, false);
       EditText newTaskText = view.findViewById(R.id.mynewtask);
       Button newTaskSaveButton = view.findViewById(R.id.addingtask);
       newTaskSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                taskmodel tm=new taskmodel();
                tm.setTask(newTaskText.getText().toString());
                tm.setStatus(0);
                dbhandler dh=new dbhandler(MainActivity.this);
                tasklist.add(0,tm);
                tad.notifyDataSetChanged();
               if(dh.addTask(tm)==true)
               {
                    bd.dismiss();
                   Toast.makeText(MainActivity.this, "Data Submitted", Toast.LENGTH_LONG).show();
               }
               else
               {
                   Toast.makeText(MainActivity.this, "Data Not Saved", Toast.LENGTH_LONG).show();
               }
            }
        });
        bd.setContentView(view);
    }
    public void updateDialog(int pos)
    {
        View view = getLayoutInflater().inflate(R.layout.new_task, null, false);
        EditText newTaskText = view.findViewById(R.id.mynewtask);
        Button newTaskSaveButton = view.findViewById(R.id.addingtask);
        newTaskText.setText(tasklist.get(pos).getTask());
        newTaskSaveButton.setText("Update");

        int status=tasklist.get(pos).getStatus();
        newTaskSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                taskmodel tm=new taskmodel();
                tm.setTask(newTaskText.getText().toString());
                tm.setStatus(status);
                dbhandler dh=new dbhandler(MainActivity.this);
                tasklist.set(pos,tm);
                tad.notifyDataSetChanged();
                dh.updateTask(tasklist.get(pos).getId(),newTaskText.getText().toString(),status);
               Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
               bd.dismiss();
            }
        });
        bd.setContentView(view);
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
    {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
        {
            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT)
            {
                new dbhandler(MainActivity.this).deleteTask(tasklist.get(position).getId());
                tasklist.remove(position);
                tad.notifyItemRemoved(position);
                tad.notifyDataSetChanged();
               if( new dbhandler(MainActivity.this).isItemExists(position))
               {
                   Snackbar snackbar = Snackbar.make(r, "Item not deleted", Snackbar.LENGTH_LONG);
                   snackbar.show();
               }
               else
               {
                   Snackbar snackbar = Snackbar.make(r, "Item deleted successfully", Snackbar.LENGTH_LONG);
                   snackbar.show();
               }
            }
            else
            {
                updateDialog(position);
                bd.show();
            }
        }
    };

}