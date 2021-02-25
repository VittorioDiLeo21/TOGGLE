package it.polito.toggle.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;


import java.lang.reflect.Field;
import java.nio.channels.GatheringByteChannel;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.widget.ListViewCompat.scrollListBy;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

public class ScrollHandler {
    private static final int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    private static int m_nItemCount;
    private static int[] m_nItemOffY;
    private static int[] m_nItemOffX;
    private static Activity[] activities = new Activity[1];

    public static int getItemHeightOfListView(AdapterView adapterView, int nItems){
        Adapter adapter = adapterView.getAdapter();

        int elementHeight = 0;
        for(int i = 0; i < nItems; i++) {
            View child = adapter.getView(i,null,adapterView);
            child.measure(UNBOUNDED,UNBOUNDED);
            elementHeight+=child.getMeasuredHeight();
        }
        return elementHeight;
    }

    public static String getAdapterViewPosFrom(int offsetY, int offsetX, AdapterView av){
        int pos = 0,posDisplayed = 0;
        for(; pos < m_nItemCount; pos++){
            if(m_nItemOffY[pos] >= offsetY &&
                m_nItemOffX[pos] >= offsetX)
                break;
        }

        for(int i = av.getFirstVisiblePosition(); i <= av.getLastVisiblePosition(); i++,posDisplayed++){
            if(i==pos)
                break;
        }

        View child = av.getChildAt(posDisplayed);
        int[] coords = new int[2];
        child.getLocationInWindow(coords);
        return "["+coords[0]+","+coords[1]+"]";
    }

    public static String getScrollableCoords(View v){
        int[] coords = new int[2];
        v.getLocationInWindow(coords);
        return "["+coords[0]+","+coords[1]+"]";
    }

    public static void scroll(final AdapterView list, final int y){
        try {
            runOnUiThread(new Thread(new Runnable() {
                @Override
                public void run() {
                    scrollListBy((ListView)list,y);
                }
            }));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static int getListItemsHeight(ListView v){
        ListAdapter adapter = v.getAdapter();
        m_nItemCount = adapter.getCount();
        int height = 0;
        int i = 0;
        m_nItemOffY = new int[m_nItemCount];
        for(i = 0; i < m_nItemCount; i++) {
            View view = adapter.getView(i,null,v);
            view.measure(UNBOUNDED,UNBOUNDED);
            m_nItemOffY[i] = height;
            height+= view.getMeasuredHeight();
        }
        return height;
    }

    public static int getItemsHeight(AdapterView v){
        Adapter adapter = v.getAdapter();
        m_nItemCount = adapter.getCount();
        int height = 0;
        int width = 0;
        int numX = 1;
        int numY = 1;
        int colW = 0;
        int pos = 0;
        if(v instanceof GridView) {
            numX = ((GridView) v).getNumColumns();
            colW = ((GridView) v).getColumnWidth();
            numY = m_nItemCount/numX;
            if(m_nItemCount%numX > 0)
                numY++;
        } else if( v instanceof Gallery){
            numX = m_nItemCount;
            v.measure(UNBOUNDED,UNBOUNDED);
            colW = ((Gallery) v).getWidth();
            numY = 1;
        } else if( v instanceof ListView || v instanceof Spinner){
            numX = 1;
            v.measure(UNBOUNDED,UNBOUNDED);
            colW = v.getWidth();
            numY = m_nItemCount;
        }
        int i = 0;
        int j = 0;
        m_nItemOffY = new int[m_nItemCount];
        m_nItemOffX = new int[m_nItemCount];
        View view;

        for(i = 0; i < numY && pos < m_nItemCount; i++){
            for(j = 0; j < numX && pos < m_nItemCount; j++, pos++){
                view = adapter.getView(pos,null,v);
                view.measure(UNBOUNDED,UNBOUNDED);
                /*int left = view.getLeft();
                int top = view.getTop();
                int coords[] = new int[2];
                view.getLocationInWindow(coords);*/
                m_nItemOffY[pos] = height;
                m_nItemOffX[pos] = width;
                //width+= view.getMeasuredWidth();
                width+= colW;
                if((j == (numX-1)) ||               // if I'm going to start a new row
                        (pos == m_nItemCount-1)){   // or if this was the last element
                    height+= view.getMeasuredHeight();
                }
            }
            width = 0;
        }

        /*for(i = 0; i < m_nItemCount;) {
            for(j = i%numX; j < numX && i < m_nItemCount; i++, j++){
                view = adapter.getView(i,null,v);
                view.measure(UNBOUNDED,UNBOUNDED);
                m_nItemOffY[i] = height;
                m_nItemOffX[i] = width;
                width+= view.getMeasuredWidth();
                if((j == (numX-1)) || (i == m_nItemCount)){
                    height+= view.getMeasuredHeight();
                }
            }
            width = 0;
        }*/
        return height;
    }

    public static int getListActualOffsetFromTop(ListView v ){
        int pos = v.getFirstVisiblePosition();
        View view = v.getChildAt(0);
        int nItemY = view.getTop();
        return m_nItemOffY[pos] - nItemY;
    }

    public static int getActualOffsetFromTop(AdapterView v ){
        int pos = v.getFirstVisiblePosition();
        View view = v.getChildAt(0);
        int nItemY = view.getTop();
        return m_nItemOffY[pos] - nItemY;
    }

    public static int getActualOffsetFromStart(AdapterView v){
        int pos = v.getFirstVisiblePosition();
        View view = v.getChildAt(0);
        int nItemX = view.getLeft();
        return m_nItemOffX[pos] - nItemX;
    }
//todo
    public static int getActualOffsetFrom(AdapterView v, int pos ){
        int actual = v.getFirstVisiblePosition();
        View view = v.getChildAt(pos);
        int nItemY = view.getTop();
        return m_nItemOffY[actual] - nItemY;
    }

    public static int getSingleItemHeightIn(AdapterView v,boolean first){
        int pos = 0;
        if(first)
            pos = v.getFirstVisiblePosition();
        else
            pos = v.getLastVisiblePosition();
        View view = v.getAdapter().getView(pos,null,v);
        view.measure(UNBOUNDED,UNBOUNDED);
        return view.getMeasuredHeight();
    }

    public static int getSingleItemWidthIn(AdapterView v,boolean first){
        int pos = 0;
        if(first)
            pos = v.getFirstVisiblePosition();
        else
            pos = v.getLastVisiblePosition();
        View view = v.getAdapter().getView(pos,null,v);
        view.measure(UNBOUNDED,UNBOUNDED);
        return view.getMeasuredWidth();
    }

    public static int[] getListFirstVisiblePx(ListView v){
        int pos = v.getFirstVisiblePosition();
        View view = v.getAdapter().getView(pos,null,v);
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        return loc;
    }

    public static Activity getCurrentActivity() {
        return activities[0];
    }

    public static void monitorCurrentActivity(ActivityTestRule rule) {
        rule.getActivity().getApplication()
                .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) { }

                    @Override
                    public void onActivityStarted(final Activity activity) { }

                    @Override
                    public void onActivityResumed(final Activity activity) {
                        activities[0] = activity;
                    }

                    @Override
                    public void onActivityPaused(final Activity activity) { }

                    @Override
                    public void onActivityStopped(final Activity activity) { }

                    @Override
                    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) { }

                    @Override
                    public void onActivityDestroyed(final Activity activity) { }
                });
    }

    public static int[] getFirstVisiblePx(AdapterView v){
        int pos = v.getFirstVisiblePosition();
        View view = v.getAdapter().getView(pos,null,v);
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        return loc;
    }

    public static void scrollListToY(final ListView v, final int scrollY){
        try {
            runOnUiThread(new Thread(new Runnable() {
                @Override
                public void run() {
                    int i,off;
                    for(i = 0; i < m_nItemCount;i++){
                        off = m_nItemOffY[i] - scrollY;
                        if(off >= 0){
                            v.setSelectionFromTop(i,off);
                            break;
                        }
                    }
                }
            }));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static AdapterView findAdapterView(View v){
        if(v instanceof AdapterView)
            return (AdapterView)v;
        else {
            ViewParent vg = v.getParent();
            do {
                if(vg instanceof AdapterView)
                    return (AdapterView)vg;
            } while((vg = vg.getParent()) != null);
            return null;
        }
    }

    public static boolean isScrollable(View v) {
        return v instanceof ScrollView ||
                v instanceof HorizontalScrollView ||
                v instanceof ListView /*|| TODO NO ESPRESSO SUPPORT!
                v instanceof NestedScrollView*/;
    }

    public static String getScrollableClass(View v){
        if(v instanceof ScrollView)
            return "ScrollView";
        else if(v instanceof HorizontalScrollView)
            return "HorizontalScrollView";
        else if(v instanceof ListView)
            return "ListView";
        else
            return "";
    }

    public static View findScrollableFromText(Activity activity,String text,boolean isHint) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        WindowManager windowManager = (WindowManager)activity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Field globalField = Class.forName("android.view.WindowManagerImpl").getDeclaredField("mGlobal");
        globalField.setAccessible(true);
        Object globalFieldValue = globalField.get(windowManager);
        ArrayList<View> views = (ArrayList<View>) TOGGLETools.getViews(globalFieldValue);
        ViewGroup v = (ViewGroup)views.get(views.size()-1);

        int scrollId = findScrollableInHierarchy(v,text,isHint);
        if(scrollId == -1)
            return null;
        return activity.findViewById(scrollId);
    }

    /**
     * If return value == -1 --> all the child views do not contain a view with given id
     * if return value == id --> one of the child views is actually the required view and we are going up to find a scrollable parent
     * if return value != -1 && != id --> we found the required scrollable view, we are leaving this method
     * @param v
     * @param txt
     * @param isHint
     * @return
     */
    public static int findScrollableInHierarchy(View v,String txt,boolean isHint){
        if(v == null)
            return -1;
        if(!(v instanceof ViewGroup)){
            if(v instanceof TextView){
                if(isHint)
                    return ((TextView)v).getHint().equals(txt) ? v.getId() : -1;
                else
                    return ((TextView)v).getText().equals(txt) ? v.getId() : -1;
            } else {
                return -1;
            }
        }
        ViewGroup vg = (ViewGroup) v;
        if(vg.getChildCount() == 0){
            return -1;
        }
        for(int i = 0 ; i < vg.getChildCount(); i++){
            int res = findScrollableInHierarchy((View)vg.getChildAt(i),txt,isHint);
            if(res != -1) { // view found
                if(isScrollable(vg.getChildAt(i)))
                    return vg.getChildAt(i).getId();
                return res;
            }
        }
        return -1;
    }

    public static View getScrollableParent(View v){
        if(isScrollable(v)){
            return v;
        } else {
            ViewParent vg = v.getParent();
            do {
                if(isScrollable((View)vg))
                    return (View)vg;
            } while((vg = vg.getParent()) != null);
            return null;
        }
    }

    public static int getScrollXFromScrollable(View scrollable) {
        if(scrollable == null)
            return 0;
        if(scrollable instanceof ScrollView){
            return ((ScrollView) scrollable).getScrollX();
        }
        if(scrollable instanceof HorizontalScrollView){
            return ((HorizontalScrollView) scrollable).getScrollX();
        }
        if(scrollable instanceof ListView){
            return ((ListView) scrollable).getScrollX();
        }
        return 0;
    }

    public static int getScrollYFromScrollable(View scrollable) {
        if(scrollable == null)
            return 0;
        if(scrollable instanceof ScrollView){
            return ((ScrollView) scrollable).getScrollY();
        }
        if(scrollable instanceof HorizontalScrollView){
            return ((HorizontalScrollView) scrollable).getScrollY();
        }
        if(scrollable instanceof ListView){
            return ((ListView) scrollable).getScrollY();
        }
        return 0;
    }
}
