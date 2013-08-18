package hunternif.mc.dota2items.util;

import hunternif.mc.dota2items.Config;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.item.Dota2Item;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.FMLLog;

public class DescriptionBuilder {
	/** Example: "+{%s%%} Mana Regeneration" <br>
	 * Text in {} will be marked gold.
	 * Text in [] will be marked dark gray. */
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface BuffLineFormat {
		String value();
	}
	/** Same color formatting as in {@link BuffLineFormat} */
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Description {
		String value();
	}
	
	public static void build() {
		for (Item item : Dota2Items.itemList) {
			if (item instanceof Dota2Item) {
				List<String> lines = new ArrayList<String>();
				Description desAn = item.getClass().getAnnotation(Description.class);
				if (desAn != null) {
					String description = desAn.value();
					// Build list of strings from the description string
					description = applyColorFormatting(description.replace("\n", " \n "));
					FontRenderer font = Minecraft.getMinecraft().fontRenderer;
					String[] descrWords = description.split(" ");
					int curLineWidth = 0;
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < descrWords.length; i++) {
						int wordWidth = font.getStringWidth(descrWords[i]);
						// At least one word will always fit:
						if ((curLineWidth > 0 && curLineWidth + wordWidth > Dota2Item.maxTooltipWidth) || descrWords[i].equals("\n")) {
							curLineWidth = 0;
							lines.add(sb.toString());
							sb = new StringBuilder();
						}
						if (!descrWords[i].equals("\n")) {
							curLineWidth += font.getStringWidth(descrWords[i] + " ");
							sb.append(descrWords[i]).append(" ");
						}
					}
					if (sb.length() > 0) {
						lines.add(sb.toString());
					}
				}
				Buff buff = ((Dota2Item) item).passiveBuff;
				if (buff != null) {
					lines.addAll(buffDescription(buff));
				}
				((Dota2Item) item).descriptionLines = lines;
				FMLLog.log(Dota2Items.ID, Level.INFO, "Built description lines for item %s", Config.forClass(item.getClass()).name);
			}
		}
	}
	
	public static List<String> buffDescription(Buff buff) {
		List<String> lines = new ArrayList<String>();
		try {
			Field[] fields = Buff.class.getFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(BuffLineFormat.class)) {
					String format = field.getAnnotation(BuffLineFormat.class).value();
					format = applyColorFormatting(format);
					Number value = (Number)field.get(buff);
					if (value.doubleValue() > 0) {
						String line = String.format(format, value);
						lines.add(line);
					}
				}
			}
		} catch (Exception e) {
			FMLLog.log(Dota2Items.ID, Level.WARNING, "Failed to build description for buff %s", buff.name);
		}
		return lines;
	}
	
	private static String applyColorFormatting(String str) {
		return str.replace("{", EnumChatFormatting.GOLD.toString())
				.replace("}", EnumChatFormatting.GRAY.toString())
				.replace("[", EnumChatFormatting.DARK_GRAY.toString())
				.replace("]", EnumChatFormatting.GRAY.toString());
	}
}
