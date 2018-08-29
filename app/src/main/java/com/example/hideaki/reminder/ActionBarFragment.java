package com.example.hideaki.reminder;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class ActionBarFragment extends Fragment {

  private MainActivity activity;

  public static ActionBarFragment newInstance() {

    return new ActionBarFragment();
  }

  @Override
  public void onAttach(Context context) {

    super.onAttach(context);
    activity = (MainActivity)context;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    this.setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    Toolbar toolbar = activity.findViewById(R.id.toolbar_layout);
    activity.setSupportActionBar(toolbar);
    activity.drawerToggle.setDrawerIndicatorEnabled(true);
    ActionBar actionBar = activity.getSupportActionBar();
    assert actionBar != null;

    actionBar.setTitle(R.string.app_name);
    return inflater.inflate(R.layout.fragment_search, container, false);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.reminder_menu, menu);

    //検索メニューの実装
    MenuItem search_item = menu.findItem(R.id.search_item);
    SearchView searchView = (SearchView)search_item.getActionView();

    searchView.setIconifiedByDefault(true);
    searchView.setSubmitButtonEnabled(false);

    //検索アイコン押下時の処理
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String text) {
        if(text == null || text.equals("")) {
          activity.expandableListView.clearTextFilter();
        }
        else {
          activity.expandableListView.setFilterText(text);
        }

        return false;
      }
    });

    //To-doメニューの実装
    MenuItem toggle_button = menu.findItem(R.id.toggle);
    ToggleButton button = (ToggleButton)toggle_button.getActionView();

    button.setTextOff(getResources().getString(R.string.todo));
    button.setTextOn(getResources().getString(R.string.done));
    button.setChecked(false);
    button.setBackgroundResource(R.drawable.toggle_button);

    //To-doメニューボタン押下時の処理
    button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
          //TODO: Doneデータベースからデータを読み込み、レイアウト表示をDone用に変える
        }
        else {
          //TODO: To-doデータベースからデータを読み込み、レイアウト表示を元に戻す。
        }
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch(item.getItemId()) {
      case R.id.add_item:
        activity.showMainEditFragment();
        return true;
    }
    return false;
  }
}
