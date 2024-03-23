package zettasword.player_mana;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Arrays;
import java.util.Locale;

@GameRegistry.ObjectHolder(PlayerMana.MODID)
@Config(modid = PlayerMana.MODID, type = Config.Type.INSTANCE, name = "PlayerMana")
public class Tales {

    @Config.Comment("Mana Pool System, which makes player really become master wizard slowly!")
    @Config.Name("Player Mana System")
    public static ManaPoolSystem mp = new ManaPoolSystem();

    public static class ManaPoolSystem {

        @Config.Comment("If it is true - then we see mana pool as bar")
        @Config.Name("0: Bar")
        public boolean manaPoolBar = true;
        @Config.Comment("If it is true - then mana from wands when you cast spell - will not be used")
        @Config.Name("Is items mana not used anymore?")
        public boolean noMoreManaUse = true;
        @Config.Comment("If it is true - then casting cost bases on spell cost, if it's false - then it bases of spell tier")
        @Config.Name("Is casting Cost based on Spell Cost?")
        public boolean isCastingCostBased = true;

        @Config.Comment("If it is true - then if your mana is near 0, you'll get debuffs.")
        @Config.Name("Are you get debuff when mana is low? ")
        public boolean lowOnMana = true;
        @Config.RangeDouble(min = 0.0D)
        @Config.Comment({"Allows to change cost of using spells, by multiplying final cost on this value (cost * this)",
                "Cost of spell is calculated: this * (Spell-Tier * Spell-Tier)", "So setting it to 0.0 will make spellcasting not use Mana Pool at all"})
        @Config.Name("1: Spell Cost Multiplier")
        public double spell_multiplier = 1.0D;

        @Config.RangeDouble(min = 1.0D)
        @Config.Comment("Max Mana in your pool available from the beginning. It'll grow as you cast spells up to Max. ")
        @Config.Name("1: Mana Pool Initial")
        public double initial = 10.0D;

        @Config.RangeDouble(min = 1.0D)
        @Config.Comment("Max Mana in your pool available at all. Can't be higher then this [value]")
        @Config.Name("1: Mana Pool Max")
        public double max = 2500.0D;

        @Config.RangeDouble(min = 0.0D)
        @Config.Comment({"Each successful cast increases Mana Pool, this way you can define how much Max mana added. Can't be higher then [Mana Pool Max]", "After successful cast, your progress is",
                "[This] * ([Progression Multiplier] * (spell-cost-for-pool / player-current-max-mana))"})
        @Config.Name("2: Mana Pool Progression")
        public double progression = 0.02D;

        @Config.RangeDouble(min = 1.0D)
        @Config.Comment({"Each successful cast increases Mana Pool, this way you can define how much Max mana added. Can't be higher then [Mana Pool Max]", "After successful cast, your progress is",
                "[This] * ([Progression Multiplier] * (spell-cost-for-pool / player-current-max-mana))"})
        @Config.Name("2: Mana Pool Progression Multiplier")
        public double progression_multiplier = 4.0D;

        @Config.RangeDouble(min = 0.0D)
        @Config.Comment("Each [Mana Pool Regeneration Frequency] seconds regenerates this [value] to your mana pool")
        @Config.Name("2: Mana Pool Regeneration")
        public double regeneration = 0.1D;

        @Config.RangeDouble(min = 0.0D)
        @Config.Comment("The maximum plus to your mana pool regen you can get. It scales of how close you are to Mana Pool Max")
        @Config.Name("2: Mana Pool Regeneration Bonus")
        public double bonus_regen = 1D;

        @Config.RangeDouble(min = 0.1D)
        @Config.Comment("Frequency of Mana Regeneration in seconds (0.5 = 10 ticks)")
        @Config.Name("2: Mana Pool Regeneration Seconds Frequency")
        public double seconds_frequency = 0.2D;

        @Config.RangeDouble(min = 0.0D)
        @Config.Comment("Spell Cost * This multiplier for Casting ring casting")
        @Config.Name("3: Casting Ring Cost Multiplier")
        public double casting_ring_cost = 1.0D;

        @Config.RangeInt(min = 0)
        @Config.Comment("Not to allow just spam too much with cast. 20 = 1 second")
        @Config.Name("3: Casting Ring Cooldown")
        public int casting_ring_cooldown = 20;

        @Config.RangeInt(min = 0)
        @Config.Comment("How much it gives mana upon usage?")
        @Config.Name("3: Mana Flask: Small")
        public int mana_flask_small = 75;

        @Config.RangeInt(min = 0)
        @Config.Comment("How much it gives mana upon usage?")
        @Config.Name("3: Mana Flask: Medium")
        public int mana_flask_medium = 350;

        @Config.RangeInt(min = 0)
        @Config.Comment("How much it gives mana upon usage?")
        @Config.Name("3: Mana Flask: Large")
        public int mana_flask_large = 1400;
    }

    /** Converts the given strings to an array of {@link ResourceLocation}s */
    public static ResourceLocation[] toResourceLocations(String[] strings){
        return Arrays.stream(strings).map(s -> new ResourceLocation(s.toLowerCase(Locale.ROOT).trim())).toArray(ResourceLocation[]::new);
    }

    /** Converts the given string to {@link ResourceLocation} */
    public static ResourceLocation toResourceLocation(String string){
        return new ResourceLocation(string.toLowerCase(Locale.ROOT).trim());
    }
}
