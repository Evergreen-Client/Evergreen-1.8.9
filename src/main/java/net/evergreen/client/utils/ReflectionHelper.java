/*
 * Copyright [2020] [Evergreen]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
