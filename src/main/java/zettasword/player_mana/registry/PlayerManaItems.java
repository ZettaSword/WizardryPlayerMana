package zettasword.player_mana.registry;

import electroblob.wizardry.item.ItemBlockMultiTextured;
import electroblob.wizardry.item.ItemWandUpgrade;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import zettasword.player_mana.PlayerMana;
import zettasword.player_mana.items.ItemPoolFlask;
import zettasword.player_mana.items.TalesWandUpgrade;

import javax.annotation.Nonnull;
import java.util.Objects;

@GameRegistry.ObjectHolder(PlayerMana.MODID)
@Mod.EventBusSubscriber
public final class PlayerManaItems {

	private PlayerManaItems() {
	} // No instances!

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }
	
	@GameRegistry.ObjectHolder("chant_upgrade_cost")
	public static final Item chant_upgrade_cost = placeholder();
	
	@GameRegistry.ObjectHolder("small_pool_flask")
	public static final Item small_pool_flask = placeholder();
	@GameRegistry.ObjectHolder("medium_pool_flask")
	public static final Item medium_pool_flask = placeholder();
	@GameRegistry.ObjectHolder("large_pool_flask")
	public static final Item large_pool_flask = placeholder();
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> reg = event.getRegistry();
		
		registerItem(reg, "chant_upgrade_cost", new TalesWandUpgrade(null));

		registerItem(reg, "small_pool_flask", new ItemPoolFlask(ItemPoolFlask.Size.SMALL));
		registerItem(reg, "medium_pool_flask", new ItemPoolFlask(ItemPoolFlask.Size.MEDIUM));
		registerItem(reg, "large_pool_flask", new ItemPoolFlask(ItemPoolFlask.Size.LARGE));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRegistryModel(ModelRegistryEvent e) {
		registryModel(small_pool_flask);
		registryModel(medium_pool_flask);
		registryModel(large_pool_flask);
		
	}

	// below registry methods are courtesy of EB
	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item) {
		registerItem(registry, name, item, false);
	}

	// below registry methods are courtesy of EB
	public static void registerItem(IForgeRegistry<Item> registry, String name, String modid, Item item) {
		registerItem(registry, name, modid, item, false);
	}

	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item, boolean setTabIcon) {
		item.setRegistryName(PlayerMana.MODID, name);
		item.setUnlocalizedName(PlayerMana.MODID + ":" + name);
		//item.setRegistryName(item.getRegistryName().toString());
		registry.register(item);

		if (setTabIcon && item.getCreativeTab() instanceof WizardryTabs.CreativeTabSorted) {
			((WizardryTabs.CreativeTabSorted) item.getCreativeTab()).setIconItem(new ItemStack(item));
		}

		if (item.getCreativeTab() instanceof WizardryTabs.CreativeTabListed) {
			((WizardryTabs.CreativeTabListed) item.getCreativeTab()).order.add(item);
		}
	}

	public static void registerItem(IForgeRegistry<Item> registry, String modid, String name, Item item, boolean setTabIcon) {
		item.setRegistryName(modid, name);
		item.setRegistryName(Objects.requireNonNull(item.getRegistryName()).toString());
		registry.register(item);

		if (setTabIcon && item.getCreativeTab() instanceof WizardryTabs.CreativeTabSorted) {
			((WizardryTabs.CreativeTabSorted) item.getCreativeTab()).setIconItem(new ItemStack(item));
		}

		if (item.getCreativeTab() instanceof WizardryTabs.CreativeTabListed) {
			((WizardryTabs.CreativeTabListed) item.getCreativeTab()).order.add(item);
		}
	}

	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		Item itemblock = new ItemBlock(block).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
		registry.register(itemblock);
	}

	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Item itemblock) {
		itemblock.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
		registry.register(itemblock);
	}

	private static void registerItemModel(Item item) {
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
		ModelLoader.setCustomMeshDefinition(item, s -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	@SideOnly(Side.CLIENT)
	private static void registryModel(Item item) {
		final ResourceLocation regName = item.getRegistryName();// Не забываем, что getRegistryName может вернуть Null!
		assert regName != null;
		final ModelResourceLocation mrl = new ModelResourceLocation(regName, "inventory");
		ModelBakery.registerItemVariants(item, mrl);// Регистрация вариантов предмета. Это нужно если мы хотим использовать подтипы предметов/блоков(см. статью подтипы)
		ModelLoader.setCustomModelResourceLocation(item, 0, mrl);// Устанавливаем вариант модели для нашего предмета. Без регистрации варианта модели, сама модель не будет установлена для предмета/блока(см. статью подтипы)
	}

	@SideOnly(Side.CLIENT)
	private static void registryModel(Item item, String regMame) {
		item.setRegistryName(regMame);
		item.setUnlocalizedName(regMame);
		final ResourceLocation regName = item.getRegistryName();
		assert regName != null;
		final ModelResourceLocation mrl = new ModelResourceLocation(regName, "inventory");
		ModelBakery.registerItemVariants(item, mrl);// Регистрация вариантов предмета. Это нужно если мы хотим использовать подтипы предметов/блоков(см. статью подтипы)
		ModelLoader.setCustomModelResourceLocation(item, 0, mrl);// Устанавливаем вариант модели для нашего предмета. Без регистрации варианта модели, сама модель не будет установлена для предмета/блока(см. статью подтипы)
	}

	/** Registers the given ItemBlock for the given block, with the same registry name as that block. This
	 * also automatically adds it to the order list for its creative tab if that tab is a {@link WizardryTabs.CreativeTabListed},
	 * meaning the order can be defined simply by the order in which the items are registered in this class. */
	private static void registerItemBlock(IForgeRegistry<Item> registry, String name, Block block, ItemBlock itemblock){
		// We don't need to keep a reference to the ItemBlock
		itemblock.setRegistryName( PlayerMana.MODID, name);
		registry.register(itemblock);

		if(block.getCreativeTabToDisplayOn() instanceof WizardryTabs.CreativeTabListed){
			((WizardryTabs.CreativeTabListed)block.getCreativeTabToDisplayOn()).order.add(itemblock);
		}
	}

	private static void registerMultiTexturedItemBlock(IForgeRegistry<Item> registry, Block block, boolean separateNames, String... prefixes){
		// We don't need to keep a reference to the ItemBlock
		Item itemblock = new ItemBlockMultiTextured(block, separateNames, prefixes).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
		registry.register(itemblock);

		if(block.getCreativeTabToDisplayOn() instanceof WizardryTabs.CreativeTabListed){
			((WizardryTabs.CreativeTabListed)block.getCreativeTabToDisplayOn()).order.add(itemblock);
		}
	}
}