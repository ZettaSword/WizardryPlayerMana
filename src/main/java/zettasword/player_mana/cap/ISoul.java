package zettasword.player_mana.cap;

import net.minecraft.entity.player.EntityPlayer;

public interface ISoul {
 void addMana(EntityPlayer player, double add);
 void setMP(EntityPlayer player, double set);
 void addMaxMana(EntityPlayer player, double add);
 void setMaxMP(EntityPlayer player, double set);


 void addMana(double add);
 void setMP(double set);
 void addMaxMana(double add);
 void setMaxMP(double set);

 double getMP();
 double getMaxMP();




 void sync(EntityPlayer player);
 void syncMana(EntityPlayer player, double mana);
 void syncMaxMana(EntityPlayer player, double maxMana);
}