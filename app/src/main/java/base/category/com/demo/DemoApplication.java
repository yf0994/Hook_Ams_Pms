package base.category.com.demo;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.InvocationTargetException;

import base.category.com.demo.hook.Hook;

/**
 * Created by fengyin on 16-7-7.
 */
public class DemoApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Hook.hookAms();
            Hook.hookPms(this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
