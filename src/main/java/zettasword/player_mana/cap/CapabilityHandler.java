package zettasword.player_mana.cap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zettasword.player_mana.PlayerMana;

@Mod.EventBusSubscriber(modid = PlayerMana.MODID)
public class CapabilityHandler
{
   public static final ResourceLocation SOUL = new ResourceLocation(PlayerMana.MODID, "soul");

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if(!(event.getObject() instanceof EntityPlayer)) return;
        event.addCapability(SOUL, new SoulProvider());
    }

    // This event is crucial to sync stuff with old body of player in another dimension or died one.
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if(event.getEntityPlayer().world.isRemote) return; // No client allowed! We change this by packets

        EntityPlayer current = event.getEntityPlayer();
        ISoul soul = current.getCapability(SoulProvider.SOUL_CAP, null);
        if (soul == null){PlayerMana.log.warn("[Player Mana]: Can't find new Soul!"); return;}

        EntityPlayer old = event.getOriginal();
        ISoul oldSoul = old.getCapability(SoulProvider.SOUL_CAP, null);
        if (oldSoul == null){PlayerMana.log.warn("[Player Mana]: Can't find old Soul!"); return;}

        int MP = (int) Math.ceil(oldSoul.getMP());
        double maxMP = oldSoul.getMaxMP();

        if (current instanceof EntityPlayerMP) {
            soul.setMaxMP(current, maxMP);
            soul.setMP(current, MP);
        }

        NBTTagCompound tags = current.getEntityData();
        tags.setDouble("maxMP", maxMP);
        tags.setDouble("MP", MP);
    }

    // This event is crucial to sync mana, races and stuff on player entering the world.
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerLogInSoul(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.getEntity();
            ISoul soul = player.getCapability(SoulProvider.SOUL_CAP, null);
            if (soul == null) { PlayerMana.log.warn("[Player Mana]: Can't find Soul!");return;}
            if (player instanceof EntityPlayerMP) {
                NBTTagCompound tags = player.getEntityData();
                if (tags.hasNoTags()) return;
                soul.setMaxMP(player, tags.getDouble("maxMP") == 0D ? soul.getMaxMP() : tags.getDouble("maxMP"));
                soul.setMP(player, tags.getDouble("MP"));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerLogOutSoul(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        EntityPlayer player = event.player;
        ISoul soul = player.getCapability(SoulProvider.SOUL_CAP, null);
        if (soul == null) { PlayerMana.log.warn("[Player Mana]: Can't find Soul!");return;}
        if (player instanceof EntityPlayerMP){
            NBTTagCompound tags = player.getEntityData();
            tags.setDouble("maxMP", soul.getMaxMP());
            tags.setDouble("MP", soul.getMP());
        }
    }
}