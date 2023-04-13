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