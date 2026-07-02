package com.ideaverse.stoiccard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity {
    static final String PREFS_NAME = "stoic_card";
    static final String MORNING_QUOTE_KEY = "morning_quote";
    private static final String ENTRIES_KEY_PREFIX = "entries_";
    private static final String OPENED_DAYS_KEY = "opened_days";
    private static final int ENTRY_RETENTION_DAYS = 30;
    private static final int STREAK_DOT_COUNT = 14;
    private static final int MAX_ENTRY_LENGTH = 140;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 10;

    private static final int COLOR_BACKGROUND = Color.rgb(7, 8, 10);
    private static final int COLOR_GRID_DOT = Color.rgb(20, 21, 24);
    private static final int COLOR_PANEL_STROKE = Color.rgb(52, 54, 58);
    private static final int COLOR_TEXT = Color.rgb(246, 246, 241);
    private static final int COLOR_MUTED = Color.rgb(150, 153, 158);
    private static final int COLOR_FAINT = Color.rgb(64, 66, 70);
    private static final int COLOR_RED = Color.rgb(232, 37, 37);

    private static final String[] MONTHS = {
            "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    };

    // 5x7 dot-matrix glyphs, only what the header needs
    private static final Map<Character, String[]> DOT_FONT = new HashMap<>();

    static {
        DOT_FONT.put('0', new String[]{"01110", "10001", "10011", "10101", "11001", "10001", "01110"});
        DOT_FONT.put('1', new String[]{"00100", "01100", "00100", "00100", "00100", "00100", "01110"});
        DOT_FONT.put('2', new String[]{"01110", "10001", "00001", "00010", "00100", "01000", "11111"});
        DOT_FONT.put('3', new String[]{"11111", "00010", "00100", "00010", "00001", "10001", "01110"});
        DOT_FONT.put('4', new String[]{"00010", "00110", "01010", "10010", "11111", "00010", "00010"});
        DOT_FONT.put('5', new String[]{"11111", "10000", "11110", "00001", "00001", "10001", "01110"});
        DOT_FONT.put('6', new String[]{"00110", "01000", "10000", "11110", "10001", "10001", "01110"});
        DOT_FONT.put('7', new String[]{"11111", "00001", "00010", "00100", "01000", "01000", "01000"});
        DOT_FONT.put('8', new String[]{"01110", "10001", "10001", "01110", "10001", "10001", "01110"});
        DOT_FONT.put('9', new String[]{"01110", "10001", "10001", "01111", "00001", "00010", "01100"});
        DOT_FONT.put('D', new String[]{"11100", "10010", "10001", "10001", "10001", "10010", "11100"});
        DOT_FONT.put('A', new String[]{"01110", "10001", "10001", "11111", "10001", "10001", "10001"});
        DOT_FONT.put('Y', new String[]{"10001", "10001", "01010", "00100", "00100", "00100", "00100"});
        DOT_FONT.put('"', new String[]{"01010", "01010", "01010", "00000", "00000", "00000", "00000"});
        DOT_FONT.put(' ', new String[]{"00000", "00000", "00000", "00000", "00000", "00000", "00000"});
    }

    private SharedPreferences prefs;
    private String todayKey;
    private EditText frictionInput;
    private LinearLayout entriesContainer;
    private TextView emptyText;
    private LinearLayout todaySection;
    private LinearLayout historySection;
    private LinearLayout historyContainer;
    private TextView historyEmpty;
    private Button todayTabButton;
    private Button historyTabButton;
    private Switch morningSwitch;
    private boolean historyVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        todayKey = dayKey(Calendar.getInstance());
        markOpenedToday();
        pruneOldEntries();
        configureWindow();
        buildUi();
        renderEntries();
    }

    private void configureWindow() {
        Window window = getWindow();
        window.setStatusBarColor(COLOR_BACKGROUND);
        window.setNavigationBarColor(COLOR_BACKGROUND);
    }

    // ---------- date helpers ----------

    private static String dayKey(Calendar c) {
        return String.format(Locale.US, "%04d-%02d-%02d",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
    }

    private String prettyDay(String key) {
        int month = Integer.parseInt(key.substring(5, 7));
        int day = Integer.parseInt(key.substring(8, 10));
        String label = String.format(Locale.US, "%02d %s", day, MONTHS[month - 1]);
        return key.equals(todayKey) ? label + " · TODAY" : label;
    }

    // ---------- storage ----------

    private void markOpenedToday() {
        Set<String> opened = new HashSet<>(prefs.getStringSet(OPENED_DAYS_KEY, Collections.<String>emptySet()));
        if (opened.add(todayKey)) {
            if (opened.size() > 90) {
                List<String> sorted = new ArrayList<>(opened);
                Collections.sort(sorted);
                opened = new HashSet<>(sorted.subList(sorted.size() - 90, sorted.size()));
            }
            prefs.edit().putStringSet(OPENED_DAYS_KEY, opened).apply();
        }
    }

    private Set<String> openedDays() {
        return prefs.getStringSet(OPENED_DAYS_KEY, Collections.<String>emptySet());
    }

    private int streakCount() {
        Set<String> opened = openedDays();
        Calendar c = Calendar.getInstance();
        int count = 0;
        while (opened.contains(dayKey(c))) {
            count++;
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        return count;
    }

    private void pruneOldEntries() {
        Calendar cutoff = Calendar.getInstance();
        cutoff.add(Calendar.DAY_OF_MONTH, -ENTRY_RETENTION_DAYS);
        String cutoffKey = ENTRIES_KEY_PREFIX + dayKey(cutoff);
        SharedPreferences.Editor editor = prefs.edit();
        boolean dirty = false;
        for (String key : prefs.getAll().keySet()) {
            if (key.startsWith(ENTRIES_KEY_PREFIX) && key.compareTo(cutoffKey) < 0) {
                editor.remove(key);
                dirty = true;
            }
        }
        if (dirty) {
            editor.apply();
        }
    }

    private JSONArray loadEntries(String key) {
        try {
            return new JSONArray(prefs.getString(ENTRIES_KEY_PREFIX + key, "[]"));
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    private void saveTodayEntries(JSONArray entries) {
        prefs.edit().putString(ENTRIES_KEY_PREFIX + todayKey, entries.toString()).apply();
    }

    // ---------- ui ----------

    private void buildUi() {
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(COLOR_BACKGROUND);

        root.addView(new DotGridView(this), new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        root.addView(scrollView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        final LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(22), dp(28), dp(22), dp(28));
        scrollView.addView(content, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
        ));

        root.setOnApplyWindowInsetsListener((v, insets) -> {
            int top;
            int bottom;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                android.graphics.Insets bars = insets.getInsets(
                        WindowInsets.Type.systemBars() | WindowInsets.Type.ime());
                top = bars.top;
                bottom = bars.bottom;
            } else {
                top = insets.getSystemWindowInsetTop();
                bottom = insets.getSystemWindowInsetBottom();
            }
            content.setPadding(dp(22), dp(28) + top, dp(22), dp(28) + bottom);
            return insets;
        });

        Calendar now = Calendar.getInstance();

        // header: pulse dot, brand, date
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        View pulse = new View(this);
        GradientDrawable pulseShape = new GradientDrawable();
        pulseShape.setShape(GradientDrawable.OVAL);
        pulseShape.setColor(COLOR_RED);
        pulse.setBackground(pulseShape);
        AlphaAnimation blink = new AlphaAnimation(1f, 0.15f);
        blink.setDuration(1200);
        blink.setRepeatMode(Animation.REVERSE);
        blink.setRepeatCount(Animation.INFINITE);
        pulse.startAnimation(blink);
        LinearLayout.LayoutParams pulseParams = new LinearLayout.LayoutParams(dp(8), dp(8));
        pulseParams.rightMargin = dp(8);
        header.addView(pulse, pulseParams);

        header.addView(labelText("STOIC CARD // 002", 10, COLOR_MUTED));

        View headerSpacer = new View(this);
        header.addView(headerSpacer, new LinearLayout.LayoutParams(0, 1, 1f));

        String dateLabel = String.format(Locale.US, "%02d %s",
                now.get(Calendar.DAY_OF_MONTH), MONTHS[now.get(Calendar.MONTH)]);
        header.addView(labelText(dateLabel, 10, COLOR_TEXT));

        content.addView(header);

        // day of year, dot matrix
        TextView dayLabel = labelText("DAY OF YEAR", 10, COLOR_MUTED);
        LinearLayout.LayoutParams dayLabelParams = wrapParams();
        dayLabelParams.topMargin = dp(30);
        content.addView(dayLabel, dayLabelParams);

        DotMatrixView dayMatrix = new DotMatrixView(this,
                "DAY " + now.get(Calendar.DAY_OF_YEAR), 5f, COLOR_TEXT);
        LinearLayout.LayoutParams dayMatrixParams = wrapParams();
        dayMatrixParams.topMargin = dp(12);
        content.addView(dayMatrix, dayMatrixParams);

        // streak
        LinearLayout streakRow = new LinearLayout(this);
        streakRow.setOrientation(LinearLayout.HORIZONTAL);
        streakRow.setGravity(Gravity.CENTER_VERTICAL);
        streakRow.addView(labelText(streakCount() + " DAY STREAK", 10, COLOR_MUTED));
        View streakSpacer = new View(this);
        streakRow.addView(streakSpacer, new LinearLayout.LayoutParams(0, 1, 1f));
        streakRow.addView(new StreakDotsView(this, openedDays()));
        LinearLayout.LayoutParams streakParams = wrapParams();
        streakParams.topMargin = dp(14);
        streakParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        content.addView(streakRow, streakParams);

        // tabs
        LinearLayout tabRow = new LinearLayout(this);
        tabRow.setOrientation(LinearLayout.HORIZONTAL);
        todayTabButton = pillButton("TODAY", true);
        todayTabButton.setOnClickListener(v -> selectTab(false));
        historyTabButton = pillButton("HISTORY", false);
        historyTabButton.setOnClickListener(v -> selectTab(true));
        LinearLayout.LayoutParams todayTabParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        todayTabParams.rightMargin = dp(5);
        LinearLayout.LayoutParams historyTabParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        historyTabParams.leftMargin = dp(5);
        tabRow.addView(todayTabButton, todayTabParams);
        tabRow.addView(historyTabButton, historyTabParams);
        LinearLayout.LayoutParams tabRowParams = wrapParams();
        tabRowParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        tabRowParams.topMargin = dp(26);
        content.addView(tabRow, tabRowParams);

        // today section
        todaySection = new LinearLayout(this);
        todaySection.setOrientation(LinearLayout.VERTICAL);
        content.addView(todaySection, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        buildTodaySection(now);

        // history section
        historySection = new LinearLayout(this);
        historySection.setOrientation(LinearLayout.VERTICAL);
        historySection.setVisibility(View.GONE);
        content.addView(historySection, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        historyContainer = new LinearLayout(this);
        historyContainer.setOrientation(LinearLayout.VERTICAL);
        historySection.addView(historyContainer, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        historyEmpty = labelText("NO HISTORY YET", 10, COLOR_FAINT);
        LinearLayout.LayoutParams historyEmptyParams = wrapParams();
        historyEmptyParams.topMargin = dp(24);
        historySection.addView(historyEmpty, historyEmptyParams);

        // footer
        String footerHead = "IN YOUR POWER: ";
        String footerRed = "OPINION, AIM, DESIRE, AVERSION.";
        SpannableString footerLine = new SpannableString(footerHead + footerRed);
        footerLine.setSpan(new ForegroundColorSpan(COLOR_RED), footerHead.length(),
                footerLine.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView footerTop = labelText("", 9, COLOR_FAINT);
        footerTop.setText(footerLine);
        TextView footerBottom = labelText("NOT IN YOUR POWER: EVERYTHING ELSE.", 9, COLOR_FAINT);
        LinearLayout.LayoutParams footerTopParams = wrapParams();
        footerTopParams.topMargin = dp(40);
        content.addView(footerTop, footerTopParams);
        LinearLayout.LayoutParams footerBottomParams = wrapParams();
        footerBottomParams.topMargin = dp(6);
        content.addView(footerBottom, footerBottomParams);

        setContentView(root);
    }

    private void buildTodaySection(Calendar now) {
        String[] quote = Quotes.today();

        DotMatrixView quoteMark = new DotMatrixView(this, "\"", 3f, COLOR_RED);
        LinearLayout.LayoutParams quoteMarkParams = wrapParams();
        quoteMarkParams.topMargin = dp(30);
        todaySection.addView(quoteMark, quoteMarkParams);

        TextView quoteText = new TextView(this);
        quoteText.setText(quote[0]);
        quoteText.setTextSize(20);
        quoteText.setTextColor(COLOR_TEXT);
        quoteText.setTypeface(Typeface.MONOSPACE);
        quoteText.setLineSpacing(0, 1.35f);
        LinearLayout.LayoutParams quoteParams = wrapParams();
        quoteParams.topMargin = dp(16);
        todaySection.addView(quoteText, quoteParams);

        LinearLayout attribution = new LinearLayout(this);
        attribution.setOrientation(LinearLayout.HORIZONTAL);
        attribution.setGravity(Gravity.CENTER_VERTICAL);
        View dash = new View(this);
        dash.setBackgroundColor(COLOR_RED);
        LinearLayout.LayoutParams dashParams = new LinearLayout.LayoutParams(dp(18), dp(1));
        dashParams.rightMargin = dp(10);
        attribution.addView(dash, dashParams);
        attribution.addView(labelText(quote[1].toUpperCase(Locale.US), 11, COLOR_TEXT));
        TextView source = labelText(quote[2], 9, COLOR_MUTED);
        LinearLayout.LayoutParams sourceParams = wrapParams();
        sourceParams.leftMargin = dp(10);
        attribution.addView(source, sourceParams);
        LinearLayout.LayoutParams attributionParams = wrapParams();
        attributionParams.topMargin = dp(20);
        todaySection.addView(attribution, attributionParams);

        todaySection.addView(divider(), dividerParams());

        // friction sorter
        todaySection.addView(labelText("TODAY'S FRICTION. NAME IT, THEN SORT IT.", 10, COLOR_MUTED));

        frictionInput = new EditText(this);
        frictionInput.setHint("what is on your mind");
        frictionInput.setHintTextColor(COLOR_FAINT);
        frictionInput.setTextColor(COLOR_TEXT);
        frictionInput.setTextSize(15);
        frictionInput.setTypeface(Typeface.MONOSPACE);
        frictionInput.setMaxLines(3);
        frictionInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_ENTRY_LENGTH)});
        frictionInput.setPadding(dp(14), dp(14), dp(14), dp(14));
        GradientDrawable inputShape = new GradientDrawable();
        inputShape.setColor(Color.TRANSPARENT);
        inputShape.setStroke(dp(1), COLOR_PANEL_STROKE);
        inputShape.setCornerRadius(dp(2));
        frictionInput.setBackground(inputShape);
        LinearLayout.LayoutParams inputParams = wrapParams();
        inputParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        inputParams.topMargin = dp(14);
        todaySection.addView(frictionInput, inputParams);

        LinearLayout buttonRow = new LinearLayout(this);
        buttonRow.setOrientation(LinearLayout.HORIZONTAL);
        Button inButton = pillButton("IN MY CONTROL", true);
        inButton.setOnClickListener(v -> addEntry("in"));
        Button outButton = redPillButton("NOT MINE, RELEASE");
        outButton.setOnClickListener(v -> addEntry("out"));
        LinearLayout.LayoutParams inParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        inParams.rightMargin = dp(5);
        LinearLayout.LayoutParams outParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        outParams.leftMargin = dp(5);
        buttonRow.addView(inButton, inParams);
        buttonRow.addView(outButton, outParams);
        LinearLayout.LayoutParams buttonRowParams = wrapParams();
        buttonRowParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        buttonRowParams.topMargin = dp(10);
        todaySection.addView(buttonRow, buttonRowParams);

        entriesContainer = new LinearLayout(this);
        entriesContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams entriesParams = wrapParams();
        entriesParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        entriesParams.topMargin = dp(18);
        todaySection.addView(entriesContainer, entriesParams);

        emptyText = labelText("NOTHING SORTED YET TODAY", 10, COLOR_FAINT);
        LinearLayout.LayoutParams emptyParams = wrapParams();
        emptyParams.topMargin = dp(8);
        todaySection.addView(emptyText, emptyParams);

        todaySection.addView(divider(), dividerParams());

        // morning quote toggle
        LinearLayout morningRow = new LinearLayout(this);
        morningRow.setOrientation(LinearLayout.HORIZONTAL);
        morningRow.setGravity(Gravity.CENTER_VERTICAL);
        morningRow.addView(labelText("MORNING QUOTE // 08:00", 10, COLOR_MUTED));
        View morningSpacer = new View(this);
        morningRow.addView(morningSpacer, new LinearLayout.LayoutParams(0, 1, 1f));
        morningSwitch = new Switch(this);
        morningSwitch.setChecked(prefs.getBoolean(MORNING_QUOTE_KEY, false));
        morningSwitch.setOnCheckedChangeListener((v, checked) -> handleMorningToggle(checked));
        morningRow.addView(morningSwitch, wrapParams());
        LinearLayout.LayoutParams morningParams = wrapParams();
        morningParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        todaySection.addView(morningRow, morningParams);
    }

    private void selectTab(boolean history) {
        historyVisible = history;
        todaySection.setVisibility(history ? View.GONE : View.VISIBLE);
        historySection.setVisibility(history ? View.VISIBLE : View.GONE);
        stylePill(todayTabButton, !history);
        stylePill(historyTabButton, history);
        if (history) {
            hideKeyboard();
            renderHistory();
        }
    }

    // ---------- entries ----------

    private void addEntry(String bucket) {
        String text = frictionInput.getText().toString().trim();
        if (text.isEmpty()) {
            frictionInput.requestFocus();
            return;
        }
        JSONArray entries = loadEntries(todayKey);
        try {
            JSONObject entry = new JSONObject();
            entry.put("text", text);
            entry.put("bucket", bucket);
            entry.put("done", false);
            entries.put(entry);
        } catch (JSONException e) {
            return;
        }
        saveTodayEntries(entries);
        frictionInput.setText("");
        hideKeyboard();
        renderEntries();
    }

    private void renderEntries() {
        JSONArray entries = loadEntries(todayKey);
        entriesContainer.removeAllViews();
        emptyText.setVisibility(entries.length() == 0 ? View.VISIBLE : View.GONE);

        for (int i = 0; i < entries.length(); i++) {
            JSONObject entry = entries.optJSONObject(i);
            if (entry == null) {
                continue;
            }
            entriesContainer.addView(entryRow(entry, i), new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            entriesContainer.addView(divider(), new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dp(1)));
        }
    }

    private void renderHistory() {
        historyContainer.removeAllViews();
        List<String> keys = new ArrayList<>();
        for (String key : prefs.getAll().keySet()) {
            if (key.startsWith(ENTRIES_KEY_PREFIX)) {
                keys.add(key.substring(ENTRIES_KEY_PREFIX.length()));
            }
        }
        Collections.sort(keys, Collections.reverseOrder());

        int shownDays = 0;
        for (String key : keys) {
            JSONArray entries = loadEntries(key);
            if (entries.length() == 0) {
                continue;
            }

            int inControl = 0;
            int released = 0;
            for (int i = 0; i < entries.length(); i++) {
                JSONObject entry = entries.optJSONObject(i);
                if (entry == null) {
                    continue;
                }
                if ("in".equals(entry.optString("bucket"))) {
                    inControl++;
                } else {
                    released++;
                }
            }

            LinearLayout dayHeader = new LinearLayout(this);
            dayHeader.setOrientation(LinearLayout.HORIZONTAL);
            dayHeader.setGravity(Gravity.CENTER_VERTICAL);
            dayHeader.addView(labelText(prettyDay(key), 10, COLOR_TEXT));
            View headerSpacer = new View(this);
            dayHeader.addView(headerSpacer, new LinearLayout.LayoutParams(0, 1, 1f));
            dayHeader.addView(labelText(
                    inControl + " IN CONTROL · " + released + " RELEASED", 9, COLOR_MUTED));
            LinearLayout.LayoutParams dayHeaderParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            dayHeaderParams.topMargin = dp(shownDays == 0 ? 30 : 26);
            historyContainer.addView(dayHeader, dayHeaderParams);

            for (int i = 0; i < entries.length(); i++) {
                JSONObject entry = entries.optJSONObject(i);
                if (entry == null) {
                    continue;
                }
                historyContainer.addView(entryRow(entry, -1), new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                historyContainer.addView(divider(), new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dp(1)));
            }
            shownDays++;
        }

        historyEmpty.setVisibility(shownDays == 0 ? View.VISIBLE : View.GONE);
    }

    /** Builds an entry row. A non-negative index makes the row interactive (today only). */
    private LinearLayout entryRow(JSONObject entry, int index) {
        boolean interactive = index >= 0;
        boolean inControl = "in".equals(entry.optString("bucket"));
        boolean done = entry.optBoolean("done");

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(12), 0, dp(12));

        if (inControl) {
            View tick = new View(this);
            GradientDrawable tickShape = new GradientDrawable();
            tickShape.setCornerRadius(dp(2));
            if (done) {
                tickShape.setColor(COLOR_RED);
            } else {
                tickShape.setColor(Color.TRANSPARENT);
                tickShape.setStroke(dp(1), COLOR_MUTED);
            }
            tick.setBackground(tickShape);
            if (interactive) {
                tick.setOnClickListener(v -> toggleDone(index));
            }
            LinearLayout.LayoutParams tickParams = new LinearLayout.LayoutParams(dp(16), dp(16));
            tickParams.rightMargin = dp(12);
            row.addView(tick, tickParams);
        }

        TextView text = new TextView(this);
        text.setText(entry.optString("text"));
        text.setTextSize(14);
        text.setTypeface(Typeface.MONOSPACE);
        if (inControl) {
            text.setTextColor(done ? COLOR_MUTED : COLOR_TEXT);
        } else {
            text.setTextColor(COLOR_MUTED);
            text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        row.addView(text, new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        if (!inControl) {
            TextView tag = labelText("RELEASED", 8, COLOR_RED);
            LinearLayout.LayoutParams tagParams = wrapParams();
            tagParams.leftMargin = dp(10);
            row.addView(tag, tagParams);
        }

        if (interactive) {
            TextView delete = new TextView(this);
            delete.setText("×");
            delete.setTextSize(16);
            delete.setTextColor(COLOR_FAINT);
            delete.setPadding(dp(10), dp(2), dp(2), dp(2));
            delete.setOnClickListener(v -> deleteEntry(index));
            row.addView(delete, wrapParams());
        }

        return row;
    }

    private void toggleDone(int index) {
        JSONArray entries = loadEntries(todayKey);
        JSONObject entry = entries.optJSONObject(index);
        if (entry == null) {
            return;
        }
        try {
            entry.put("done", !entry.optBoolean("done"));
        } catch (JSONException e) {
            return;
        }
        saveTodayEntries(entries);
        renderEntries();
    }

    private void deleteEntry(int index) {
        JSONArray entries = loadEntries(todayKey);
        JSONArray kept = new JSONArray();
        for (int i = 0; i < entries.length(); i++) {
            if (i != index) {
                kept.put(entries.optJSONObject(i));
            }
        }
        saveTodayEntries(kept);
        renderEntries();
    }

    // ---------- morning quote notification ----------

    private void handleMorningToggle(boolean checked) {
        if (checked) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
                return;
            }
            prefs.edit().putBoolean(MORNING_QUOTE_KEY, true).apply();
            QuoteReceiver.schedule(this);
        } else {
            prefs.edit().putBoolean(MORNING_QUOTE_KEY, false).apply();
            QuoteReceiver.cancel(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_NOTIFICATION_PERMISSION) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            prefs.edit().putBoolean(MORNING_QUOTE_KEY, true).apply();
            QuoteReceiver.schedule(this);
        } else {
            morningSwitch.setChecked(false);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && frictionInput != null) {
            imm.hideSoftInputFromWindow(frictionInput.getWindowToken(), 0);
        }
    }

    // ---------- view helpers ----------

    private TextView labelText(String text, int sizeSp, int color) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextSize(sizeSp);
        view.setTextColor(color);
        view.setTypeface(Typeface.MONOSPACE);
        view.setLetterSpacing(0.2f);
        return view;
    }

    private Button pillButton(String text, boolean active) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(10);
        button.setLetterSpacing(0.15f);
        button.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        button.setAllCaps(true);
        button.setPadding(dp(6), dp(14), dp(6), dp(14));
        button.setStateListAnimator(null);
        stylePill(button, active);
        return button;
    }

    private void stylePill(Button button, boolean active) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(dp(999));
        if (active) {
            shape.setColor(COLOR_TEXT);
            button.setTextColor(COLOR_BACKGROUND);
        } else {
            shape.setColor(Color.TRANSPARENT);
            shape.setStroke(dp(1), COLOR_PANEL_STROKE);
            button.setTextColor(COLOR_MUTED);
        }
        button.setBackground(shape);
    }

    private Button redPillButton(String text) {
        Button button = pillButton(text, false);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(dp(999));
        shape.setColor(Color.TRANSPARENT);
        shape.setStroke(dp(1), COLOR_RED);
        button.setBackground(shape);
        button.setTextColor(COLOR_RED);
        return button;
    }

    private View divider() {
        return new DottedLineView(this);
    }

    private LinearLayout.LayoutParams dividerParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(1));
        params.topMargin = dp(26);
        params.bottomMargin = dp(26);
        return params;
    }

    private static LinearLayout.LayoutParams wrapParams() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private int dp(float value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    // ---------- custom views ----------

    private static final class DotGridView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final float spacing;
        private final float radius;

        DotGridView(Context context) {
            super(context);
            float density = context.getResources().getDisplayMetrics().density;
            spacing = 22 * density;
            radius = 0.8f * density;
            paint.setColor(COLOR_GRID_DOT);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            for (float y = spacing / 2; y < getHeight(); y += spacing) {
                for (float x = spacing / 2; x < getWidth(); x += spacing) {
                    canvas.drawCircle(x, y, radius, paint);
                }
            }
        }
    }

    private static final class DotMatrixView extends View {
        private final String text;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final float dot;
        private final float gap;
        private final float charGap;

        DotMatrixView(Context context, String text, float dotDp, int color) {
            super(context);
            this.text = text.toUpperCase(Locale.US);
            float density = context.getResources().getDisplayMetrics().density;
            dot = dotDp * density;
            gap = dot * 0.5f;
            charGap = dot * 1.4f;
            paint.setColor(color);
        }

        private float charWidth() {
            return 5 * dot + 4 * gap;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int count = text.length();
            float width = count * charWidth() + Math.max(0, count - 1) * charGap;
            float height = 7 * dot + 6 * gap;
            setMeasuredDimension((int) Math.ceil(width), (int) Math.ceil(height));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            float x = 0;
            for (int i = 0; i < text.length(); i++) {
                String[] glyph = DOT_FONT.get(text.charAt(i));
                if (glyph == null) {
                    glyph = DOT_FONT.get(' ');
                }
                for (int row = 0; row < 7; row++) {
                    for (int col = 0; col < 5; col++) {
                        if (glyph[row].charAt(col) == '1') {
                            float cx = x + col * (dot + gap) + dot / 2;
                            float cy = row * (dot + gap) + dot / 2;
                            canvas.drawCircle(cx, cy, dot / 2, paint);
                        }
                    }
                }
                x += charWidth() + charGap;
            }
        }
    }

    private static final class StreakDotsView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final boolean[] hits = new boolean[STREAK_DOT_COUNT];
        private final float dot;
        private final float gap;

        StreakDotsView(Context context, Set<String> opened) {
            super(context);
            float density = context.getResources().getDisplayMetrics().density;
            dot = 6 * density;
            gap = 5 * density;
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -(STREAK_DOT_COUNT - 1));
            for (int i = 0; i < STREAK_DOT_COUNT; i++) {
                hits[i] = opened.contains(dayKey(c));
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            float width = STREAK_DOT_COUNT * dot + (STREAK_DOT_COUNT - 1) * gap;
            setMeasuredDimension((int) Math.ceil(width), (int) Math.ceil(dot));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            for (int i = 0; i < STREAK_DOT_COUNT; i++) {
                if (i == STREAK_DOT_COUNT - 1) {
                    paint.setColor(COLOR_RED);
                } else {
                    paint.setColor(hits[i] ? COLOR_TEXT : Color.rgb(36, 38, 42));
                }
                float cx = i * (dot + gap) + dot / 2;
                canvas.drawCircle(cx, dot / 2, dot / 2, paint);
            }
        }
    }

    private static final class DottedLineView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final float radius;
        private final float spacing;

        DottedLineView(Context context) {
            super(context);
            float density = context.getResources().getDisplayMetrics().density;
            radius = 0.8f * density;
            spacing = 6 * density;
            paint.setColor(COLOR_PANEL_STROKE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            float cy = getHeight() / 2f;
            for (float x = radius; x < getWidth(); x += spacing) {
                canvas.drawCircle(x, cy, radius, paint);
            }
        }
    }
}
