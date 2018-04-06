package com.geek.hw.drawermenu;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    BottomNavigationView bottomNav;
    CameraFragment cameraFragment;
    GalleryFragment galleryFragment;
    ToolsFragment toolsFragment;
    AboutFragment aboutFragment;
    SendFragment sendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.head_imageView);
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.head_label);
        imageView.setImageResource(R.drawable.avtr);
        textView.setText(getResources().getString(R.string.header_label));

        cameraFragment = new CameraFragment();
        galleryFragment = new GalleryFragment();
        toolsFragment = new ToolsFragment();
        aboutFragment = new AboutFragment();
        sendFragment = new SendFragment();

        bottomNav = findViewById(R.id.navigation_bottom);
        bottomNav.setOnNavigationItemSelectedListener(this);

        setScreen(cameraFragment, R.id.menu_item_camera);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_camera:
            case R.id.nav_camera:
                setScreen(cameraFragment, R.id.menu_item_camera);
                return true;
            case R.id.menu_item_gallery:
            case R.id.nav_gallery:
                setScreen(galleryFragment, R.id.menu_item_gallery);
                return true;
            case R.id.menu_item_tools:
            case R.id.nav_tools:
                setScreen(toolsFragment, R.id.menu_item_tools);
                return true;
            case R.id.menu_item_about:
                setScreen(aboutFragment, R.id.menu_item_about);
                return true;
            case R.id.menu_item_send:
                setScreen(sendFragment, R.id.menu_item_send);
                return true;
            default:
                return false;
        }
    }

    private void setScreen(Fragment fragment, int itemId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
        navigationView.setCheckedItem(itemId);

        switch (itemId) {
            case R.id.menu_item_camera:
                bottomNav.getMenu().findItem(R.id.nav_camera).setChecked(true);
                break;
            case R.id.menu_item_gallery:
                bottomNav.getMenu().findItem(R.id.nav_gallery).setChecked(true);
                break;
            case R.id.menu_item_tools:
                bottomNav.getMenu().findItem(R.id.nav_tools).setChecked(true);
                break;
            default:
                bottomNav.getMenu().findItem(R.id.nav_camera).setChecked(true);
                break;
        }

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
