import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * PACKAGE_NAME
 * Created by YJM6280 .
 */
public class MyDispathcer {
    private  Integer port;
    private  EventHandler handler;

    public MyDispathcer(Integer port, EventHandler handler) {
        this.port = port;
        this.handler= handler;
    }
    public  void run () throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(!Thread.interrupted()){
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while(keyIterator.hasNext()){
                if(selector.select(3000)==0){
                    continue;
                }
                SelectionKey next = keyIterator.next();
                if(next.isAcceptable()){
                   handler.handAccept(next);
                }
                if(next.isReadable()){
                    handler.handRead(next);
                }
                if(next.isWritable()){
                    handler.handWrite(next);
                }
                keyIterator.remove();
            }
        }
    }

}
