package javaspi;

import java.util.Iterator;
import java.util.ServiceLoader;

public class RSPITest {
    public static void main(String[] args) {
        ServiceLoader<RSPI> rspis = ServiceLoader.load(RSPI.class);
        Iterator<RSPI> iterator = rspis.iterator();
        while (iterator.hasNext()) {
            RSPI rspi = iterator.next();
            rspi.say("id");
        }
    }
}
