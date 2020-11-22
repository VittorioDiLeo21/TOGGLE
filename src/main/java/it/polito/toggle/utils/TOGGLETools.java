package it.polito.toggle.utils;


import android.Manifest;
import android.app.Activity;
import android.app.UiAutomation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.test.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;


public class TOGGLETools {

    private static String LOG_FILE_PATH = "sdcard/";
    private static String LOG_FILE_NAME = "mylog.txt";

    public static void request(/*String... permissions*/) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            UiAutomation auto = InstrumentationRegistry.getInstrumentation().getUiAutomation();
            String cmd = "pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " %1$s";
            String cmdTest = "pm grant " + InstrumentationRegistry.getContext().getPackageName() + " %1$s";
            String currCmd;
            execute(String.format(cmd, Manifest.permission.READ_EXTERNAL_STORAGE), auto);
            execute(String.format(cmdTest, Manifest.permission.READ_EXTERNAL_STORAGE), auto);
            execute(String.format(cmd, Manifest.permission.WRITE_EXTERNAL_STORAGE), auto);
            execute(String.format(cmdTest, Manifest.permission.WRITE_EXTERNAL_STORAGE), auto);
        }
        GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //GrantPermissionRule.grant(permissions);
    }

    private static void execute(String currCmd, UiAutomation auto){
        //Log.d(TAG, "exec cmd: " + currCmd);
        auto.executeShellCommand(currCmd);
    }

    private Rect retrieveSmallRect(Point center_point, Rect starting_rect) {


        return null;
    }



    private Point retrieve_relative_x_y_int(float x, float y, Rect reference_rectangle) {


        return null;


    }

    public static void clearLog() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(LOG_FILE_PATH);
        pw.close();
    }

    public static void setLogFileName(String name){
        LOG_FILE_NAME = name+".txt";
    }

    public static void appendLog(String className,String text)
    {
        File logFile = new File(LOG_FILE_PATH + className + ".txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.flush();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Rect GetCurrentDisplaySize(Activity activity) {

        View v1 = activity.getWindow().getDecorView().getRootView();
        Display display = activity.getWindowManager().getDefaultDisplay();
        Rect r = new Rect();
        v1.getWindowVisibleDisplayFrame(r);
        return r;


    }




    public static boolean hasOpenedDialogs(FragmentActivity activity) {
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void getDialogScreenshot(Context context, View starting_view, String bitmap_path) throws Exception {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Field globalField = Class.forName("android.view.WindowManagerImpl").getDeclaredField("mGlobal");
        globalField.setAccessible(true);
        Object globalFieldValue = globalField.get(windowManager);
        List<?> viewsFieldValue = getViews(globalFieldValue);
        for (int i = viewsFieldValue.size() - 1; i >= 0; i--) {
            View decorView = (View)viewsFieldValue.get(i);
            WindowManager.LayoutParams castedParams = (WindowManager.LayoutParams)decorView.getLayoutParams();
            //if (decorView.getClass().getName().compareTo("android.widget.PopupWindow$PopupDecorView") == 0) {
            if (i == viewsFieldValue.size() - 1) {

                decorView.setDrawingCacheEnabled(true);
                int[] coord = new int[2];
                decorView.getLocationOnScreen(coord);
                //Log.wtf("dialogs", "xv =" + coord[0] + " - " + "yv = " + coord[1]);
                //Log.wtf("dialogs", "vw =" + decorView.getWidth() + " - " + "vh = " + decorView.getHeight());
                Bitmap bitmap = Bitmap.createBitmap(decorView.getDrawingCache());
                decorView.setDrawingCacheEnabled(false);
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "prova.png";
                // File imageFile = new File(mPath);
                //  FileOutputStream outputStream = new FileOutputStream(imageFile);
                //    int quality = 100;
                //   bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
                //Log.wtf("dialogs", "bitmap sizes " + bitmap.getWidth() + " - " + bitmap.getHeight());

                Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                Bitmap generated_bitmap = Bitmap.createBitmap(starting_view.getWidth(), starting_view.getHeight(), conf); // this creates a MUTABLE bitmap
                Canvas canvas = new Canvas(generated_bitmap);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                canvas.drawRect(0F, 0F, (float) starting_view.getWidth(), (float) starting_view.getHeight(), paint);

                paint = new Paint();
                canvas.drawBitmap(bitmap, coord[0], coord[1], paint);

                Log.wtf("bitmaps", bitmap_path);

                boolean result = new BitmapSaver().save(generated_bitmap, bitmap_path);
                Log.wtf("bitmaps", bitmap_path);


                //File imageFile2 = new File(bitmap_path);
                //FileOutputStream outputStream2 = new FileOutputStream(imageFile2);

                //generated_bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream2);


            }
            if (castedParams.type == WindowManager.LayoutParams.TYPE_APPLICATION) { // Dialog
                Log.wtf("dialogs", decorView.getClass().getName());
            }
        }
    }

    public static List<?> getViews(Object globalWindowManager) throws NoSuchFieldException, IllegalAccessException {
        Field viewsField = globalWindowManager.getClass().getDeclaredField("mViews");
        viewsField.setAccessible(true);
        Object viewsFieldValue = viewsField.get(globalWindowManager);
        if (viewsFieldValue instanceof List<?>)
            return (List<?>)viewsFieldValue;
        else if (viewsFieldValue instanceof View[]) {
            return new ArrayList<>(Arrays.asList((View[])viewsFieldValue));
        }
        return null;
    }


    public static boolean TakeScreenCapture(Date curr_time, Activity activity) throws Exception {


        Thread.sleep(500);
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + curr_time.getTime() + ".png";
        //Log.i("touchtest", mPath);
        String mPath2 = Environment.getExternalStorageDirectory().toString() + "/" + curr_time.getTime() + ".bmp";

        View v1 = activity.getWindow().getDecorView().getRootView();


        View v2;


        Log.wtf("dialogs", String.valueOf(v1.hasWindowFocus()));
        if (!v1.hasWindowFocus()) {

            getDialogScreenshot(activity.getApplicationContext(), v1, mPath2);
        } else {
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);

            try {

                //need to have storage permission for this (enable from settings)
                //FileOutputStream outputStream = new FileOutputStream(imageFile);


                Display display = activity.getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int display_width = size.x;
                int display_height = size.y;

                Rect r = new Rect();
                v1.getWindowVisibleDisplayFrame(r);

                int real_height = r.bottom - r.top;
                int real_width = r.right - r.left;

                int quality = 100;

                //Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, r.left, r.top, real_width, real_height);
                Bitmap croppedBitmap = bitmap; //per non ritagliare contorni

                boolean result = new BitmapSaver().save(bitmap, mPath2);


                //croppedBitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);


                //outputStream.flush();
                //outputStream.close();


                //Log.wtf("dialogs", "returning true");

            } catch (Exception e) {
                //Log.d("debug_screencapture", e.getMessage());
                Log.wtf("bitmaps", e.getMessage());

                e.printStackTrace();
                return false;
            }


            //spoon screenshot


        }
        return true;

    }


    public static Bitmap resize_screen_capture() {



        return null;
    }


    public static String DumpScreen(Date curr_time, UiDevice device) {

        String dumpPath = Environment.getExternalStorageDirectory().toString() + "/" + curr_time.getTime() + ".xml";
        final File dump = new File(dumpPath);

        if (dump.exists()) {
            dump.delete();
        }
        try {
            device.dumpWindowHierarchy(dump);
            //Log.d("touchtest2",  "dumped " + Environment.getExternalStorageDirectory().toString() + "/" + curr_time.getTime() + ".xml");
            return dumpPath;
        }
        catch (Exception e) {
            Log.d("touchtest2", "error in dumping");
            Log.d("touchtest2", e.getMessage());
            e.printStackTrace();
            return "";
        }


    }




    public static void TakeAndLogInfo() {


        //functions that calls all the internal ones....
    }


    public static void LogInteraction(Date now, String test_name, String search_type, String search_kw, String interaction_type, String interaction_params) {

        //version for interaction with parameters
        Log.d("touchtest", test_name +", " + now.getTime() + ", " + search_type + ", " + search_kw + ", " + interaction_type + ", " + interaction_params);

        return;

    }

    public static void LogInteraction(Date now, String test_name, String search_type, String search_kw, String interaction_type) {

        //version for interaction without parameters
        Log.d("touchtest", test_name + ", " + now.getTime() + ", " + search_type + ", " + search_kw + ", " + interaction_type + ",  ");

        return;

    }

    public static void LogInteraction(Date now) {

        //version for check of full screen
        Log.d("touchtest", now.getTime() + ", , , , ");
    }


    public static String DumpScreenProgressive(int num, String test_name, UiDevice device) {

        String dumpPath = Environment.getExternalStorageDirectory().toString() + "/" + test_name + num + ".xml";
        final File dump = new File(dumpPath);

        if (dump.exists()) {
            dump.delete();
        }
        try {
            device.dumpWindowHierarchy(dump);
            //Log.d("touchtest2",  "dumped " + Environment.getExternalStorageDirectory().toString() + "/" + curr_time.getTime() + ".xml");
            return dumpPath;
        }
        catch (Exception e) {
            //Log("touchtest2: error in dumping");
            Log.d("touchtest2", "error in dumping");
            //appendLog("touchtest2: " + e.getMessage());
            Log.d("touchtest2", e.getMessage());
            e.printStackTrace();
            return "";
        }


    }





    //progressive versions

    public static void LogInteractionProgressive(String className, int num, String test_name, String search_type, String search_kw, String interaction_type, String interaction_params) {

        //version for interaction with parameters
        appendLog(className,"touchtest: "
                + test_name + "; "
                + test_name + num + "; "
                + search_type + "; "
                + search_kw + "; "
                + interaction_type + "; "
                + interaction_params);
        Log.d("touchtest", test_name +"; " + test_name + num + "; " + search_type + "; " + search_kw + "; " + interaction_type + "; " + interaction_params);

        return;

    }

    public static void LogInteractionProgressive(String className, int num, String test_name, String search_type, String search_kw, String interaction_type) {

        //version for interaction without parameters
        appendLog(className,"touchtest: "
                + test_name + "; "
                + test_name + num + "; "
                + search_type + "; "
                + search_kw + "; "
                + interaction_type);
        Log.d("touchtest", test_name + "; " +test_name + num + "; " + search_type + "; " + search_kw + "; " + interaction_type + "; ");

        return;

    }

    public static void LogInteractionProgressive(String className, int num) {

        //version for check of full screen
        appendLog(className,"touchtest: , , , , ");
        Log.d("touchtest", num + ", , , , ");
    }

    public static boolean TakeScreenCaptureProgressive(int num, String test_name, Activity activity) throws Exception {


        Thread.sleep(500);
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + test_name + num + ".png";
        //Log.i("touchtest", mPath);
        String mPath2 = Environment.getExternalStorageDirectory().toString() + "/" + test_name + num + ".bmp";

        View v1 = activity.getWindow().getDecorView().getRootView();


        View v2;


        Log.wtf("dialogs", String.valueOf(v1.hasWindowFocus()));
        //appendLog("dialogs: " + String.valueOf(v1.hasWindowFocus()));
        if (!v1.hasWindowFocus()) {

            getDialogScreenshot(activity.getApplicationContext(), v1, mPath2);
        } else {
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);

            try {

                //need to have storage permission for this (enable from settings)
                //FileOutputStream outputStream = new FileOutputStream(imageFile);


                Display display = activity.getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int display_width = size.x;
                int display_height = size.y;

                Rect r = new Rect();
                v1.getWindowVisibleDisplayFrame(r);

                int real_height = r.bottom - r.top;
                int real_width = r.right - r.left;

                int quality = 100;

                //Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, r.left, r.top, real_width, real_height);
                Bitmap croppedBitmap = bitmap; //per non ritagliare contorni

                boolean result = new BitmapSaver().save(bitmap, mPath2);


                //croppedBitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);


                //outputStream.flush();
                //outputStream.close();


                //Log.wtf("dialogs", "returning true");

            } catch (Exception e) {
                //Log.d("debug_screencapture", e.getMessage());
                Log.wtf("bitmaps", e.getMessage());
                //appendLog("bitmaps: " + e.getMessage());

                e.printStackTrace();
                return false;
            }


            //spoon screenshot


        }
        return true;

    }



    public static class TakeScreenCaptureTask implements Callable<Boolean> {
        Date now;
        Activity activityToggleTools;
        TakeScreenCaptureTask(Date d, Activity a) { now = d; activityToggleTools = a; }
        public Boolean call() {
            try {

                boolean res = TOGGLETools.TakeScreenCapture(now, activityToggleTools);
                return res;
            }
            catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    public static class TakeScreenCaptureTaskProgressive implements Callable<Boolean> {
        int num;
        String test_name;
        Activity activityToggleTools;
        TakeScreenCaptureTaskProgressive(int num, String test_name, Activity a) { this.num = num; this.test_name = test_name; activityToggleTools = a; }
        public Boolean call() {
            try {

                boolean res = TOGGLETools.TakeScreenCaptureProgressive(num, test_name, activityToggleTools);
                return res;
            }
            catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }
    }


}
