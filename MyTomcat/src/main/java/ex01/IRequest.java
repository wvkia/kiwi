package ex01;

import java.io.IOException;
import java.io.InputStream;

/**
 * 代表一个HTTP请求，从Socket传递过来的InputStream读取read原始数据
 */
public class IRequest {
    private InputStream inputStream;
    private String uri;

    public IRequest(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void parse() {
        //从socket读取数据
        StringBuilder stringBuilder = new StringBuilder();
        int i;
        byte[] buffer = new byte[2018];
        try {
            i = inputStream.read(buffer);
            if (i != -1) {
                for (int j = 0; j < i; j++) {
                    stringBuilder.append((char) buffer[j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(stringBuilder.toString());
        uri = parseUri(stringBuilder.toString());
    }

    private String parseUri(String requestString) {
        int index1 = requestString.indexOf(' ');

        //截取字符串中两个空格之间的数据，就是URL的数据
        //协议
        if (index1 != -1) {
            int index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                return requestString.substring(index1 + 1, index2);
            }
        }
        return null;

    }

    public String getUri() {
        return uri;
    }
}
