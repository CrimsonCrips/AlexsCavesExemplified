#version 150

// by nimitz (stormoid.com) (twitter: @stormoid)

uniform float iTime;
uniform vec2 RegionSize;
uniform vec2 BoxSize;
uniform float Margin;
uniform float Dissipation;
uniform float PatternUnit;
uniform float Opacity;
uniform vec3 Tint;
uniform float TintStrength;

in vec2 texCoord;
out vec4 fragColor;

const float NOISE_SIZE = 256.0;
const float RING_BASE = 2.0;

mat2 makem2(in float theta) {
    float c = cos(theta);
    float s = sin(theta);
    return mat2(c, -s, s, c);
}

float hash(vec2 cell) {
    cell = mod(cell, NOISE_SIZE);
    return fract(sin(dot(cell, vec2(127.1, 311.7))) * 43758.5453);
}

float noise(in vec2 x) {
    vec2 p = x * 0.01 * NOISE_SIZE - 0.5;
    vec2 i = floor(p);
    vec2 f = p - i;
    return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), f.x),
               mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), f.x), f.y);
}

float fbm(in vec2 p) {
    float z = 2.0;
    float rz = 0.0;
    for (float i = 1.0; i < 6.0; i++) {
        rz += abs((noise(p) - 0.5) * 2.0) / z;
        z = z * 2.0;
        p = p * 2.0;
    }
    return rz;
}

float dualfbm(in vec2 p, float time) {
    vec2 p2 = p * 0.7;
    vec2 basis = vec2(fbm(p2 - time * 1.6), fbm(p2 + time * 1.7));
    basis = (basis - 0.5) * 0.2;
    p += basis;
    return fbm(p * makem2(time * 0.2));
}

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

    float boxDistance = length(max(outside, 0.0));
    float fade = exp(-boxDistance / Dissipation) * (1.0 - smoothstep(0.75 * Margin, Margin, boxDistance));
    if (fade * Opacity < 0.002) {
        discard;
    }

    vec2 p = centered / PatternUnit;
    float rz = dualfbm(p, iTime);
    float electric = clamp(pow(abs(0.25 / (rz * RING_BASE)), 0.99), 0.0, 1.0);

    fragColor = vec4(rainbow(fract(atan(centered.y, centered.x) / 6.2831853 + iTime * 0.2 + rz * 0.3)), electric * fade * Opacity);
}
