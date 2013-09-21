package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.config.DescriptionBuilder;
import hunternif.mc.dota2items.core.AttackHandler;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.event.UseItemEvent;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;

public class Dagon extends CooldownItem {
	public int level;
	public float damage;
	/** [blocks] */
	public double range;
	
	public Dagon(int id) {
		this(id, 1);
	}
	public Dagon(int id, int level) {
		super(id);
		this.level = level;
		setCooldown(40 - 5*level);
		setManaCost(200 - 20*level);
		this.damage = 300 + 100*level;
		this.range = 12 + level;
		setPassiveBuff(new Buff("Dagon lvl"+level).setIntelligence(3).setAgility(2).setDamage(9).setIntelligence(14+2*level));
	}
	
	@Override
	public Dota2Item setDescriptionLines(List<String> lines) {
		lines.addAll(DescriptionBuilder.wrapDescriptionString("[Burst damage:] {"+String.valueOf((int)damage)+"}"));
		return super.setDescriptionLines(lines);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!tryUse(stack, player, entity)) {
			return false;
		}
		MinecraftForge.EVENT_BUS.post(new UseItemEvent(player, this));
		player.worldObj.playSoundAtEntity(player, Sound.DAGON.getName(), 0.7f, 1);
		startCooldown(stack, player);
		Dota2Items.stats.getOrCreateEntityStats(player).removeMana(getManaCost());
		float mcDamage = damage / AttackHandler.DOTA_VS_MINECRAFT_DAMAGE;
		entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, entity), mcDamage);
		return true;
	}
}
