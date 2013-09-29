package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.config.DescriptionBuilder;
import hunternif.mc.dota2items.core.AttackHandler;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.entity.EntityDagonBolt;
import hunternif.mc.dota2items.network.DagonBoltPacket;
import hunternif.mc.dota2items.util.NetworkUtil;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;

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
		entity.attackEntityFrom(new DamageSourceDagon("dagon", player), mcDamage);
		if (!player.worldObj.isRemote) {
			EntityDagonBolt bolt = new EntityDagonBolt(player.worldObj, this.level,
					player.posX, player.posY+1, player.posZ,
					entity.posX, (entity.boundingBox.maxY + entity.boundingBox.minY)/2d, entity.posZ);
			player.worldObj.spawnEntityInWorld(bolt);
			NetworkUtil.sendToAllAround(new DagonBoltPacket(bolt).makePacket(), player);
		}
	}
	
	public static class DamageSourceDagon extends EntityDamageSource {
		public DamageSourceDagon(String name, Entity entity) {
			super(name, entity);
			setDamageBypassesArmor();
			setMagicDamage();
		}
	}
}
