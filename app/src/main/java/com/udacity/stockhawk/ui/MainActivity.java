package com.udacity.stockhawk.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.sync.QuoteSyncJob;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final String TICKER = "ticker";
    private static MainFragment mainFragment;
    private Toolbar toolbar;
    private static boolean twoPane;
    public static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT";
    public static final String TWO_PANE = "TWO_PANE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //toolbar setup
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this check prevents the fragment from
        //being recreated twice on config change
        if (savedInstanceState == null) {

            if (mainFragment == null) {
                mainFragment = new MainFragment();
            }

            if (findViewById(R.id.stock_chart_fragment) != null) {
                twoPane = true;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, mainFragment, MAIN_FRAGMENT_TAG).commit();
            } else {
                twoPane = false;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, mainFragment, MAIN_FRAGMENT_TAG).commit();
            }

        }


    }//end of onCreate()


    private void setDisplayModeMenuItemIcon(MenuItem item) {
        if (PrefUtils.getDisplayMode(this)
                .equals(getString(R.string.pref_display_mode_absolute_key))) {
            item.setIcon(R.drawable.ic_percentage);
        } else {
            item.setIcon(R.drawable.ic_dollar);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_settings, menu);
        MenuItem item = menu.findItem(R.id.action_change_units);
        setDisplayModeMenuItemIcon(item);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_change_units) {
            PrefUtils.toggleDisplayMode(this);
            setDisplayModeMenuItemIcon(item);

            mainFragment.adapter.notifyDataSetChanged();

            return true;
        }
        else if(id == R.id.action_refresh){
            mainFragment.onRefresh();
        }
        return super.onOptionsItemSelected(item);
    }


    public static boolean isTwoPane() {
        return twoPane;
    }

}
