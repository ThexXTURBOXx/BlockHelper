package de.thexxturboxx.blockhelper.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.item.ItemStack;

/**
 * Adapter class providing useful helper functions when extending BlockHelper's integration functionalities.
 */
public class BlockHelperInfoProvider implements BlockHelperBlockProvider, BlockHelperEntityProvider,
        BlockHelperNameFixer, BlockHelperItemStackFixer, BlockHelperModFixer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInformation(BlockHelperEntityState state, InfoHolder info) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(BlockHelperBlockState state) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMod(BlockHelperBlockState state) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMod(Object object) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Checks if the given class is currently loaded and the given object is instance of that class.
     *
     * @param obj   The object to check if it is an instance of a class.
     * @param clazz The fully qualified class name.
     * @return Whether the given class is loaded and the given object is instance of that class.
     */
    public static boolean isLoadedAndInstanceOf(Object obj, String clazz) {
        if (obj == null)
            return false;
        try {
            Class<?> c = Class.forName(clazz);
            if (c.isInstance(obj))
                return true;
        } catch (Throwable ignored) {
        }
        return false;
    }

    /**
     * Checks if the given object is instance of the given class.
     *
     * @param obj   The object to check if it is an instance of a class.
     * @param clazz The fully qualified class name.
     * @return Whether the given object is instance of the given class.
     * @see #isLoadedAndInstanceOf(Object, String)
     */
    public static boolean iof(Object obj, String clazz) {
        return isLoadedAndInstanceOf(obj, clazz);
    }

    /**
     * Returns the searched {@link Class}.
     *
     * @param clazz The fully-qualified class name to search for.
     * @return The searched {@link Class} or {@code null} if the search failed.
     */
    public static Class<?> getClass(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * Returns the reflected declared {@link Method}.
     *
     * @param clazz          The class to search the declared method in.
     * @param method         The method name to search for.
     * @param parameterTypes The parameter types of the searched method.
     * @return The searched {@link Method} or {@code null} if the search failed.
     */
    public static Method getDeclaredMethod(Class<?> clazz, String method, Class<?>... parameterTypes) {
        try {
            Method m = clazz.getDeclaredMethod(method, parameterTypes);
            m.setAccessible(true);
            return m;
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * Returns the reflected {@link Method}.
     *
     * @param clazz          The class to search the method in.
     * @param method         The method name to search for.
     * @param parameterTypes The parameter types of the searched method.
     * @return The searched {@link Method} or {@code null} if the search failed.
     */
    public static Method getMethod(Class<?> clazz, String method, Class<?>... parameterTypes) {
        try {
            Method m = clazz.getMethod(method, parameterTypes);
            m.setAccessible(true);
            return m;
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * Returns the declared field of a class from a given object.
     *
     * @param clazz The class to search the declared field in.
     * @param obj   The object to retrieve the field's value from, or {@code null} for a static search.
     * @param field The field name to search for.
     * @param <T>   The desired return type.
     * @return The searched field's value or {@code null} if the search failed.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDeclaredField(Class<?> clazz, Object obj, String field) {
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            return (T) f.get(obj);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * Returns the field of a class from a given object.
     *
     * @param clazz The class to search the field in.
     * @param obj   The object to retrieve the field's value from, or {@code null} for a static search.
     * @param field The field name to search for.
     * @param <T>   The desired return type.
     * @return The searched field's value or {@code null} if the search failed.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getField(Class<?> clazz, Object obj, String field) {
        try {
            Field f = clazz.getField(field);
            f.setAccessible(true);
            return (T) f.get(obj);
        } catch (Throwable ignored) {
            return null;
        }
    }

}
