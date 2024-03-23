package zettasword.player_mana.network;

import zettasword.player_mana.PlayerMana;
import zettasword.player_mana.network.packets.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketManager {
    private static short id;
    public static final SimpleNetworkWrapper net = NetworkRegistry.INSTANCE.newSimpleChannel(PlayerMana.MODID);

    public static void init(){
        registerMessage(PacketSoulMana.PacketHandler.class, PacketSoulMana.class);
        registerMessage(PacketSoulMaxMana.PacketHandler.class, PacketSoulMaxMana.class);
    }
/*
    public static void regServer(){
      //  runes.registerMessage(AmuletActivity.AmuletsActivityHandler.class, AmuletActivity.class, id++, Side.SERVER);
      //  runes.registerMessage(MeditationStart.MeditationHandler.class, MeditationStart.class, id++, Side.SERVER);
    }

    public static void regClient(){
       // runes.registerMessage(SoulUpdate.SoulUpdateHandler.class, SoulUpdate.class, id++, Side.CLIENT);
    }
*/
    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(
            Class<? extends IMessageHandler<REQ, REPLY>> packet, Class<REQ> message){
        net.registerMessage(packet, message, id, Side.CLIENT);
        net.registerMessage(packet, message, id, Side.SERVER);
        id++;
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(
            Class<? extends IMessageHandler<REQ, REPLY>> packet, Class<REQ> message, boolean isServer){
        net.registerMessage(packet, message, id, isServer ? Side.SERVER : Side.CLIENT);
        id++;
    }
}
