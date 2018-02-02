package com.example.marinaangelovska.insights.Fragment;

import android.app.Fragment;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.marinaangelovska.insights.Adapters.CustomAppsAdapter;
import com.example.marinaangelovska.insights.Model.Application;
import com.example.marinaangelovska.insights.R;
import com.example.marinaangelovska.insights.Service.AppsService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by marinaangelovska on 2/2/18.
 */

public class AppsFragment extends Fragment {
    UsageStatsManager mUsageStatsManager;
    CustomAppsAdapter adapter;
    List<UsageStats> usageStatsList;
    AppsService appService;
    ArrayList<Application> appList;
    View view;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        return queryUsageStats;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_app, container, false);
        mUsageStatsManager = (UsageStatsManager) getActivity()
                .getSystemService(Context.USAGE_STATS_SERVICE);


        usageStatsList = getUsageStatistics(UsageStatsManager.INTERVAL_WEEKLY);
        appService = new AppsService(getActivity());
        try {
            appList = appService.getApps(usageStatsList);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        adapter=new CustomAppsAdapter(getActivity(), appList);

        ListView viewList=(ListView)view.findViewById (R.id.appsList);
        viewList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
