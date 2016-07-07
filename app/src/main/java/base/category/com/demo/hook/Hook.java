package base.category.com.demo.hook;

import android.content.Context;
import android.content.pm.PackageManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import base.category.com.demo.hook.proxy.IActivityManagerProxy;
import base.category.com.demo.hook.proxy.IPackageManagerProxy;

/**
 * Created by fengyin on 16-7-7.
 */
public class Hook {
    public static void hookAms() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        //Get gDefault object.
        Class activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
        Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
        gDefaultField.setAccessible(true);
        Object gDefault = gDefaultField.get(null);

        //Get IActivityManager object.
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = singletonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        Object iActivityManager = mInstanceField.get(gDefault);

        Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");
        //Generate iactivity proxy object.
        Object iActivityManagerProxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{iActivityManagerClass}, new IActivityManagerProxy(iActivityManager));

        //Replace proxy object.
        mInstanceField.set(gDefault, iActivityManagerProxy);
    }

    public static void hookPms(Context context) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // Get ActivityThread object.
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        Object sCurrentActivityThread = currentActivityThreadMethod.invoke(null);
        // Get sPackageManager object.
        Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
        sPackageManagerField.setAccessible(true);
        Object sPackageManager = sPackageManagerField.get(sCurrentActivityThread);

        //Generate proxy object.
        Class<?> iPackageManagerClass = Class.forName("android.content.pm.IPackageManager");
        Object iPackageManagerProxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{iPackageManagerClass}, new IPackageManagerProxy(sPackageManager));

        //Replace proxy object.
        sPackageManagerField.set(sCurrentActivityThread, iPackageManagerProxy);

        //Replace mPM to proxy object in ApplicationPackageManager.
        PackageManager packageManager = context.getPackageManager();
        Field mPMField = packageManager.getClass().getDeclaredField("mPM");
        mPMField.setAccessible(true);
        mPMField.set(packageManager, iPackageManagerProxy);
    }

}
