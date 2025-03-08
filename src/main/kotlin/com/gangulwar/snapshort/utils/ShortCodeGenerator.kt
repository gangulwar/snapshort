package com.gangulwar.snapshort.utils

import java.util.UUID

object ShortCodeGenerator {
    fun generate(): String = UUID.randomUUID().toString().take(6)
}
