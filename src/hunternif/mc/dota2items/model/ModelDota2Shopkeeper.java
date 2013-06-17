package hunternif.mc.dota2items.model;

import hunternif.mc.dota2items.util.MathUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelDota2Shopkeeper extends ModelBase {
	private static final float RIGHT_ARM_BASE_ROTATION = -1.1f;
	private static final float PI_2 = (float)Math.PI / 2f;
	
	public ModelRenderer head;
	public ModelRenderer body;
	public ModelRenderer nose;
	public ModelRenderer rightarm;
	public ModelRenderer leftarm;
	public ModelRenderer rightleg;
	public ModelRenderer leftleg;
	public ModelRenderer clothes;
	
	public ModelRenderer staff;
	public ModelRenderer cage;
	
	public ModelDota2Shopkeeper() {
		textureWidth = 64;
		textureHeight = 64;
		
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8, 8, 8);
		head.setRotationPoint(0F, 0F, 0F);
		head.setTextureSize(64, 64);
		
		nose = new ModelRenderer(this, 24, 0);
		nose.setRotationPoint(0.0F, -2.0F, 0.0F);
		nose.addBox(-1.0F, -2.0F, -6.0F, 2, 4, 2);
		head.addChild(nose);
		
		body = new ModelRenderer(this, 0, 16);
		body.addBox(-5F, 0F, -3F, 10, 14, 6);
		body.setRotationPoint(0F, 0F, 0F);
		body.setTextureSize(64, 64);
		
		rightarm = new ModelRenderer(this, 40, 36);
		rightarm.addBox(-4F, -2F, -2F, 4, 14, 4);
		rightarm.setRotationPoint(-6F, 3F, 0F);
		rightarm.setTextureSize(64, 64);
		rightarm.rotateAngleX = RIGHT_ARM_BASE_ROTATION;
		
		leftarm = new ModelRenderer(this, 40, 36);
		leftarm.addBox(0F, -2F, -2F, 4, 14, 4);
		leftarm.setRotationPoint(6F, 3F, 0F);
		leftarm.setTextureSize(64, 64);
		leftarm.mirror = true;
		
		rightleg = new ModelRenderer(this, 40, 20);
		rightleg.addBox(-2F, 0F, -2F, 4, 12, 4);
		rightleg.setRotationPoint(-3F, 12F, 0F);
		rightleg.setTextureSize(64, 64);
		
		leftleg = new ModelRenderer(this, 40, 20);
		leftleg.addBox(-2F, 0F, -2F, 4, 12, 4);
		leftleg.setRotationPoint(3F, 12F, 0F);
		leftleg.setTextureSize(64, 64);
		leftleg.mirror = true;
		
		clothes = new ModelRenderer(this, 0, 36);
		clothes.addBox(-6F, 0F, -4F, 12, 18, 8);
		clothes.setRotationPoint(0F, 0F, 0F);
		clothes.setTextureSize(64, 64);
		
		staff = new ModelRenderer(this, 56, 18);
		staff.setRotationPoint(-2, 10, 2);
		staff.addBox(-1F, -17F, -1F, 2, 34, 2);
		staff.setTextureSize(64, 64);
		staff.rotateAngleX = PI_2;
		rightarm.addChild(staff);
		
		cage = new ModelRenderer(this, 40, 0);
		cage.addBox(-3F, -12F, -3F, 6, 12, 6);
		cage.setRotationPoint(0, -17F, 0F);
		cage.setTextureSize(64, 64);
		staff.addChild(cage);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		head.render(f5);
		body.render(f5);
		rightarm.render(f5);
		leftarm.render(f5);
		rightleg.render(f5);
		leftleg.render(f5);
		clothes.render(f5);
		//TODO render parrot inside the cage
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		head.rotateAngleY = f3 / (180F / (float)Math.PI);
		head.rotateAngleX = f4 / (180F / (float)Math.PI);
		
		rightarm.rotateAngleX = -PI_2 + MathHelper.cos(f * 0.6662F + (float)Math.PI) * 2.0F * f1 * 0.25F;
		rightarm.rotateAngleZ = 0;
		leftarm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
		leftarm.rotateAngleZ = 0;
		
		rightleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1 * 0.5F;
		rightleg.rotateAngleY = 0.0F;
		leftleg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1 * 0.5F;
		leftleg.rotateAngleY = 0.0F;
	}

}
