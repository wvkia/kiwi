package javaspi;

import com.alibaba.dubbo.common.extension.SPI;

@SPI
public interface RSPI {
    void say(String id);

}
