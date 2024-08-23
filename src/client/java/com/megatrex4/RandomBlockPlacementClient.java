package com.megatrex4;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.hit.BlockHitResult;

import java.util.Random;

public class RandomBlockPlacementClient implements ClientModInitializer {
	private static boolean randomPlacementMode = false;
	private static final Random random = new Random();
	private boolean wasPlacingBlock = false;

	@Override
	public void onInitializeClient() {
		KeyBindings.registerKeyBindings();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && client.currentScreen == null) {
				boolean isRightClicking = client.options.useKey.isPressed();
				boolean isPlacingBlock = isRightClicking && !wasPlacingBlock;

				if (randomPlacementMode && isPlacingBlock) {
					handleBlockPlacement(client.player);
					wasPlacingBlock = true;
				} else if (!isRightClicking) {
					wasPlacingBlock = false;
				}
			}
		});
	}

	public static void onRandomPlaceKeyPressed() {
		randomPlacementMode = !randomPlacementMode;

		MinecraftClient client = MinecraftClient.getInstance();
		String message = randomPlacementMode ? "Enabled" : "Disabled";
		if (client.player != null) {
			client.player.sendMessage(Text.literal(message), true);
		}
	}

	private void handleBlockPlacement(ClientPlayerEntity player) {
		if (player.getMainHandStack().getItem() instanceof BlockItem) {
			BlockHitResult hitResult = (BlockHitResult) player.raycast(20, 0, false);
			BlockPos blockPos = hitResult.getBlockPos();

			if (!player.clientWorld.getBlockState(blockPos).isAir()) {
				randomizeHotbarSlot(player);
			}
		}
	}

	private static void randomizeHotbarSlot(ClientPlayerEntity player) {
		int currentSlot = player.getInventory().selectedSlot;
		int blockCount = 0;

		// Count the number of block items in the hotbar
		for (int i = 0; i < 9; i++) {
			if (player.getInventory().getStack(i).getItem() instanceof BlockItem) {
				blockCount++;
			}
		}

		// If there's only one block item, do not change the slot
		if (blockCount <= 1) {
			return;
		}

		int randomSlot;
		do {
			randomSlot = random.nextInt(9);
		} while (randomSlot == currentSlot || !(player.getInventory().getStack(randomSlot).getItem() instanceof BlockItem));

		player.getInventory().selectedSlot = randomSlot;
	}
}
