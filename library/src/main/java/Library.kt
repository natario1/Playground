package playground.library


interface Interface {
    val property: Int
}

data class DataClass(
        @JvmField override val property: Int
) : Interface