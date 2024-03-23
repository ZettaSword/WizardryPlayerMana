package zettasword.player_mana.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zettasword.player_mana.PlayerMana;

public class PacketSoulMana implements IMessage
{
    public double mana;

    public PacketSoulMana(){}

    public PacketSoulMana(double mana){
        this.mana = mana;
    }

    @Override public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.mana);
    }

    @Override public void fromBytes(ByteBuf buf) {
        this.mana = buf.readDouble();
    }

    public static class PacketHandler implements IMessageHandler<PacketSoulMana, IMessage>
    {

        @Override public IMessage onMessage(PacketSoulMana message, MessageContext ctx)
        {
            if(ctx.side.isClient()) {
                Minecraft.getMinecraft().addScheduledTask(() -> PlayerMana.proxy.handleSoulMana(message));
            }

            return null;
        }
    }


}
