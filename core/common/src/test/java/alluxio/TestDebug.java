package alluxio;
import alluxio.conf.AlluxioProperties;
import alluxio.conf.Configuration;
import alluxio.conf.InstancedConfiguration;
import alluxio.conf.PropertyKey;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestDebug {

    @Test
    public void testECFuzzFail() {
        AlluxioProperties config = Configuration.copyGlobal().getProperties();
        PropertyKey debugProperty = PropertyKey.getOrBuildCustom("alluxio.fuse.debug.enabled");
        String debug = (String) config.get(debugProperty);
        if (debug != null && debug.equalsIgnoreCase("true")) {
            throw new RuntimeException("Fake Bug");
        }
    }

    @Test
    public void test() throws Exception {
        PropertyKey fake2 = PropertyKey.getOrBuildCustom("fake2");
        PropertyKey fake3 = PropertyKey.getOrBuildCustom("fake3");
        AlluxioProperties config = Configuration.copyGlobal().getProperties();

        int count = 0;
        String name = (String) config.get(PropertyKey.getOrBuildCustom("myname"));

        String age = (String) config.get(PropertyKey.getOrBuildCustom("myage"));
        System.out.println("name: " + name + ", age: " + age);

        String str = (String) config.get(PropertyKey.getOrBuildCustom("fake-param"));
        config.set(fake2, 200);
        config.set(fake3, 300);
        assertEquals(200, config.get(fake2));

        //System.out.println(str);
        if (str != null) {
            if (str.equals("always")) {
                System.out.println("always");
                count++;
                // This should fail if the fuzzer is not specified with -DexpectedException=java.lang.AssertionError
		//                assertEquals(200, config.get(fake3));
            } else if (str.equals("asneeded")) {
                System.out.println("asneeded");
                count--;
                throw new Exception("Fake Bug asneeded");
            } else {
                System.out.println(str);
            }
        } else {
            System.out.println("str is null!!!");
        }
    }
}
