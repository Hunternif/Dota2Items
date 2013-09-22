package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.config.DescriptionBuilder;
import hunternif.mc.dota2items.core.AttackHandler;
import hunternif.mc.dota2items.core.buff.Buff;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class Dagon extends TargetEntityItem {
	public int level;
	public float damage;
	
	public Dagon(int id) {
		this(id, 1);
	}
	public Dagon(int id, int level) {
		super(id);
		this.level = level;
		setCooldown(40 - 5*level);
		setManaCost(200 - 20*level);
		this.damage = 300 + 100*level;
		setCastRange(12 + level);
		setPassiveBuff(new Buff("Dagon lvl"+level).setIntelligence(3).setAgility(2).setDamage(9).setIntelligence(14+2*level));
	}
	
	@Override
	public Dota2Item setDescriptionLines(List<String> lines) {
		lines.addAll(DescriptionBuilder.wrapDescriptionString("[Burst damage:] {"+String.valueOf((int)damage)+"}"));
		return super.setDescriptionLines(lines);
	}

	@Override
	protected void onUseOnEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
		player.worldObj.playSoundAtEntity(player, Sound.DAGON.getName(), 0.7f, 1);
		float mcDamage = damage / AttackHandler.DOTA_VS_MINECRAFT_DAMAGE;
		entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, entity), mcDamage);
	}
}
