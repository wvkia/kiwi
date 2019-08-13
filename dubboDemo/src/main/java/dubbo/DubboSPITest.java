package dubbo;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import javaspi.RSPI;

public class DubboSPITest {
    public static void main(String[] args) {
        ExtensionLoader<RSPI> extensionLoader = ExtensionLoader.getExtensionLoader(RSPI.class);
        RSPI rspia = extensionLoader.getExtension("A");
        rspia.say("id");


        RSPI rspib = extensionLoader.getExtension("B");
        rspib.say("id");

    }
}
