package com.jpa.app

import com.jpa.core.sprank.SprankApplication

fun main(args: Array<String>) {
    val port: Int = if (args.isNotEmpty()) args[0].toInt() else 7070
    val debug: Boolean = if (args.size > 1) args[1].toBoolean() else true
    Bootstrap().bootstrapData()
    SprankApplication(port, debug).run()
}