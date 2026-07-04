package com.Fsq_tconstruct.fsq_TC;

import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;

import static net.minecraft.util.FastColor.ARGB32.*;

public class PaletteHelper {

    public static GreyToColorMapping fromMidColor(int midColorARGB) {
        int a = alpha(midColorARGB);
        int r = red(midColorARGB);
        int g = green(midColorARGB);
        int b = blue(midColorARGB);

        return GreyToColorMapping.builderFromBlack()
                .addARGB(63,  color(a, lerp(0, r, 63, 216), lerp(0, g, 63, 216), lerp(0, b, 63, 216)))
                .addARGB(102, color(a, lerp(0, r, 102, 216), lerp(0, g, 102, 216), lerp(0, b, 102, 216)))
                .addARGB(140, color(a, lerp(0, r, 140, 216), lerp(0, g, 140, 216), lerp(0, b, 140, 216)))
                .addARGB(178, color(a, lerp(0, r, 178, 216), lerp(0, g, 178, 216), lerp(0, b, 178, 216)))
                .addARGB(216, midColorARGB)
                .addARGB(255, color(a, lighten(r), lighten(g), lighten(b)))
                .build();
    }

    private static int lerp(int from, int to, int step, int total) {
        return from + (to - from) * step / total;
    }

    private static int lighten(int channel) {
        return channel + (255 - channel) * 40 / 100;
    }
}
