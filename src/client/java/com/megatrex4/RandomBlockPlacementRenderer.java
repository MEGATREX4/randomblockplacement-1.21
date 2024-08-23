package com.megatrex4;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class RandomBlockPlacementRenderer {
    private static final Identifier TEXTURE = Identifier.of("randomblockplacement", "textures/gui/rblock.png");


    public static void renderTexture(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();

        int textureWidth = 16;
        int textureHeight = 16;

        int x = (client.getWindow().getScaledWidth() / 2) - (textureWidth / 2);
        int y = (client.getWindow().getScaledHeight() / 2) - (textureHeight / 2);

        // Bind the texture and draw it on the screen
        drawContext.drawTexture(TEXTURE, x, y - 20, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
}
