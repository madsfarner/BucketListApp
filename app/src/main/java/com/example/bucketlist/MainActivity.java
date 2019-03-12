package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, IdeaAdapter.CheckButtonListener {

    //TODO
    //-text strikethrough when checked
    //-long hold to delete

    private RecyclerView rvIdeaList;
    private IdeaAdapter ideaAdapter;
    private List<Idea> ideaList = new ArrayList<>();
    private IdeaRoomDatabase db;
    public static final int REQUEST_CODE = 1234;
    public static final String EXTRATEXT_TITLE = "title";
    public static final String EXTRATEXT_DESC = "desc";

    private GestureDetector gestureDetector;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = IdeaRoomDatabase.getDatabase(this);

        initToolbar();
        initRecyclerView();
        initFloatingActionButton();

    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Shopping List");
        setSupportActionBar(toolbar);
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddIdeaActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void initRecyclerView(){
        ideaAdapter = new IdeaAdapter(ideaList, this);
        rvIdeaList = findViewById(R.id.recyclerView);
        rvIdeaList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvIdeaList.setAdapter(ideaAdapter);
        rvIdeaList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e){
                super.onLongPress(e);
                View child = rvIdeaList.findChildViewUnder(e.getX(), e.getY());
                if(child != null){
                    int adapterPosition = rvIdeaList.getChildAdapterPosition(child);
                    deleteIdea(ideaList.get(adapterPosition));
                }
            }
        });
        rvIdeaList.addOnItemTouchListener(this);
        getAllIdeas();
    }

    private void updateUI(List<Idea> ideas) {
        ideaList.clear();
        ideaList.addAll(ideas);
        ideaAdapter.notifyDataSetChanged();
    }

    private void getAllIdeas() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Idea> ideas = db.ideaDao().getallIdeas();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(ideas);
                    }
                });
            }
        });
    }

    private void insertIdea(final Idea idea) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.ideaDao().insert(idea);
            }
        });
    }

    private void deleteIdea(final Idea idea) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.ideaDao().delete(idea);
                getAllIdeas();
            }
        });
    }

    private void updateIdea(final Idea idea) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.ideaDao().update(idea);
                getAllIdeas();
            }
        });
    }

    private void deleteAllIdeas(final List<Idea> ideaList) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.ideaDao().delete(ideaList);
                getAllIdeas();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_item) {
            deleteAllIdeas(ideaList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(REQUEST_CODE == requestCode){
            if(resultCode == RESULT_OK) {
                String title = data.getStringExtra(EXTRATEXT_TITLE);
                String desc = data.getStringExtra(EXTRATEXT_DESC);
                Idea idea = new Idea(title, desc);
                insertIdea(idea);
                getAllIdeas();
            }
        }
    }

    @Override
    public void onCheckClick(Idea idea) {
        updateIdea(idea);
        getAllIdeas();
    }
}
