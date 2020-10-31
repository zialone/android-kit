package com.hcanyz.android_kit.widget.core

private const val server_local_url = "http://localhost:8080"
private const val server_remote_url = "https://example.com"

private var mock = true
val server_url = if (mock) server_local_url else server_remote_url