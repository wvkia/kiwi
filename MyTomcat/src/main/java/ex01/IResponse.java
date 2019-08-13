package ex01;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class IResponse {
    private static final int BUFFER_SIZE = 1024;
    IRequest request;
    OutputStream output;

    public IResponse(OutputStream output) {
        this.output = output;
    }
    public void setRequest(IRequest request) { this.request = request;
    }


    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {

            //获取请求的数据目录
            File file = new File(IHttpServer.WEB_ROOT, request.getUri());
            if (file.exists()) {
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch!=-1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            } else {
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" + "Content-Length: 23\r\n" + "\r\n" +
                        "<h1>File Not Found</h1>";
                output.write(errorMessage.getBytes());
            }
        }
        catch (Exception e) {
        }
        finally {
            if (fis!=null)
                fis.close();
        }
    }
}
