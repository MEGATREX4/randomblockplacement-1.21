package com.megatrex4;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.Random;

public class RandomBlockPlacementClient implements ClientModInitializer {
	private static boolean randomPlacementMode = false;
	private static final Random random = new Random();
	private boolean wasRightClicking = false;
	private static final RandomBlockPlacementClient INSTANCE = new RandomBlockPlacementClient();

	private static final Identifier ICON_TEXTURE = Identifier.of("randomblockplacement", "textures/gui/rblock.png");

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
				renderIcon(drawContext);
			}
		});
	}

	public static void onRandomPlaceKeyPressed() {
		randomPlacementMode = !randomPlacementMode;


//		MinecraftClient client = MinecraftClient.getInstance();
//		String translationKey = randomPlacementMode
//				? "randomblockplacement.enabled"
//				: "randomblockplacement.disabled";
//
//		if (client.player != null) {
//			client.player.sendMessage(Text.translatable(translationKey), true);
//		}
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

	private void renderIcon(DrawContext drawContext) {
		MinecraftClient client = MinecraftClient.getInstance();
		int screenWidth = client.getWindow().getScaledWidth();
		int screenHeight = client.getWindow().getScaledHeight();

		int iconSize = 16;
		int x = (screenWidth - iconSize) / 2; // Center horizontally
		int y = (screenHeight - iconSize) / 2 - 13; // Slightly above the crosshair

		// Use getGuiTextured for textures
		drawContext.drawTexture(
				texture -> RenderLayer.getGuiTextured(ICON_TEXTURE), // Correct RenderLayer
				ICON_TEXTURE, // Texture identifier
				x, y,         // Position on screen
				0.0f, 0.0f,   // Texture coordinates
				iconSize, iconSize, // Texture width and height
				iconSize, iconSize  // Actual texture dimensions
		);
	}



}
