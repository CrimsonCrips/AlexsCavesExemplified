#version 150

uniform float iTime;
uniform vec2 RegionSize;
uniform vec2 BoxSize;
uniform float Reach;
uniform float Opacity;
uniform vec3 Tint;
uniform float TintStrength;

in vec2 texCoord;
out vec4 fragColor;

vec3 rainbow(float hue) {
    vec3 k = clamp(abs(mod(hue * 6.0 + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0, 0.0, 1.0);
    vec3 tinted = mix(vec3(1.0), Tint, 0.6 + 0.4 * sin(hue * 6.2831853));
    return mix(mix(vec3(1.0), k, 0.85), tinted, TintStrength);
}

void main() {
    vec2 centered = (texCoord - 0.5) * RegionSize;
    vec2 outside = abs(centered) - 0.5 * BoxSize;
    if (outside.x <= 0.0 && outside.y <= 0.0) {
        discard;
    }

    float edgeDistance = length(max(outside, 0.0));
    float falloff = 1.0 - clamp(edgeDistance / Reach, 0.0, 1.0);
    float angle = atan(centered.y, centered.x);
    float shimmer = 0.75 + 0.25 * sin(angle * 6.0 - iTime * 3.0 + edgeDistance * 0.15);
    float alpha = falloff * falloff * shimmer * Opacity;
    if (alpha < 0.002) {
        discard;
    }

    fragColor = vec4(rainbow(fract(angle / 6.2831853 + iTime * 0.25)), alpha);
}
