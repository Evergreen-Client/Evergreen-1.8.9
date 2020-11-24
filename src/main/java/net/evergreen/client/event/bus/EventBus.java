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

package net.evergreen.client.event.bus;

import net.evergreen.client.Evergreen;
import net.evergreen.client.exception.IllegalAnnotationException;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventBus {

    private final Map<Object, List<EventMethod>> registeredClasses = new HashMap<>();

    public void register(Object o) {
        List<EventMethod> eventMethods = new ArrayList<>();

        for (Method m : o.getClass().getDeclaredMethods()) {
            SubscribeEvent annotation = m.getAnnotation(SubscribeEvent.class);

            if (annotation != null) {
                Class<?> parameterType = m.getParameterTypes()[0];
                if (parameterType == null) {
                    throw new IllegalAnnotationException("Annotated event method did not contain parameter for event specification.");
                }

                m.setAccessible(true);
                eventMethods.add(new EventMethod(o, m, parameterType, annotation));
            }
        }

        eventMethods.sort(Comparator.comparingInt(em -> em.getAnnotation().priority().getValue()));
        registeredClasses.put(o, eventMethods);
    }

    public void unregister(Object instance) {
        registeredClasses.remove(instance);
    }
    
    public void post(@NotNull Object event) {
        registeredClasses.forEach((object, methodList) -> {
            for (EventMethod em : methodList) {
                // Correct event
                if (em.getEvent().equals(event.getClass())) {
                    try {
                        Minecraft.getMinecraft().mcProfiler.startSection(em.getMethod().getName());
                        em.getMethod().invoke(em.getInstance(), event);
                        Minecraft.getMinecraft().mcProfiler.endSection();
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
