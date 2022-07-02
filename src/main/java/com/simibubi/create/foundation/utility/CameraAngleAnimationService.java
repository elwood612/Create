package com.simibubi.create.foundation.utility;

import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import net.minecraft.client.Minecraft;

public class CameraAngleAnimationService {

	private static final LerpedFloat yRotation = LerpedFloat.angular().startWithValue(0);
	private static final LerpedFloat xRotation = LerpedFloat.angular().startWithValue(0);

	private static Mode animationMode = Mode.LINEAR;
	private static float animationSpeed = -1;

	public static void tick() {

		if (Minecraft.getInstance().player != null) {
			if (!yRotation.settled())
				Minecraft.getInstance().player.setYRot(yRotation.getValue(1));

			if (!xRotation.settled())
				Minecraft.getInstance().player.setXRot(xRotation.getValue(1));
		}

		yRotation.tickChaser();
		xRotation.tickChaser();
	}

	public static boolean isYawAnimating() {
		return !yRotation.settled();
	}

	public static boolean isPitchAnimating() {
		return !xRotation.settled();
	}

	public static float getYaw(float partialTicks) {
		return yRotation.getValue(partialTicks);
	}

	public static float getPitch(float partialTicks) {
		return xRotation.getValue(partialTicks);
	}

	public static void setAnimationMode(Mode mode) {
		animationMode = mode;
	}

	public static void setAnimationSpeed(float speed) {
		animationSpeed = speed;
	}

	public static void setYawTarget(float yaw) {
		yRotation.startWithValue(getCurrentYaw());
		setupChaser(yRotation, yaw);
	}

	public static void setPitchTarget(float pitch) {
		xRotation.startWithValue(getCurrentPitch());
		setupChaser(xRotation, pitch);
	}

	private static float getCurrentYaw() {
		if (Minecraft.getInstance().player == null)
			return 0;

		return Minecraft.getInstance().player.getYRot();
	}

	private static float getCurrentPitch() {
		if (Minecraft.getInstance().player == null)
			return 0;

		return Minecraft.getInstance().player.getXRot();
	}

	private static void setupChaser(LerpedFloat rotation, float target) {
		if (animationMode == Mode.LINEAR) {
			rotation.chase(target, animationSpeed > 0 ? animationSpeed : 2, LerpedFloat.Chaser.LINEAR);
		} else if (animationMode == Mode.EXPONENTIAL) {
			rotation.chase(target, animationSpeed > 0 ? animationSpeed : 0.25, LerpedFloat.Chaser.EXP);
		}
	}

	public enum Mode {
		LINEAR,
		EXPONENTIAL
	}
}
