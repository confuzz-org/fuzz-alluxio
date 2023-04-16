/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.conf;

import edu.illinois.confuzz.internal.ConfigTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ConfTracker {
    private static final Logger LOG = LoggerFactory.getLogger(ConfTracker.class);
    private static final boolean ctestLogEnabled = Boolean.getBoolean("ctest.log");
    public static void trackConfig(PropertyKey key, Optional<Object> value, boolean isSet) {
        trackConfig(key, value.orElse(null), isSet);
    }

    public static void trackConfig(PropertyKey key, Object value, boolean isSet) {
        trackConfig(key.getName(), key, value, isSet);
    }

    public static void trackConfig(String name, PropertyKey key, Object value, boolean isSet) {
        if (ctestLogEnabled) {
            if (isSet) {
                LOG.warn("[CTEST][SET-PARAM] " + key + " = " + value + " " + getStackTrace()); //CTEST
            } else {
                LOG.warn("[CTEST][GET-PARAM] " + key + " = " + value + " " + getStackTrace()); //CTEST
            }
        }
        if (isSet) {
            ConfigTracker.trackSet(name, key, value);
        } else {
            ConfigTracker.trackGet(name, key, value);
        }
    }

    private static String getStackTrace() {
        String stacktrace = " ";
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().contains("Test")) {
                stacktrace = stacktrace.concat(element + "\t");
            }
        }
        return stacktrace;
    }
}
