package org.crimsoncrips.alexscavesexemplified.backport;

import java.util.IdentityHashMap;
import java.util.Map;

public class AttachmentData {

    final Map<AttachmentType<?>, Object> values = new IdentityHashMap<>();

    public Map<AttachmentType<?>, Object> values() {
        return this.values;
    }
}
