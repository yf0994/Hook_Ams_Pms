package base.category.com.demo.hook.proxy;

import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by fengyin on 16-7-7.
 */
public class IActivityManagerProxy implements InvocationHandler {


    private Object mIActivityManager; // Origin IActivityManager

    public IActivityManagerProxy(Object iActivityManager){
        this.mIActivityManager = iActivityManager;
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //public int startActivity(IBinder whoThread, String callingPackage,Intent intent, String resolvedType, Bundle options)
        if(method.getName().equals("startActivity")){
            Intent intent = (Intent)args[2];
            Log.i("Demo", intent.toString());
        }
        return method.invoke(mIActivityManager, args);
    }
}
