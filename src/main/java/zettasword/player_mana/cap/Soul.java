package zettasword.player_mana.cap;

import zettasword.player_mana.PlayerMana;
import zettasword.player_mana.network.PacketManager;
import zettasword.player_mana.Tales;
import zettasword.player_mana.network.packets.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class Soul implements ISoul {
 private double MP = 0.0D;
 private double maxMP = Tales.mp.initial;

 @Override
 public void addMana(double add) { MP=Math.min(Math.max(MP + add, 0), maxMP);
 }
 @Override
 public void addMana(EntityPlayer player, double add) { MP=Math.min(Math.max(MP + add, 0), maxMP); syncMana(player, MP);
 }

 @Override
 public void setMP(double set) { MP=Math.min(Math.max(set, 0), maxMP); }

 @Override
 public void setMP(EntityPlayer player, double set) { MP=Math.min(Math.max(set, 0), maxMP); syncMana(player, MP);
 }

 @Override
 public void setMaxMP(double set) { maxMP =Math.min(set, Tales.mp.max);}

 @Override
 public void setMaxMP(EntityPlayer player, double set) { maxMP = Math.min(set, Tales.mp.max); syncMaxMana(player, maxMP);
 }

 @Override
 public void addMaxMana(double add) { maxMP =Math.min(Math.max(maxMP + add, 0), Tales.mp.max);}

 @Override
 public void addMaxMana(EntityPlayer player, double add) { maxMP=Math.min(Math.max(maxMP + add, 0), Tales.mp.max);
  syncMaxMana(player, maxMP);
 }

 @Override
 public double getMP() { return MP; }

 @Override
 public double getMaxMP() { return maxMP; }

 @Override
 public void sync(EntityPlayer player) {
  if(player instanceof EntityPlayerMP){
   ISoul soul = player.getCapability(SoulProvider.SOUL_CAP, null);
   if (soul == null){
    PlayerMana.log.warn("[Player Mana soul]: LOST SOUL " + player.getGameProfile().getName()); //TODO: Remove after testing.
    return;
   }
   PacketManager.net.sendTo(new PacketSoulMana(this.MP), (EntityPlayerMP) player);
   PacketManager.net.sendTo(new PacketSoulMaxMana(this.maxMP), (EntityPlayerMP) player);
  }
 }

 @Override
 public void syncMana(EntityPlayer player, double mana) {
  if(player instanceof EntityPlayerMP){
   ISoul soul = player.getCapability(SoulProvider.SOUL_CAP, null);
   assert soul != null;
      PacketManager.net.sendTo(new PacketSoulMana(mana), (EntityPlayerMP) player);
  }
 }

 @Override
 public void syncMaxMana(EntityPlayer player, double maxMana) {
  if(player instanceof EntityPlayerMP){
   ISoul soul = player.getCapability(SoulProvider.SOUL_CAP, null);
   assert soul != null;
   PacketManager.net.sendTo(new PacketSoulMaxMana(maxMana), (EntityPlayerMP) player);
  }
 }
}