package com.pedroid.qrcodecompose.androidapp.core.presentation

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Instant.isToday(zoneId: ZoneId = ZoneId.systemDefault()): Boolean = this.atZone(zoneId).toLocalDate().isToday(zoneId)

fun Instant.isYesterday(zoneId: ZoneId = ZoneId.systemDefault()): Boolean = this.atZone(zoneId).toLocalDate().isYesterday(zoneId)

fun LocalDate.isToday(zoneId: ZoneId = ZoneId.systemDefault()): Boolean = this.isEqual(LocalDate.now(zoneId))

fun LocalDate.isYesterday(zoneId: ZoneId = ZoneId.systemDefault()): Boolean = this.isEqual(LocalDate.now(zoneId).minusDays(1))

fun getHourTimeFormat(): String = (SimpleDateFormat.getTimeInstance() as? SimpleDateFormat)?.toPattern() ?: "HH:mm:ss"
