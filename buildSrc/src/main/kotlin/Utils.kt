import org.jetbrains.kotlin.konan.target.HostManager
import java.util.Locale

val experimentalAnnotations = listOf(
    "kotlin.experimental.ExperimentalTypeInference",
    "kotlin.ExperimentalMultiplatform",
    "kotlinx.coroutines.DelicateCoroutinesApi",
    "kotlinx.coroutines.ExperimentalCoroutinesApi",
    "kotlinx.coroutines.ObsoleteCoroutinesApi",
    "kotlinx.coroutines.InternalCoroutinesApi",
    "kotlinx.coroutines.FlowPreview"
)

val hostManager by lazy { HostManager() }


/**
 * Check if the project run on Linux operating system
 *
 * @return true if the operating system is one of them
 */
fun isLinuxMachine(): Boolean {
    val osName = System.getProperty("os.name").toLowerCase(Locale.ROOT)
    return osName.fullTrim().contains("linux")
}

/**
 * Check if the project run on Mac operating system
 *
 * @return true if the operating system is one of them
 */
fun isMacOsMachine(): Boolean {
    val osName = System.getProperty("os.name").toLowerCase(Locale.ROOT)
    return osName.fullTrim().contains("macos")
}

/**
 * Check if the project run on Linux or Mac operating system
 *
 * @return true if the operating system is one of them
 */
fun isLinuxOrMacOsMachine(): Boolean = isLinuxMachine() || isMacOsMachine()

fun String.fullTrim() = replace(" ", "")
