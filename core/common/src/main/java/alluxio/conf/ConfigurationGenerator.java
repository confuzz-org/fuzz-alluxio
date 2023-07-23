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

import java.util.Map;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import edu.illinois.confuzz.internal.ConfigTracker;
import edu.illinois.confuzz.internal.ConfuzzGenerator;
import org.junit.AssumptionViolatedException;


public class ConfigurationGenerator extends Generator<AlluxioProperties>{
    private static AlluxioProperties generatedConf = null;
    /**
     * Constructor for Configuration Generator
     */
    public ConfigurationGenerator() {
        super(AlluxioProperties.class);
    }

    public static AlluxioProperties getGeneratedConf() {
        if (generatedConf == null) {
            return null;
        }
        return new AlluxioProperties(generatedConf);
    }

    /**
     * This method is invoked to generate a Configuration object
     * @param random
     * @param generationStatus
     * @return
     */
    @Override
    public AlluxioProperties generate(SourceOfRandomness random, GenerationStatus generationStatus)
            throws IllegalArgumentException {
        return generate(random);
    }

    public static AlluxioProperties generate(SourceOfRandomness random) throws IllegalArgumentException {
        Map<String, Object> confMap = ConfuzzGenerator.generate(random);
        generatedConf = new AlluxioProperties(null);
        for (Map.Entry<String, Object> entry: confMap.entrySet()) {
            try {
                PropertyKey key = PropertyKey.fromString(entry.getKey());
                Object value = entry.getValue();
                // TODO: Setting as RUNTIME here might not be the best idea...
                    generatedConf.put_purged(key, value, Source.RUNTIME);
                    Configuration.set_purged(key, value, Source.RUNTIME);
            } catch (Exception e) {
                // do nothing
            }
        }
        try {
            Configuration.reloadProperties();
        } catch (NoClassDefFoundError e) {
            // pass
        } catch (RuntimeException e) {
            throw new AssumptionViolatedException(e.getMessage());
        }
        return generatedConf;
    }
}
