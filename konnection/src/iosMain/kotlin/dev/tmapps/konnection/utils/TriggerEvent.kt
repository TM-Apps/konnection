package dev.tmapps.konnection.utils

internal sealed class BaseTriggerEvent {
    override fun equals(other: Any?): Boolean = false
    override fun hashCode(): Int = kotlin.random.Random.nextInt()
}

internal object TriggerEvent : BaseTriggerEvent()
