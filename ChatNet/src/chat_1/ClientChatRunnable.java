package chat_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientChatRunnable  implements Runnable{
    private Socket socket;

    private BufferedReader reader;
    private OutputStream writer;

    public ClientChatRunnable(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            String msg = null;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = socket.getOutputStream();

            while ((msg = reader.readLine()) != null) {
                msg += "\n";
                //发送给除了自己以外的其他客户端
                for (Socket item : ChatServer.clientList) {
                    if (item != socket) {
                        System.out.println("发送消息["+msg+"]到 "+item);
                        item.getOutputStream().write(msg.getBytes());

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printLine(String string) {
        System.out.println(string);

    }
}
