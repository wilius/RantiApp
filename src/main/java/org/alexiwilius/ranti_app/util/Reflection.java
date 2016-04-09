package org.alexiwilius.ranti_app.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by AlexiWilius on 18.1.2015.
 */
public class Reflection {

    public static Object getInstance(Class cls, Object... params) throws Exception {
        Constructor constructor = getConstructor(cls, params);
        if (constructor == null)
            throw new Exception("Cannot find applied constructor match with parameters passed");

        return constructor.newInstance(params);
    }

    public static Constructor getConstructor(Class cls, Object... params) {
        Constructor[] consList = cls.getConstructors();
        for (Constructor cons : consList) {
            Class[] consParams = cons.getParameterTypes();

            int i = 0;
            for (; i < consParams.length; i++) {
                if (consParams[i] != params[i].getClass())
                    break;
            }

            if (i == params.length) {
                return cons;
            }
        }
        return null;
    }

    public static Object call(Object o, String method, Object... params) throws Exception {
        Method m = getMethod(o.getClass(), method, params);
        return m.invoke(o, params);
    }

    public static Method getMethod(Class c, String method, Object[] params) throws NoSuchMethodException {
        Class[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++)
            paramTypes[i] = params[i].getClass();
        return c.getMethod(method, paramTypes);
    }
}