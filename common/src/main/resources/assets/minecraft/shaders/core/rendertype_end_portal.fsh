#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:matrix.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

in vec4 texProj0;
in float sphericalVertexDistance;
in float cylindricalVertexDistance;

const vec3[] COLORS = vec3[](
    vec3(0.020000, 0.110000, 0.020000),
    vec3(0.010000, 0.090000, 0.010000),
    vec3(0.025000, 0.105000, 0.020000),
    vec3(0.040000, 0.120000, 0.030000),
    vec3(0.055000, 0.130000, 0.025000),
    vec3(0.060000, 0.150000, 0.040000),
    vec3(0.080000, 0.180000, 0.030000),
    vec3(0.090000, 0.200000, 0.040000),
    vec3(0.100000, 0.170000, 0.050000),
    vec3(0.120000, 0.190000, 0.060000),
    vec3(0.060000, 0.250000, 0.050000),
    vec3(0.150000, 0.220000, 0.040000),
    vec3(0.040000, 0.320000, 0.060000),
    vec3(0.180000, 0.380000, 0.050000),
    vec3(0.070000, 0.300000, 0.080000)
);

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.25,
    0.0, 0.5, 0.0, 0.25,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(Sampler0, texProj0).rgb * COLORS[0];
    for (int i = 0; i < PORTAL_LAYERS; i++) {
        color += textureProj(Sampler1, texProj0 * end_portal_layer(float(i + 1))).rgb * COLORS[i];
    }
    fragColor = apply_fog(vec4(color, 1.0), sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
