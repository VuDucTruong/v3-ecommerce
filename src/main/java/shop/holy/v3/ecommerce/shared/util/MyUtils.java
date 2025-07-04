package shop.holy.v3.ecommerce.shared.util;

import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;

public class MyUtils {
    public static boolean isLocalhost(HttpServletRequest request) {
        try {
            String ip = request.getRemoteAddr();
            if (ip == null || ip.isEmpty()) return false; // can't determine

            InetAddress addr = InetAddress.getByName(ip);

            // Check IPv4 loopback (127.0.0.1) or IPv6 (::1)
            if (addr.isLoopbackAddress()) return true;

            // Check private/local IP ranges
            if (addr.isSiteLocalAddress()) return true;

            return false; // remote
        } catch (Exception e) {
            return false; // treat as remote on error
        }
    }
}
