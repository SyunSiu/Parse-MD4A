package com.asha.vrlib.plugins;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hzqiujiadi on 16/7/22.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class SharkPluginManager {

    private static final String TAG = "SharkPluginManager";

    private List<SharkAbsPlugin> mList;

    public SharkPluginManager() {
        mList = new CopyOnWriteArrayList<>();
    }

    public void add(SharkAbsPlugin plugin){
        mList.add(plugin);
    }

    public List<SharkAbsPlugin> getPlugins() {
        return mList;
    }

    public void remove(SharkAbsPlugin plugin) {
        if (plugin != null){
            mList.remove(plugin);
        }
    }

    public void removeAll() {
        Iterator<SharkAbsPlugin> iterator = mList.iterator();
        while (iterator.hasNext()){
            SharkAbsPlugin plugin = iterator.next();
            if (plugin.removable()){
                mList.remove(plugin);
            }
        }
    }
}
