package com.megatrex4;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class RandomBlockPlacementClient implements ClientModInitializer {
	private static boolean randomPlacementMode = false;
	private static final Random random = new Random();

	@Override
	public void onInitializeClient() {
		KeyBindings.registerKeyBindings();
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (player instanceof ClientPlayerEntity clientPlayer) {
				if (randomPlacementMode && clientPlayer.getMainHandStack().getItem() instanceof BlockItem) {
					if (hitResult instanceof BlockHitResult blockHitResult) {
						BlockPos blockPos = blockHitResult.getBlockPos();
						if (!world.getBlockState(blockPos).isAir()) {
							randomizeHotbarSlot(clientPlayer);
							return ActionResult.SUCCESS;
						}
					}
				}
			}
			return ActionResult.PASS;
		});
	}

	public static void onRandomPlaceKeyPressed() {
		randomPlacementMode = !randomPlacementMode;

		MinecraftClient client = MinecraftClient.getInstance();
		String message = randomPlacementMode ? "Random Placement Mode Enabled" : "Random Placement Mode Disabled";
		client.player.sendMessage(Text.literal(message), true);
	}

	private static void randomizeHotbarSlot(ClientPlayerEntity player) {
		int currentSlot = player.getInventory().selectedSlot;
		int randomSlot;

		do {
			randomSlot = random.nextInt(9);
		} while (randomSlot == currentSlot || !(player.getInventory().getStack(randomSlot).getItem() instanceof BlockItem));

		player.getInventory().selectedSlot = randomSlot;
	}
}
