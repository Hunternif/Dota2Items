package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.network.Dota2PacketID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//TODO refactor Dota2Effect
public final class Dota2Effect {
	public static final int NONE = -1;
	
	public static final String BLINK = "blink";
	public static final String CYCLONE_RING = "cycloneRing";
	
	private static List<String> effectList = new ArrayList<String>();
	static {
		effectList.add(BLINK);
		effectList.add(CYCLONE_RING);
	}
	
	public static int getEffectId(String name) {
		int id = effectList.indexOf(name);
		if (id != -1) {
			return id;
		} else {
			return NONE;
		}
	}
	
	public static String getEffectName(int id) {
		try {
			return effectList.get(id);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	
	public String name;
	public double x;
	public double y;
	public double z;
	public Object[] miscData;
	/*public double velX;
	public double velY;
	public double velZ;*/
	
	public Dota2Effect(String name, double x, double y, double z, Object ... miscData) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.miscData = miscData;
		/*this.velX = velX;
		this.velY = velY;
		this.velZ = velZ;*/
	}
	
	public Dota2Effect(String name, double x, double y, double z) {
		this(name, x, y, z, (Object) null);
	}
	
	@SideOnly(Side.CLIENT)
	public void start() {
		World world = Minecraft.getMinecraft().theWorld;
		EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
		EntityFX effect = null;
		if (name.equals(BLINK)) {
			effect = new BlinkFX(world, x, y, z, effectRenderer);
		} else if (name.equals(CYCLONE_RING)) {
			if (miscData != null && miscData.length >= 2) {
				float yaw = (Float) miscData[0];
				float pitch = (Float) miscData[1];
				effect = new CycloneFXRing(world, x, y, z, yaw, pitch, effectRenderer);
				if (miscData.length > 2) {
					float alpha = (Float) miscData[2];
					effect.setAlphaF(alpha);
				}
			}
		}
		if (effect != null) {
			effectRenderer.addEffect(effect/*, Dota2Items.particlesDummyItem*/);
		}
	}
	
	public Packet250CustomPayload toPacket() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(Dota2PacketID.EFFECT);
			outputStream.writeInt(getEffectId(name));
			outputStream.writeInt(MathHelper.floor_double(x));
			outputStream.writeInt(MathHelper.floor_double(y));
			outputStream.writeInt(MathHelper.floor_double(z));
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = Dota2Items.CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		return packet;
	}
	
	public static Dota2Effect fromPacket(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		String name;
		double x;
		double y;
		double z;
		
		try {
			if (inputStream.readInt() != Dota2PacketID.EFFECT)
				return null;
			name = Dota2Effect.getEffectName(inputStream.readInt());
			x = ((double) inputStream.readInt()) + 0.5;
			y = ((double) inputStream.readInt()) + 0.5;
			z = ((double) inputStream.readInt()) + 0.5;
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new Dota2Effect(name, x, y, z);
	}
}
