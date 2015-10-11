package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilities;

/**
 * Created by domi on 06.10.15.
 */
public class ScoresWidgetIntentService extends IntentService {

    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.DATE_COL
    };

    private static final int INDEX_AWAY = 0;
    private static final int INDEX_AWAY_GOALS = 1;
    private static final int INDEX_HOME = 2;
    private static final int INDEX_HOME_GOALS = 3;
    private static final int INDEX_DATE = 4;

    public ScoresWidgetIntentService() {
        super("ScoresWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ScoresWidgetProvider.class));

        Uri scoresUri = DatabaseContract.BASE_CONTENT_URI;
        Cursor data = getContentResolver().query(scoresUri, SCORES_COLUMNS, null, null, DatabaseContract.scores_table.DATE_COL + " DESC");

        if (data == null) {
            return;
        }

        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        String away = data.getString(INDEX_AWAY);
        int awayGoals = data.getInt(INDEX_AWAY_GOALS);
        String home = data.getString(INDEX_HOME);
        int homeGoals = data.getInt(INDEX_HOME_GOALS);
        String date = data.getString(INDEX_DATE);
        data.close();

        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.widget_scores;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            views.setTextViewText(R.id.widget_away_name, away);
            String score = Utilities.getScores(homeGoals, awayGoals);
            views.setTextViewText(R.id.widget_score_textview, score);
            views.setTextViewText(R.id.widget_home_name, home);
            views.setTextViewText(R.id.widget_date_textview, date);

            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }
}
