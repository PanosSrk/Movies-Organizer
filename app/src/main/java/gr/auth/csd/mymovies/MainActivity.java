package gr.auth.csd.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import gr.auth.csd.mymovies.databinding.ActivityMainBinding;

/**
 * The main Activity of the app. Creates a bottom nav menu with 3 items
 */
public class MainActivity extends AppCompatActivity {

    private Fragment homeFragment;
    private Fragment discoverFragment;
    private Fragment collectionsFragment;
    private Fragment activeFragment;
    private ActivityMainBinding binding;


    private static Collections collections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply the theme
        ThemeHelper.applyTheme(this);

        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //create the collections
        collections = new Collections(this);

        // Bottom Navigation Bar
        homeFragment = new HomeFragment();
        discoverFragment = new DiscoverFragment();
        collectionsFragment = new CollectionsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.flfragment, collectionsFragment, "3").hide(collectionsFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.flfragment, discoverFragment, "2").hide(discoverFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.flfragment,homeFragment, "1").commit();
        activeFragment=homeFragment;

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;

                return true;
            }
            if (item.getItemId() == R.id.discover) {
                getSupportFragmentManager().beginTransaction().hide(activeFragment).show(discoverFragment).commit();
                activeFragment = discoverFragment;
                return true;
            }
            if (item.getItemId() == R.id.collections) {
                getSupportFragmentManager().beginTransaction().hide(activeFragment).show(collectionsFragment).commit();
                activeFragment = collectionsFragment;

                return true;
            }
            return false;

        });
        binding.bottomNavigationView.setSelectedItemId(R.id.home);


    }

    public static Collections getCollections(){
        return collections;
    }



    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ThemeHelper.applyTheme(this);  // Apply the updated theme

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.select_theme) {
            // Display theme selection dialog
            showThemeSelectionDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showThemeSelectionDialog() {
        String[] themeOptions = {"Light", "Dark"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Theme")
                .setItems(themeOptions, (dialog, which) -> {
                    switch (which) {
                        case 0: // Light theme
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            getSupportFragmentManager().beginTransaction().hide(activeFragment).commit();
                            recreate(); // Apply the new theme
                            break;
                        case 1: // Dark theme
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            Log.println(Log.ERROR,"error","here3");
                            getSupportFragmentManager().beginTransaction().hide(activeFragment).commit();
                            recreate(); // Apply the new theme
                            break;
                    }
                });

        builder.create().show();
    }
}