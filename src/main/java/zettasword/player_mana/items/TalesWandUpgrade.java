package zettasword.player_mana.items;

import electroblob.wizardry.item.ItemWandUpgrade;
import electroblob.wizardry.registry.WizardryTabs;
import electroblob.wizardry.util.WandHelper;
import net.minecraft.creativetab.CreativeTabs;
import zettasword.player_mana.registry.PlayerManaItems;

public class TalesWandUpgrade extends ItemWandUpgrade {
    public TalesWandUpgrade(){
        super();
        setCreativeTab(WizardryTabs.WIZARDRY);
    }
    public TalesWandUpgrade(CreativeTabs tab){
        super();
        setCreativeTab(tab);
    }

    public static void init() {
        WandHelper.registerSpecialUpgrade(PlayerManaItems.chant_upgrade_cost, "chant_upgrade_cost");
    }
}
