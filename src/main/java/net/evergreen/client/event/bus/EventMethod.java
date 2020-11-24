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

import java.lang.reflect.Method;

public class EventMethod {

    private final Object instance;
    private final Method method;
    private final Class<?> event;
    private final SubscribeEvent annotation;

    public EventMethod(Object instance, Method method, Class<?> event, SubscribeEvent annotation) {
        this.instance = instance;
        this.method = method;
        this.event = event;
        this.annotation = annotation;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getEvent() {
        return event;
    }

    public Object getInstance() {
        return instance;
    }

    public SubscribeEvent getAnnotation() {
        return annotation;
    }

    @Override
    public String toString() {
        return "EventMethod{" +
                "instance=" + instance +
                ", method=" + method +
                ", event=" + event +
                ", annotation=" + annotation +
                '}';
    }
}
