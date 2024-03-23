package zettasword.player_mana.proxy;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zettasword.player_mana.cap.ISoul;
import zettasword.player_mana.cap.SoulProvider;
import zettasword.player_mana.network.packets.PacketSoulMana;
import zettasword.player_mana.network.packets.PacketSoulMaxMana;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);

    }

    @Override
    public void handleSoulMana(PacketSoulMana message) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ISoul soul = player.getCapability(SoulProvider.SOUL_CAP, null);
        if(soul != null) {
            soul.setMP(message.mana);
        }
    }

    @Override
    public void handleSoulMaxMana(PacketSoulMaxMana message) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ISoul soul = player.getCapability(SoulProvider.SOUL_CAP, null);
        if(soul != null) {
            soul.setMaxMP(message.maxMana);
        }
    }


    @Override
    public void initialiseLayers() {
    }

    @Override
    public void registerResourceReloadListeners(){
        IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
        if(manager instanceof IReloadableResourceManager){
           // ((IReloadableResourceManager)manager).registerReloadListener(GuiTalesSpellDisplay::loadSkins);
        }
    }

    @Override
    public void registerKeyBindings(){
    }

    @Override
    public void registerParticles(){
        // I'll be a good programmer and use the API method rather than the one above. Lead by example, as they say...
       // ParticleWizardry.registerParticle(ParticlesCreator.Type.RING, ParticleRing::new);
    }

    @Override
    public void registerExtraHandbookContent() {
      //  GuiWizardHandbook.registerAddonHandbookContent(WizardryTales.MODID);
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
    }
}
