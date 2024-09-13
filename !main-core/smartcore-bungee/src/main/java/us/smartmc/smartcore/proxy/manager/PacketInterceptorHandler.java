package us.smartmc.smartcore.proxy.manager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PacketInterceptorHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;

            // Guardar el índice de lectura actual
            int packetStartIndex = buf.readerIndex();

            // Leer el identificador del paquete (packet ID)
            int packetId = readVarInt(buf);
            if (packetId == -676459938) {
                buf.readerIndex(packetStartIndex);
                return;
            }

            //System.out.println("Paquete entrante con ID: " + packetId);

            if (packetId == 0x12) {
                System.out.println("PAQUETE ENTRANTE PLUGIN MESSAGE!");
            }

            // Volver al índice de lectura original para no alterar el flujo
            buf.readerIndex(packetStartIndex);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;

            // Guardar el índice de lectura actual
            int packetStartIndex = buf.readerIndex();

            // Leer el identificador del paquete (packet ID)
            int packetId = readVarInt(buf);
            if (packetId == -676459938) {
                buf.readerIndex(packetStartIndex);
                return;
            }

            if (packetId == 0x12) {
                System.out.println("PAQUETE SALIENTE PLUGIN MESSAGE!");
            }

            //System.out.println("Paquete saliente con ID: " + packetId);

            // Volver al índice de lectura original para no alterar el flujo
            buf.readerIndex(packetStartIndex);
        }
        super.write(ctx, msg, promise);
    }

    private int readVarInt(ByteBuf buf) {
        try {
            int numRead = 0;
            int result = 0;
            byte read;
            do {
                read = buf.readByte();
                int value = (read & 0b01111111);
                result |= (value << (7 * numRead));

                numRead++;
                if (numRead > 5) {
                    return -676459938;
                }
            } while ((read & 0b10000000) != 0);

            return result;
        } catch (Exception e) {
            return -676459938;
        }
    }

}
