package zettasword.player_mana;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import zettasword.player_mana.items.TalesWandUpgrade;
import zettasword.player_mana.network.PacketManager;
import zettasword.player_mana.proxy.CommonProxy;

@Mod(modid = PlayerMana.MODID, name = PlayerMana.NAME, version = PlayerMana.VERSION, dependencies =
        "required-after:ebwizardry@[4.3.11,);required-after:forge@[14.23.5.2847,);")
public class PlayerMana {
    public static final String MODID = "player_mana";
    public static final String NAME = "Player Mana";
    public static final String VERSION = "1.2.0";

    @SidedProxy(clientSide = "zettasword.player_mana.proxy.ClientProxy",
            serverSide = "zettasword.player_mana.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(PlayerMana.MODID)
    public static PlayerMana instance;

    public static Logger log;

    public static boolean canCompat(String id){
        return Loader.isModLoaded(id);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        log = event.getModLog();
        ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        proxy.registerParticles();

        TalesWandUpgrade.init();
        PacketManager.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        proxy.initialiseLayers();
        //ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
