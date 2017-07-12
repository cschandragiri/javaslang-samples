package com.playground.javaslang;

import org.openjdk.jol.info.GraphLayout;

class Memory {
    static long byteSize(Object target) { return GraphLayout.parseInstance(target).totalSize(); }
}
