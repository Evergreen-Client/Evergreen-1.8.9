/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.utils;

import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.lang.reflect.Field;

public class ReflectionHelper {

    public static Field findField(Class<?> clazz, String... fieldNames) {
        Exception failed = null;
        for (String fieldName : fieldNames) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (Exception e) {
                failed = e;
            }
        }
        throw new ReportedException(CrashReport.makeCrashReport(failed, "Could not find field."));
    }

    @SuppressWarnings("unchecked")
    public static <T, E> T getPrivateValue(Class <? super E> classToAccess, E instance, int fieldIndex) {
        try {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            return (T) f.get(instance);
        } catch (Exception e) {
            throw new ReportedException(CrashReport.makeCrashReport(e, "Could not access field."));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, E> T getPrivateValue(Class <? super E> classToAccess, E instance, String... fieldNames) {
        try {
            return (T) findField(classToAccess, fieldNames).get(instance);
        } catch (Exception e) {
            throw new ReportedException(CrashReport.makeCrashReport(e, "Could not access field."));
        }
    }

    public static <T, E> void setPrivateValue(Class <? super T> classToAccess, T instance, E value, int fieldIndex) {
        try {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            f.set(instance, value);
        } catch (Exception e) {
            throw new ReportedException(CrashReport.makeCrashReport(e, "Unable to access field."));
        }
    }

    public static <T, E> void setPrivateValue(Class <? super T> classToAccess, T instance, E value, String... fieldNames) {
        try {
            findField(classToAccess, fieldNames).set(instance, value);
        } catch (Exception e) {
            throw new ReportedException(CrashReport.makeCrashReport(e, "Unable to access field."));
        }
    }

}
