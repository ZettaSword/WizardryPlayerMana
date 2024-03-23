package zettasword.player_mana.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zettasword.player_mana.cap.ISoul;
import zettasword.player_mana.cap.Soul;
import zettasword.player_mana.cap.SoulStorage;
import zettasword.player_mana.network.commands.CommandCheckMana;
import zettasword.player_mana.network.commands.CommandSetMana;
import zettasword.player_mana.network.commands.CommandSetMaxMana;
import zettasword.player_mana.network.packets.PacketSoulMana;
import zettasword.player_mana.network.packets.PacketSoulMaxMana;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        CapabilityManager.INSTANCE.register(ISoul.class, new SoulStorage(), Soul::new);
        //RuneMagic.regServer();
        //MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
    }

    public void init(FMLInitializationEvent event)
    {
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void serverStarting(FMLServerStartingEvent event) {
       event.registerServerCommand(new CommandCheckMana());
       event.registerServerCommand(new CommandSetMana());
       event.registerServerCommand(new CommandSetMaxMana());
    }

    public void initialiseLayers(){}

    public void handleSoulMana(PacketSoulMana message){}
    public void handleSoulMaxMana(PacketSoulMaxMana message){}

    public void registerResourceReloadListeners(){}

    public void registerKeyBindings(){}
    public void registerParticles(){}
    public void registerExtraHandbookContent() {}

    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }
}
