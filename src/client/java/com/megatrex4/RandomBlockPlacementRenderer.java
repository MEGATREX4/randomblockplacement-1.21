package com.megatrex4;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;

public class RandomBlockPlacementRenderer {
    private static final Identifier TEXTURE = Identifier.of("randomblockplacement", "textures/gui/rblock.png");

    public static void renderTexture(DrawContext drawContext, int screenWidth, int screenHeight) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.getTextureManager().bindTexture(TEXTURE);

        int textureWidth = 16;
        int textureHeight = 16;
        int x = (screenWidth / 2) - (textureWidth / 2);
        int y = (screenHeight / 2) - (textureHeight / 2);

        MatrixStack matrixStack = drawContext.getMatrices();
        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();

        // Set up alpha blending
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F); // Set alpha to 0.5 for semi-translucent

        // Draw the texture
        drawContext.drawTexture(TEXTURE, x, y - 20, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

        // Disable alpha blending after rendering
        RenderSystem.disableBlend();
    }
}
