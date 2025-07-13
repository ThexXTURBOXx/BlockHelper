package mcp.mobius.waila.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public final class AccessHelper {

    private AccessHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the searched {@link Class}.
     *
     * @param clazz The fully-qualified class name to search for.
     * @return The searched {@link Class}.
     * @throws ClassNotFoundException The {@link Class} could not be found.
     */
    public static Class<?> getClass(String clazz) throws ClassNotFoundException {
        try {
            return Class.forName(clazz);
        } catch (Throwable ignored) {
        }
        throw new ClassNotFoundException(clazz);
    }

    /**
     * Returns the searched declared {@link Constructor}.
     *
     * @param clazz          The class to search the declared constructor in.
     * @param parameterTypes The parameter types of the searched constructor.
     * @return The searched {@link Constructor}.
     * @throws NoSuchMethodException The {@link Constructor} could not be found.
     */
    public static Constructor<?> getDeclaredConstructor(Class<?> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (Throwable ignored) {
        }
        throw new NoSuchMethodException(clazz.getName() + ".<init>(" + parameterTypes.length + " params)");
    }

    /**
     * Returns the searched {@link Constructor}.
     *
     * @param clazz          The class to search the constructor in.
     * @param parameterTypes The parameter types of the searched constructor.
     * @return The searched {@link Constructor}.
     * @throws NoSuchMethodException The {@link Constructor} could not be found.
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        try {
            Constructor<?> constructor = clazz.getConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (Throwable ignored) {
        }
        throw new NoSuchMethodException(clazz.getName() + ".<init>(" + parameterTypes.length + " params)");
    }

    /**
     * Returns the reflected declared {@link Method} of a class.
     *
     * @param clazz          The class to search the declared method in.
     * @param parameterTypes The parameter types of the searched method.
     * @param methodNames    Various names the method could have (e.g., original, searge, deobf).
     * @return The searched {@link Method}.
     * @throws NoSuchMethodException The {@link Method} could not be found.
     */
    public static Method getDeclaredMethod(Class<?> clazz, Class<?>[] parameterTypes, String... methodNames)
            throws NoSuchMethodException {
        for (String methodName : methodNames) {
            try {
                Method m = clazz.getDeclaredMethod(methodName, parameterTypes);
                m.setAccessible(true);
                return m;
            } catch (Throwable ignored) {
            }
        }
        throw new NoSuchMethodException(clazz.getName() + "." + Arrays.toString(methodNames)
                                        + "(" + parameterTypes.length + " params)");
    }

    /**
     * Returns the reflected {@link Method} of a class.
     *
     * @param clazz          The class to search the method in.
     * @param parameterTypes The parameter types of the searched method.
     * @param methodNames    Various names the method could have (e.g., original, searge, deobf).
     * @return The searched {@link Method}.
     * @throws NoSuchMethodException The {@link Method} could not be found.
     */
    public static Method getMethod(Class<?> clazz, Class<?>[] parameterTypes, String... methodNames)
            throws NoSuchMethodException {
        for (String methodName : methodNames) {
            try {
                Method m = clazz.getMethod(methodName, parameterTypes);
                m.setAccessible(true);
                return m;
            } catch (Throwable ignored) {
            }
        }
        throw new NoSuchMethodException(clazz.getName() + "." + Arrays.toString(methodNames)
                                        + "(" + parameterTypes.length + " params)");
    }

    /**
     * Returns the reflected declared {@link Field} of a class.
     *
     * @param clazz      The class to search the declared field in.
     * @param fieldNames Various names the field could have (e.g., original, searge, deobf).
     * @return The searched {@link Field}.
     * @throws NoSuchFieldException The {@link Field} could not be found.
     */
    public static Field getDeclaredField(Class<?> clazz, String... fieldNames) throws NoSuchFieldException {
        for (String fieldName : fieldNames) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (Throwable ignored) {
            }
        }
        throw new NoSuchFieldException(clazz.getName() + "." + Arrays.toString(fieldNames));
    }

    /**
     * Returns the reflected {@link Field} of a class.
     *
     * @param clazz      The class to search the field in.
     * @param fieldNames Various names the field could have (e.g., original, searge, deobf).
     * @return The searched {@link Field}.
     * @throws NoSuchFieldException The {@link Field} could not be found.
     */
    public static Field getField(Class<?> clazz, String... fieldNames) throws NoSuchFieldException {
        for (String fieldName : fieldNames) {
            try {
                Field f = clazz.getField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (Throwable ignored) {
            }
        }
        throw new NoSuchFieldException(clazz.getName() + "." + Arrays.toString(fieldNames));
    }

}
