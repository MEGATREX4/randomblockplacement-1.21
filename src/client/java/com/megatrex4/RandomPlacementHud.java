package com.megatrex4;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

public class RandomPlacementHud implements HudRenderCallback {

    private static boolean randomPlacementMode = false;

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        if (randomPlacementMode) {
            renderIcon(drawContext);
        }
    }

    private void renderIcon(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getWindow() == null) {
            return;
        }

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int x = screenWidth / 2;
        int y = screenHeight / 2 - 15;

        // Enable transparency
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Set the color with half transparency (alpha = 128, RGB = white)
        int color = 0x80FFFFFF;  // Alpha = 128, RGB = white

        // Example of texture binding with transparency
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        AbstractTexture texture = textureManager.getTexture(Identifier.of("randomblockplacement", "textures/gui/rblock.png"));

        // Apply the texture with transparency to the font rendering
        drawContext.drawText(
                client.textRenderer,
                "\uFD46",  // The custom character from the texture
                x - 16 / 2,
                y,
                color,
                true
        );

        // Disable blending after rendering
        RenderSystem.disableBlend();
    }


    public static void toggleRandomPlacementMode() {
        randomPlacementMode = !randomPlacementMode;
    }

    public static void registerHud() {
        HudRenderCallback.EVENT.register(new RandomPlacementHud());
    }
}
