#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform mat4 InvProjMat;
uniform vec3 SonarOrigin;
uniform float SonarRadius;
uniform float SonarRange;
uniform float SonarFade;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

const vec3 SONAR_COLOR = vec3(0.35, 0.85, 1.0);
const float MIN_DISTANCE = 3.0;
const float RING_WIDTH = 1.5;
const float EDGE_SENSITIVITY = 0.04;

vec3 viewPos(vec2 uv) {
    float depth = texture(DepthSampler, uv).r;
    vec4 view = InvProjMat * vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    return view.xyz / view.w;
}

void main() {
    vec4 scene = texture(DiffuseSampler, texCoord);
    if (texture(DepthSampler, texCoord).r >= 1.0) {
        fragColor = scene;
        return;
    }
    vec3 p = viewPos(texCoord);
    float dist = length(p - SonarOrigin);
    if (dist < MIN_DISTANCE || dist > SonarRadius + RING_WIDTH * 3.0) {
        fragColor = scene;
        return;
    }

    vec3 right = viewPos(texCoord + vec2(oneTexel.x, 0.0));
    vec3 left = viewPos(texCoord - vec2(oneTexel.x, 0.0));
    vec3 up = viewPos(texCoord + vec2(0.0, oneTexel.y));
    vec3 down = viewPos(texCoord - vec2(0.0, oneTexel.y));

    vec3 normal = normalize(cross(right - left, up - down));
    float facing = abs(dot(normal, normalize(-p)));

    float center = length(p);
    float jump = abs(length(right) - center) + abs(length(left) - center) + abs(length(up) - center) + abs(length(down) - center);
    float edge = clamp(jump / (center * EDGE_SENSITIVITY) - 0.5, 0.0, 1.0);

    float revealed = dist <= SonarRadius ? 1.0 : 0.0;
    float falloff = 1.0 - smoothstep(SonarRange * 0.6, SonarRange, dist);
    float surface = (0.12 + facing * 0.28 + edge * 0.9) * revealed * falloff;
    float ring = exp(-pow((dist - SonarRadius) / RING_WIDTH, 2.0)) * (1.0 - SonarRadius / SonarRange * 0.5);

    fragColor = vec4(scene.rgb + SONAR_COLOR * (surface + ring) * SonarFade, scene.a);
}
