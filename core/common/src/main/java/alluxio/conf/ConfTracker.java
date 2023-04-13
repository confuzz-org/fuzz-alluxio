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
    public static void trackConfig(PropertyKey key, Optional<Object> value, boolean isSet) {
        trackConfig(key, value.orElse(null), isSet);
    }

    public static void trackConfig(PropertyKey key, Object value, boolean isSet) {
        trackConfig(key.getName(), value, isSet);
    }

    public static void trackConfig(String key, Object value, boolean isSet) {
        if (value == null) {
            ConfigTracker.track(key, null, isSet);
        } else if ((value instanceof String)||(value instanceof Boolean)) {
            ConfigTracker.track(key, String.valueOf(value), isSet);
        } else {
            LOG.error("[CTEST] Cannot track config " + key + " of class " + value.getClass().getName()
                    + " with value " + value);
        }
    }
}
