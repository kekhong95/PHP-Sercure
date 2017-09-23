package com.example.kekho.myapplication.activitys;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.example.kekho.myapplication.R;
import com.example.kekho.myapplication.adapter.RowAdapter;
import com.example.kekho.myapplication.model.RowModel;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    FlowingDrawer mDrawer;
    DrawerLayout open;
    public static RecyclerView recyclerView;
    ArrayList<RowModel> rowModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_up);

        toolbar = (Toolbar) findViewById(R.id.tbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setHomeAsUpIndicator(android.R.drawable.menu_frame);
        mDrawer = (FlowingDrawer) findViewById(R.id.draw);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_OPENING) {
                    toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                } else if (newState == ElasticDrawer.STATE_CLOSED) {
                    toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.lvNavi);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        rowModels = new ArrayList<>();
        rowModels.add(new RowModel(true, R.drawable.ic_add_ed, "Thêm địa điểm"));
        rowModels.add(new RowModel(false, R.drawable.ic_history_ed, "Lịch sử"));
        rowModels.add(new RowModel(false, R.drawable.ic_settting_ed, "Cài đặt"));

        final RowAdapter rowAdapter = new RowAdapter(rowModels, getApplicationContext());
        recyclerView.setAdapter(rowAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    recyclerView.removeViewAt(2);
                    recyclerView.removeViewAt(1);
                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                    mDrawer.closeMenu(true);
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.add));
                } else if (position == 1) {
                    recyclerView.removeViewAt(2);
                    recyclerView.removeViewAt(0);
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.history));
                } else if (position == 2) {
                    recyclerView.removeViewAt(1);
                    recyclerView.removeViewAt(0);
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.setting));
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawer.openMenu();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
