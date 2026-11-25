#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform mat4 InvProjMat;
uniform vec3 SonarOrigin;
uniform float SonarRadius;
uniform float SonarRange;
uniform float SonarFade;

in vec2 texCoord;

out vec4 fragColor;

const vec3 SONAR_COLOR = vec3(0.35, 0.85, 1.0);
const float MIN_DISTANCE = 3.0;
const float RING_WIDTH = 1.5;
const float EDGE_SENSITIVITY = 0.08;

void main() {
    vec4 scene = texture(DiffuseSampler, texCoord);
    float depth = texture(DepthSampler, texCoord).r;
    vec4 view = InvProjMat * vec4(texCoord * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    vec3 p = view.xyz / view.w;
    float dist = length(p - SonarOrigin);
    float center = length(p);
    float edge = clamp(fwidth(center) / (center * EDGE_SENSITIVITY) - 0.5, 0.0, 1.0);
    if (depth >= 1.0 || dist < MIN_DISTANCE || dist > SonarRadius + RING_WIDTH * 3.0) {
        fragColor = scene;
        return;
    }
    float revealed = dist <= SonarRadius ? 1.0 : 0.0;
    float falloff = 1.0 - smoothstep(SonarRange * 0.6, SonarRange, dist);
    float surface = (0.2 + edge * 0.9) * revealed * falloff;
    float ring = exp(-pow((dist - SonarRadius) / RING_WIDTH, 2.0)) * (1.0 - SonarRadius / SonarRange * 0.5);
    fragColor = vec4(scene.rgb + SONAR_COLOR * (surface + ring) * SonarFade, scene.a);
}
