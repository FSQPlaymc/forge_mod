package com.Fsq_tconstruct.fsq_TC;

import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;

import java.awt.Color;

import static net.minecraft.util.FastColor.ARGB32.*;

public class PaletteHelper_1 {

    public static GreyToColorMapping fromMidColor(int midColorARGB) {
        int a = alpha(midColorARGB);
        int r = red(midColorARGB);
        int g = green(midColorARGB);
        int b = blue(midColorARGB);

        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        float hue = hsb[0];
        float sat = hsb[1];
        float bri = hsb[2];

        return GreyToColorMapping.builderFromBlack()
                .addARGB(63,  toARGB(a, hue, sat * 1.20f,  bri * 0.30f))
                .addARGB(102, toARGB(a, hue, sat * 1.10f,  bri * 0.45f))
                .addARGB(140, toARGB(a, hue, sat * 1.05f,  bri * 0.60f))
                .addARGB(178, toARGB(a, hue, sat * 1.00f,  bri * 0.78f))
                .addARGB(216, midColorARGB)
                .addARGB(255, toARGB(a, hue, sat * 0.85f,  Math.min(1f, bri * 1.18f)))
                .build();
    }

    private static int toARGB(int alpha, float hue, float saturation, float brightness) {
        saturation = clamp(saturation, 0f, 1f);
        brightness = clamp(brightness, 0f, 1f);
        int rgb = Color.HSBtoRGB(hue, saturation, brightness);
        int nr = (rgb >> 16) & 0xFF;
        int ng = (rgb >> 8) & 0xFF;
        int nb = rgb & 0xFF;
        return color(alpha, nr, ng, nb);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
