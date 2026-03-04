package gr.auth.csd.mymovies;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Helper class that applies the theme
 */
public class ThemeHelper {
    public static void applyTheme(Activity activity) {
        if (isDarkModeEnabled(activity)) {
            activity.setTheme(R.style.AppTheme_Dark);
        } else {
            activity.setTheme(R.style.AppTheme_Light);
        }
    }



    private static boolean isDarkModeEnabled(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }
}
