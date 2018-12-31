import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * PACKAGE_NAME
 * Created by YJM6280 .
 */
public class EventHandler {
    private  int bufSize=1024;

    public  void handAccept(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
    }

    public  void handRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        int read = socketChannel.read(byteBuffer);
        if(read==1){
           socketChannel.close();
        }else  if(read>0){
            key.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
        }
    }

    public  void handWrite(SelectionKey key){
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.flip();
        if(!byteBuffer.hasRemaining()){
            key.interestOps(SelectionKey.OP_READ);
        }
        byteBuffer.compact();
    }

}
