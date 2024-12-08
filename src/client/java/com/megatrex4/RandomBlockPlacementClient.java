package com.megatrex4;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;

import java.util.Random;

public class RandomBlockPlacementClient implements ClientModInitializer {
	private static boolean randomPlacementMode = false;
	private static final Random random = new Random();
	private boolean wasRightClicking = false;
	private static final RandomBlockPlacementClient INSTANCE = new RandomBlockPlacementClient();

	@Override
	public void onInitializeClient() {
		KeyBindings.registerKeyBindings();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && client.currentScreen == null) {
				boolean isRightClicking = client.options.useKey.isPressed();
				boolean isPlacingBlock = isRightClicking && !wasRightClicking;

				if (randomPlacementMode && isPlacingBlock) {
					handleBlockPlacement(client.player);
					wasRightClicking = true;
				} else if (!isRightClicking) {
					wasRightClicking = false;
				}
			}
		});

		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
			if (randomPlacementMode) {
				int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
				int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
			}
		});
	}

	public static void onRandomPlaceKeyPressed() {
		randomPlacementMode = !randomPlacementMode;

		MinecraftClient client = MinecraftClient.getInstance();
		String translationKey = randomPlacementMode
				? "randomblockplacement.enabled"
				: "randomblockplacement.disabled";

		if (client.player != null) {
			client.player.sendMessage(Text.translatable(translationKey), true);
		}
	}


	public void handleBlockPlacement(ClientPlayerEntity player) {
		if (randomPlacementMode && player.getMainHandStack().getItem() instanceof BlockItem) {
			randomizeHotbarSlot(player);
		}
	}

	private static void randomizeHotbarSlot(ClientPlayerEntity player) {
		int currentSlot = player.getInventory().selectedSlot;
		int blockCount = 0;

		for (int i = 0; i < 9; i++) {
			if (player.getInventory().getStack(i).getItem() instanceof BlockItem) {
				blockCount++;
			}
		}

		if (blockCount <= 1) {
			return;
		}

		int randomSlot;
		do {
			randomSlot = random.nextInt(9);
		} while (randomSlot == currentSlot || !(player.getInventory().getStack(randomSlot).getItem() instanceof BlockItem));

		player.getInventory().selectedSlot = randomSlot;
	}

	public static RandomBlockPlacementClient getInstance() {
		return INSTANCE;
	}
}
