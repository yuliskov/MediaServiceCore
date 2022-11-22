package com.liskovsoft.youtubeapi.common.helpers

import okhttp3.Dns
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

/**
 * Fix for the [issue](https://github.com/facebook/react-native/issues/32730)
 *
 * See also:
 * [1](https://gist.github.com/danmaas/c60af5fed9f55d2bc616ce302696540d)
 * [2](https://github.com/square/okhttp/issues/6954)
 * [3](https://github.com/yschimke/okurl/blob/b24caf077223cf54e2ab26589839e5ba2205c691/src/main/java/com/baulsupp/oksocial/network/DnsSelector.java)
 * [4](https://stackoverflow.com/questions/64559405/how-do-i-force-my-android-app-to-use-ipv4-instead-of-ipv6)
 */
class OkHttpDNSSelector(private val mode: IPvMode) : Dns {

    enum class IPvMode(val code: String) {
        SYSTEM("system"),
        IPV6_FIRST("ipv6"),
        IPV4_FIRST("ipv4"),
        IPV6_ONLY("ipv6only"),
        IPV4_ONLY("ipv4only");

        companion object {
            @JvmStatic
            fun fromString(ipMode: String): IPvMode =
                IPvMode.values().find { it.code == ipMode } ?: throw Exception("Unknown value $ipMode")
        }
    }

    override fun lookup(hostname: String): List<InetAddress> {
        var addresses = Dns.SYSTEM.lookup(hostname)

        addresses = when (mode) {
            IPvMode.IPV6_FIRST -> addresses.sortedBy { Inet4Address::class.java.isInstance(it) }
            IPvMode.IPV4_FIRST -> addresses.sortedBy { Inet6Address::class.java.isInstance(it) }
            IPvMode.IPV6_ONLY -> addresses.filter { Inet6Address::class.java.isInstance(it) }
            IPvMode.IPV4_ONLY -> addresses.filter { Inet4Address::class.java.isInstance(it) }
            IPvMode.SYSTEM -> addresses
        }

        //logger.fine("DJMOKHTTP ($hostname): " + addresses.joinToString(", ") { it.toString() })

        return addresses
    }
}