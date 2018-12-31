import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * PACKAGE_NAME
 * Created by YJM6280 .
 */
public class NettyServerDemo {
    private static int port;

    public NettyServerDemo(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        NettyServerDemo nettyServerDemo = new NettyServerDemo(8009);
        nettyServerDemo.run();
    }
    public  void run () throws InterruptedException {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast(new EchoServerHandler());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .option(ChannelOption.SO_KEEPALIVE,true);
            System.out.println("服务端启动成功");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
