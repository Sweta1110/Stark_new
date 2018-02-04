package com.example.piyush.magicalmethods;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Explore extends AppCompatActivity implements SearchView.OnQueryTextListener {

   Toolbar toolbar;
    Drawer result=null;
    AccountHeader headerResult=null;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;
    ProgressBar mLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Explore");
        mLoadingProgress = findViewById(R.id.pb_loading);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
        load_data_from_server(0);

        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapter(this, data_list);
        recyclerView.setAdapter(adapter);


        // Create the AccountHeader
        headerResult= new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Tony Stark").withEmail("ironman@jarvis.com").withIcon(getResources().getDrawable(R.drawable.tony_stark))

                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)

                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(getResources().getDrawable(R.drawable.home)),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Explore").withIcon(getResources().getDrawable(R.drawable.explore)),

                        new ExpandableDrawerItem().withName("Your courses").withIcon(getResources().getDrawable(R.drawable.your_courses)).withIdentifier(3).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("Course 1").withLevel(2).withIdentifier(31).withSelectable(false),
                                new SecondaryDrawerItem().withName("Course 2").withLevel(2).withIdentifier(32).withSelectable(false)
                        ),

                        new PrimaryDrawerItem().withIdentifier(4).withName("Help and Feedback").withIcon(getResources().getDrawable(R.drawable.help_and_feedback)),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Developers").withIcon(getResources().getDrawable(R.drawable.developers))


                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(Explore.this, HomeActivity.class);
                                //  } else if (drawerItem.getIdentifier() == 2) {
                                //     intent = new Intent(Explore.this, Explore.class);
                            } else if (drawerItem.getIdentifier() ==4) {
                                intent = new Intent(Explore.this, HelpAndFeedback.class);
                            } else if (drawerItem.getIdentifier() ==5) {
                                intent = new Intent(Explore.this, Developers.class);
                            }
                            if (intent != null) {
                                Explore.this.startActivity(intent);
                            }
                        }

                        return false;
                    }

                })
                // .withSavedInstance(savedInstanceState)
                // .withShowDrawerOnFirstLaunch(true)
                .build();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size() - 1) {
                    load_data_from_server(data_list.get(data_list.size() - 1).getId());
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.card_list_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(Explore.this);
        return true;
    }

    private void load_data_from_server(final int id) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://mm.s-ct.asia/arrayOut.php?id=" + id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        int tocNum = object.getJSONArray("toc").length();
                        String[] table_of_contents = new String[tocNum];
                        for (int j = 0; j < tocNum; j++) {
                            table_of_contents[j] = object.getJSONArray("toc").get(j).toString();
                        }

                        System.out.println(object);

                        MyData data = new MyData(object.getInt("id"), object.getString("description"),
                                object.getString("image"), object.getString("longDesc"), table_of_contents);
                        data_list.add(data);
                }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mLoadingProgress.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                TextView error_msg = findViewById(R.id.error_msg);
                mLoadingProgress.setVisibility(View.INVISIBLE);
                if (data_list == null) {
                    error_msg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.addData(data_list);

                recyclerView.setAdapter(adapter);
            }
        };
        task.execute(id);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<MyData> newList = new ArrayList<>();
        for (MyData myData : data_list) {
            String description = myData.getDescription().toLowerCase();
            if (description.contains(newText)) {
                newList.add(myData);
            }
        }
        adapter.setFilter(newList);

        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }

    }
}
