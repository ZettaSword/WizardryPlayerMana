package zettasword.player_mana.events;

import com.google.common.collect.Lists;
import electroblob.wizardry.client.DrawingUtils;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.ISpellCastingItem;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.WandHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zettasword.player_mana.PlayerMana;
import zettasword.player_mana.Tales;
import zettasword.player_mana.api.Aterna;
import zettasword.player_mana.api.EventsBase;
import zettasword.player_mana.api.Sage;
import zettasword.player_mana.api.Solver;
import zettasword.player_mana.cap.ISoul;
import zettasword.player_mana.cap.Mana;
import zettasword.player_mana.cap.SoulProvider;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = PlayerMana.MODID)
public class EventsHandler extends EventsBase {

    private static final ResourceLocation bar_mana =
            new ResourceLocation(PlayerMana.MODID + ":textures/gui/bar_mana.png");

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityShowcase(RenderGameOverlayEvent.Post event){
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) return;

        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (Minecraft.getMinecraft().playerController.shouldDrawHUD()) {
            //event.setCanceled(true);
            ISoul soul = Mana.getSoul(player);
            if(soul == null) return;
            GlStateManager.enableBlend();
            Minecraft.getMinecraft().getTextureManager().bindTexture(bar_mana);
            int left = (event.getResolution().getScaledWidth() / 2 + 91) + Tales.mp.manaPoolX;
            int height = GuiIngameForge.right_height + 10;
            int y = (event.getResolution().getScaledHeight() - height) - Tales.mp.manaPoolY;
            double mana = soul.getMP();
            double maxMana = soul.getMaxMP();
            double manaValue = Math.floor(mana/maxMana * 20.0D);

            if (Tales.mp.manaPoolBar) {
                for (int i = 0; i < 10; ++i) {
                    int idx = i * 2 + 1;
                    int x = left - i * 8 - 9;

                    // Draw Background
                    DrawingUtils.drawTexturedRect(x, y, 0, 0, 9, 9,
                            27, 9);

                    if (idx < manaValue) // 9 - full, 18 - half-full, 0 - zero
                        DrawingUtils.drawTexturedRect(x, y, 9, 0,
                                9, 9, 27, 9);
                    else if (idx == manaValue) {
                        DrawingUtils.drawTexturedRect(x, y, 18, 0,
                                9, 9, 27, 9);
                    }
                }
            }

            int x = left - 8 - 9;
            if (Tales.mp.manaPoolNumber)
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(((int)Math.floor(mana))+ "", x + 20, y, 0x907FB8);

            Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
            GlStateManager.disableBlend();
        }
    }

    @SideOnly(Side.SERVER)
    @SubscribeEvent()
    public static void onPlayerTickManaLow(TickEvent.PlayerTickEvent event){
        if(event.phase == TickEvent.Phase.START && event.player.ticksExisted > 5) {
            EntityPlayer player = event.player;
            ISoul soul = player.getCapability(SoulProvider.SOUL_CAP, null);
            if (soul != null){
                // Low on Mana
                if (Tales.mp.lowOnMana && !event.player.world.isRemote && !event.player.isCreative() && Solver.doEvery(player, 1)){
                    if (soul.getMP() <= soul.getMaxMP() * 0.25F){
                        player.addExhaustion(0.01F);
                    }

                    if (soul.getMP() <= soul.getMaxMP() * 0.1F){
                        player.addExhaustion(0.02F);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void castCostReplacement(SpellCastEvent.Pre event){
        event.getModifiers().set(Sage.CHANT_COST, event.getModifiers().get(Sage.COST), true);
        if (Tales.mp.noMoreManaUse && event.getCaster() instanceof EntityPlayerMP){
            event.getModifiers().set(Sage.COST, 0.0F, true);
        }
    }

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(PlayerMana.MODID))
        {
            ConfigManager.sync(PlayerMana.MODID, Config.Type.INSTANCE);
        }
    }


    @SubscribeEvent()
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if(event.phase == TickEvent.Phase.START && Solver.doEvery(event.player, Tales.mp.seconds_frequency) && event.player instanceof EntityPlayerMP){
            ISoul soul = event.player.getCapability(SoulProvider.SOUL_CAP, null);
            if (soul == null) return;
            if(event.player.ticksExisted > 5) {
                double additional = (Tales.mp.bonus_regen * (soul.getMaxMP()/Tales.mp.max));
                soul.addMana(event.player, Tales.mp.regeneration + additional);
            }
        }
    }

    private static final List<SpellCastEvent.Source> sourceList = Lists.newArrayList(SpellCastEvent.Source.WAND,
            SpellCastEvent.Source.SCROLL, SpellCastEvent.Source.OTHER);

    @SubscribeEvent
    public static void canCastSpell(PlayerInteractEvent.RightClickItem event){
        if(event.getItemStack().getItem() instanceof ISpellCastingItem){
            EntityPlayer player = event.getEntityPlayer();
            if (player instanceof EntityPlayerMP) {
                ISoul soul = Mana.getSoul(player);
                Spell spell = WandHelper.getCurrentSpell(event.getItemStack());
                if (WandHelper.getCurrentCooldown(event.getItemStack()) > 0) {
                    cancel(event);
                    return;
                }
                if (soul == null) return;

                if (isValid(spell) && !player.isCreative() ) {
                    double mana = soul.getMP();

                    // If there is not enough mana...
                    if (mana < getCost(spell)) {
                        cancel(event);
                        Aterna.translate(player, true, "mana.not_enough");
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void canSpellContinuous(SpellCastEvent.Tick event){
        if(event.getCaster() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.getCaster();
            ISoul soul = Mana.getSoul(player);
            Spell spell = event.getSpell();
            if(soul != null && isValid(spell) && !player.isCreative()  && player instanceof EntityPlayerMP) {
                double mana = soul.getMP();

                // If there is not enough mana...
                if(mana < getDistributedCost(spell, event.getCount())) {
                    cancel(event);
                }else {
                    soul.addMana(player, -getDistributedCost(spell, event.getCount())
                            * event.getModifiers().get(Sage.CHANT_COST));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void castSpell(SpellCastEvent.Pre event){
        if(event.getCaster() instanceof EntityPlayerMP) {
            EntityPlayer player = (EntityPlayer) event.getCaster();
            ISoul soul = Mana.getSoul(player);
            Spell spell = event.getSpell();
            /*if (event.getSource().equals(SpellCastEvent.Source.WAND)){
                ItemStack stack = Thief.getWandInUseUniversal(player);
                if (stack != null && WandHelper.getCurrentCooldown(stack) > 0){
                    cancel(event);
                    Aterna.translate(player, true, "mana.not_enough");
                    return;
                }
            }*/
            if (soul != null && isValid(spell) && !player.isCreative() ) {
                double mana = soul.getMP();
                if(mana < getCost(spell)) {
                    cancel(event);
                    Aterna.translate(player, true, "mana.not_enough");
                } else {
                    soul.addMana(player, -getCost(spell) * event.getModifiers().get(Sage.CHANT_COST));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void gainManaPool(SpellCastEvent.Post event){
        if(event.getCaster() instanceof EntityPlayerMP && !event.isCanceled() && sourceList.contains(event.getSource())  ) {
            EntityPlayer player = (EntityPlayer) event.getCaster();
            ISoul soul = Mana.getSoul(player);
            Spell spell = event.getSpell();
            if(soul != null && isValid(spell)){
                double maxMana = soul.getMaxMP();
                double value = (maxMana + Math.max(Tales.mp.progression * (Tales.mp.progression_multiplier *
                        (getCost(spell) / maxMana)), Tales.mp.progression));
                soul.setMaxMP(player, Math.min(value, Tales.mp.max));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void gainManaPoolContinuous(SpellCastEvent.Finish event){
        if(event.getCaster() instanceof EntityPlayerMP && !event.isCanceled() && sourceList.contains(event.getSource())
                && event.getSpell().isContinuous ) {
            EntityPlayer player = (EntityPlayer) event.getCaster();
            ISoul soul = Mana.getSoul(player);
            Spell spell = event.getSpell();
            int castingTick = event.getCount();
            if(soul != null && isValid(spell)){
                double maxMana = soul.getMaxMP();
                double value = (maxMana + Math.max(Tales.mp.progression * (Tales.mp.progression_multiplier *
                        (getContinuousProgression(spell, castingTick) / maxMana)), Tales.mp.progression));
                soul.setMaxMP(player, Math.min(value, Tales.mp.max));
            }
        }
    }

    public static double getCost(Spell spell){
        return Tales.mp.spell_multiplier * (Tales.mp.isCastingCostBased ? spell.getCost() : ((spell.getTier().ordinal()+1) * spell.getTier().ordinal()+1));
    }

    /** Distributes the given cost (which should be the per-second cost of a continuous spell) over a second and
     * returns the appropriate cost to be applied for the given tick. Currently, the cost is distributed over 2
     * intervals per second, meaning the returned value is 0 unless {@code castingTick} is a multiple of 10.*/
    protected static int getDistributedCost(Spell spell, int castingTick){
        double cost = getCost(spell);
        int partialCost;

        if(castingTick % 20 == 0){ // Whole number of seconds has elapsed
            partialCost = (int) (cost / 2 + cost % 2); // Make sure cost adds up to the correct value by adding the remainder here
        }else if(castingTick % 10 == 0){ // Something-and-a-half seconds has elapsed
            partialCost = (int) (cost/2);
        }else{ // Some other number of ticks has elapsed
            partialCost = 0; // Wands aren't damaged within half-seconds
        }

        return partialCost;
    }

    /** Version for mana pool progression **/
    protected static double getContinuousProgression(Spell spell, int castingTick){
        return getCost(spell) * (castingTick/20.0D); // Because 20 ticks is one second, then second = cost of spell; We gather all seconds!
    }

    public static boolean isValid(Spell spell){
        return spell != Spells.none && spell.getCost() > 0;
    }

}
