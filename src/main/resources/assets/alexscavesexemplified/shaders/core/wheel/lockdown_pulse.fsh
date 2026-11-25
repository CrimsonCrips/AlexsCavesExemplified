#version 150

// by nimitz (stormoid.com) (twitter: @stormoid)

uniform float iTime;
uniform vec2 RegionSize;
uniform vec2 BoxSize;
uniform float Reach;
uniform float RingWidth;
uniform float Opacity;
uniform vec3 Tint;
uniform float TintStrength;

in vec2 texCoord;
out vec4 fragColor;

const float PULSE_DURATION = 1.636;
const float PULSE_PERIOD = 1.426;
const int MAX_RINGS = 3;
const float NOISE_BASE = 0.5;

vec3 rainbow(float hue) {
    vec3 k = clamp(abs(mod(hue * 6.0 + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0, 0.0, 1.0);
    vec3 tinted = mix(vec3(1.0), Tint, 0.6 + 0.4 * sin(hue * 6.2831853));
    return mix(mix(vec3(1.0), k, 0.85), tinted, TintStrength);
}

void main() {
    vec2 centered = (texCoord - 0.5) * RegionSize;
    vec2 outside = abs(centered) - 0.5 * BoxSize;
    float edgeDistance = max(outside.x, outside.y);
    if (edgeDistance <= 0.0) {
        discard;
    }

    float falloff = 1.0 - clamp(edgeDistance / Reach, 0.0, 1.0);
    float fade = falloff * falloff;
    if (fade * Opacity < 0.002) {
        discard;
    }

    float cycleTime = mod(iTime, PULSE_PERIOD);
    float pulse = 0.0;
    for (int i = 0; i < MAX_RINGS; i++) {
        float progress = (cycleTime + float(i) * PULSE_PERIOD) / PULSE_DURATION;
        if (progress >= 1.0) {
            break;
        }

        float ringDistance = progress * Reach;
        float shape = 0.2 + 3.7 * abs(edgeDistance - ringDistance) / RingWidth;
        float ring = pow(abs(0.1 - shape), 0.9);
        pulse = max(pulse, smoothstep(0.15, 1.0, clamp(pow(abs(0.25 / (NOISE_BASE * ring)), 0.99), 0.0, 1.0)));
    }
    if (pulse <= 0.0) {
        discard;
    }

    fragColor = vec4(rainbow(fract(atan(centered.y, centered.x) / 6.2831853 + iTime * 0.2)), pulse * fade * Opacity);
}
