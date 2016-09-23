package com.wanbenyu.nerdlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 本钰 on 2016/9/20.
 */
public class NerdLauncherFragment extends ListFragment {
    private static final String TAG = "NerdLauncherFragment";

    private android.support.design.widget.FloatingActionButton mSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);


        Collections.sort(activities, new Comparator<ResolveInfo>() {
           public int compare(ResolveInfo a, ResolveInfo b) {
               PackageManager pm = getActivity().getPackageManager();
               return String.CASE_INSENSITIVE_ORDER.compare(
                       a.loadLabel(pm).toString(),
                       b.loadLabel(pm).toString()
               );
           }
        });


        ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(
                getActivity(), 0, activities) {
            public View getView(int pos, View convertView, ViewGroup parent) {
                PackageManager pm = getActivity().getPackageManager();

                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.layout_item, parent, false);
                }

                ResolveInfo ri = getItem(pos);

                ImageView icon = (ImageView)convertView.findViewById(R.id.app_icon);
                icon.setImageDrawable(ri.loadIcon(pm));

                TextView name = (TextView)convertView.findViewById(R.id.app_name);
                if(ri.loadLabel(pm) != null) name.setText(ri.loadLabel(pm));
                return convertView;
            }
        };

        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.root_list, parent, false);

        mSwitch = (android.support.design.widget.FloatingActionButton)v.findViewById(R.id.fab);
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TaskSwitchActivity.class);
                try {
                    startActivity(i);
                } catch (Exception err) {
                    Log.d(TAG, err.toString());
                }
            }
        });

        return v;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ResolveInfo resolveInfo = (ResolveInfo) l.getAdapter().getItem(position);
        ActivityInfo activityInfo = resolveInfo.activityInfo;

        if(activityInfo == null) return;

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
