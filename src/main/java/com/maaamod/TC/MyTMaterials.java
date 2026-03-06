package com.maaamod.TC;
import slimeknights.tconstruct.library.materials.Material;
public class MyTMaterials extends Material {
    public MyTMaterials(String identifier, String color) {//color16进制色码不要'#'号
        super(identifier, Integer.parseInt(color, 16));
    }
}
