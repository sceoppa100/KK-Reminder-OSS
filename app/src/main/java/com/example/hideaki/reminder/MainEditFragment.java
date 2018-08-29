package com.example.hideaki.reminder;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.Calendar;

public class MainEditFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener, DialogInterface.OnClickListener {

  public static final String ITEM = "ITEM";

  private PreferenceScreen rootPreferenceScreen;
  private PreferenceScreen tag;
  private PreferenceScreen interval;
  private EditTextPreference detail;
  private PreferenceCategory notes_category;
  private EditTextPreference notes;
  private PreferenceScreen day_repeat_item;
  private PreferenceScreen minute_repeat_item;
  static Item item;
  static String detail_str;
  static String notes_str;
  static Calendar final_cal;
  static DayRepeat dayRepeat;
  static MinuteRepeat minuteRepeat;
  static boolean is_edit;
  private MainActivity activity;

  public static MainEditFragment newInstance() {

    MainEditFragment fragment = new MainEditFragment();

    Item item = new Item();
    is_edit = false;
    detail_str = "";
    notes_str = "";
    dayRepeat = new DayRepeat();
    minuteRepeat = new MinuteRepeat();
    final_cal = Calendar.getInstance();
    Bundle args = new Bundle();
    args.putSerializable(ITEM, item);
    fragment.setArguments(args);

    return fragment;
  }

  public static MainEditFragment newInstance(Item item) {

    MainEditFragment fragment = new MainEditFragment();

    is_edit = true;
    detail_str = item.getDetail();
    notes_str = item.getNotes();
    dayRepeat = item.getDayRepeat().clone();
    minuteRepeat = item.getMinuteRepeat().clone();
    final_cal = (Calendar)item.getDate().clone();
    Bundle args = new Bundle();
    args.putSerializable(ITEM, item);
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public void onAttach(Context context) {

    super.onAttach(context);
    activity = (MainActivity)context;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.main_edit);
    setHasOptionsMenu(true);

    Bundle args = getArguments();
    item = (Item)args.getSerializable(ITEM);

    rootPreferenceScreen = getPreferenceScreen();
    detail = (EditTextPreference)findPreference("detail");
    detail.setTitle(detail_str);
    detail.setOnPreferenceChangeListener(this);
    tag =(PreferenceScreen)findPreference("tag");
    tag.setOnPreferenceClickListener(this);
    interval = (PreferenceScreen)findPreference("interval");
    interval.setOnPreferenceClickListener(this);
    day_repeat_item = (PreferenceScreen)findPreference("repeat_day_unit");
    day_repeat_item.setOnPreferenceClickListener(this);
    minute_repeat_item = (PreferenceScreen)findPreference("repeat_minute_unit");
    minute_repeat_item.setOnPreferenceClickListener(this);
    notes_category = (PreferenceCategory)findPreference("notes_category");
    notes = (EditTextPreference)findPreference("notes");
    notes.setTitle(notes_str);
    notes.setOnPreferenceChangeListener(this);

    if(activity.menuItem.getOrder() == 1) {
      rootPreferenceScreen.removeAll();
      rootPreferenceScreen.addPreference(detail);
      rootPreferenceScreen.addPreference(tag);
      rootPreferenceScreen.addPreference(notes_category);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View view = super.onCreateView(inflater, container, savedInstanceState);
    assert view != null;

    view.setBackgroundColor(getResources().getColor(android.R.color.background_light));

    Toolbar toolbar = activity.findViewById(R.id.toolbar_layout);
    activity.setSupportActionBar(toolbar);
    ActionBar actionBar = activity.getSupportActionBar();
    assert actionBar != null;

    activity.drawerToggle.setDrawerIndicatorEnabled(false);
    actionBar.setHomeAsUpIndicator(activity.upArrow);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle(R.string.edit);

    if(dayRepeat.getLabel() == null) day_repeat_item.setSummary(R.string.none);
    else day_repeat_item.setSummary(dayRepeat.getLabel());

    if(minuteRepeat.getLabel() == null) minute_repeat_item.setSummary(R.string.none);
    else minute_repeat_item.setSummary(minuteRepeat.getLabel());

    return view;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.main_edit_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch(item.getItemId()) {
      case R.id.done:
        if(is_edit && MainEditFragment.item.getDate().getTimeInMillis() != final_cal.getTimeInMillis()
            && (MainEditFragment.dayRepeat.getSetted() != 0 || MainEditFragment.minuteRepeat.getWhich_setted() != 0)) {
          new AlertDialog.Builder(getActivity())
              .setMessage(R.string.repeat_conflict_dialog_message)
              .setPositiveButton(R.string.yes, this)
              .setNegativeButton(R.string.no, this)
              .setNeutralButton(R.string.cancel, this)
              .show();
        }
        else {
          registerItem();
        }
        return true;
      case android.R.id.home:
        getFragmentManager().popBackStack();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public boolean onPreferenceClick(Preference preference) {

    switch(preference.getKey()) {
      case "tag":
        transitionFragment(TagEditFragment.newInstance());
        return true;
      case "interval":
        transitionFragment(IntervalEditFragment.newInstance());
        return true;
      case "repeat_day_unit":
        transitionFragment(DayRepeatEditFragment.newInstance());
        return true;
      case "repeat_minute_unit":
        transitionFragment(MinuteRepeatEditFragment.newInstance());
        return true;
    }
    return false;
  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object newValue) {

    switch(preference.getKey()) {
      case "detail":
        detail_str = (String)newValue;
        detail.setTitle((String)newValue);
        return true;
      case "notes":
        notes_str = (String)newValue;
        notes.setTitle((String)newValue);
        return true;
    }
    return false;
  }

  private void transitionFragment(PreferenceFragment next) {

    getFragmentManager()
        .beginTransaction()
        .replace(R.id.content, next)
        .addToBackStack(null)
        .commit();
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {

    switch(which) {
      case DialogInterface.BUTTON_POSITIVE:
        if(item.getTime_altered() == 0) {
          item.setOrg_date((Calendar)item.getDate().clone());
        }
        long altered_time = (final_cal.getTimeInMillis()
            - item.getDate().getTimeInMillis()) / (1000 * 60);
        item.addTime_altered(altered_time * 60 * 1000);

        registerItem();
        break;
      case DialogInterface.BUTTON_NEGATIVE:
        item.setOrg_date((Calendar)final_cal.clone());
        item.setTime_altered(0);

        registerItem();
        break;
      case DialogInterface.BUTTON_NEUTRAL:
        break;
    }
  }
  
  private void registerItem() {

    getFragmentManager().popBackStack();

    item.setDetail(detail_str);
    item.setDate((Calendar)final_cal.clone());
    item.setNotes(notes_str);

    //dayRepeatの登録
    if(dayRepeat.getSetted() != 0) {
      if(dayRepeat.getSetted() == 1) dayRepeat.dayClear();
      else if(dayRepeat.getSetted() == (1 << 1)) dayRepeat.weekClear();
      else if(dayRepeat.getSetted() == (1 << 2)) {
        if(dayRepeat.isDays_of_month_setted()) dayRepeat.daysOfMonthClear();
        else dayRepeat.onTheMonthClear();
      }
      else if(dayRepeat.getSetted() == (1 << 3)) dayRepeat.yearClear();
    }
    else {
      dayRepeat.clear();
    }
    item.setDayRepeat(dayRepeat.clone());

    //minuteRepeatの登録
    if(minuteRepeat.getWhich_setted() != 0) {
      if(minuteRepeat.getWhich_setted() == 1) {
        minuteRepeat.setCount(minuteRepeat.getOrg_count());
        minuteRepeat.countClear();
      }
      else if(minuteRepeat.getWhich_setted() == (1 << 1)) {
        minuteRepeat.setDuration(minuteRepeat.getOrgDuration());
        minuteRepeat.durationClear();
      }
    }
    else {
      minuteRepeat.clear();
    }
    item.setMinuteRepeat(minuteRepeat.clone());

    item.setAlarm_stopped(false);

    //スケジュールなしリストの場合はリストのIDと順番をitemに登録する
    if(activity.menuItem.getOrder() == 1) {
      item.setWhich_list_belongs(
          activity.generalSettings.getNonScheduledList(activity.which_menu_open - 1).getId()
      );

      MyListAdapter.itemList.add(0, item);
      for(int i = 0; i < MyListAdapter.itemList.size(); i++) {
        MyListAdapter.itemList.get(i).setOrder(i);
      }
    }

    if(activity.isItemExists(item, MyDatabaseHelper.TODO_TABLE)) {
      if(activity.menuItem.getOrder() == 1) {
        activity.listAdapter.notifyDataSetChanged();
      }
      else {
        activity.expandableListAdapter.notifyDataSetChanged();
      }
      activity.updateDB(item, MyDatabaseHelper.TODO_TABLE);
    }
    else {
      if(activity.menuItem.getOrder() == 1) {
        activity.listAdapter.notifyDataSetChanged();
        activity.insertDB(item, MyDatabaseHelper.TODO_TABLE);
      }
      else {
        activity.addChildren(item, MyDatabaseHelper.TODO_TABLE);
      }
    }

    //データベースに挿入を行ったら、そのデータベースを端末暗号化ストレージへコピーする
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      Context direct_boot_context = getActivity().createDeviceProtectedStorageContext();
      direct_boot_context.moveDatabaseFrom(getActivity(), MyDatabaseHelper.TODO_TABLE);
    }

    activity.deleteAlarm(item);
    activity.setAlarm(item);
  }
}
