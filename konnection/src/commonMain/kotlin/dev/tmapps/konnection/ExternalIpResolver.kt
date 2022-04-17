package dev.tmapps.konnection

/** External ip resolver contract */
interface ExternalIpResolver {
    suspend fun get(): String?
}
