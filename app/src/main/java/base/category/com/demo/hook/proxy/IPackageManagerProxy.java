package base.category.com.demo.hook.proxy;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by fengyin on 16-7-7.
 */
public class IPackageManagerProxy implements InvocationHandler {

    private Object mIPackageManager; // origin IPackageManager.

    public IPackageManagerProxy(Object iPackageManager){
        mIPackageManager = iPackageManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId)
        if(method.getName().equals("getApplicationInfo")){
            Log.i("Demo", "getApplication:" + (String)args[0]);
        }
        return method.invoke(mIPackageManager, args);
    }
}
