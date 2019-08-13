package ex02;

import java.io.IOException;

public class StaticResourceProcessor {
    public void process(IRequest02 request, IResponse02 response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
