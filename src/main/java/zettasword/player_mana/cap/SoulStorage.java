package zettasword.player_mana.cap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class SoulStorage implements Capability.IStorage<ISoul>
{
   @Override
   public NBTBase writeNBT(Capability<ISoul> capability, ISoul soul, EnumFacing side) {
       NBTTagCompound tag = new NBTTagCompound();

       tag.setDouble("MP", soul.getMP());
       tag.setDouble("maxMP", soul.getMaxMP());
    return tag;
   }

   @Override
   public void readNBT(Capability<ISoul> capability, ISoul soul, EnumFacing side, NBTBase nbt) {
       NBTTagCompound tag = (NBTTagCompound)nbt;

       soul.setMP(tag.getDouble("MP"));
       soul.setMaxMP(tag.getDouble("maxMP"));

   }
}