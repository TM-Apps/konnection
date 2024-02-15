package dev.tmapps.konnection

/** IP resolver contract */
interface IpResolver {
    suspend fun get(): String?
}
