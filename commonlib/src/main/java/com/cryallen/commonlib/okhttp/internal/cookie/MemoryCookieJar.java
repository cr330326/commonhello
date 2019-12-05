package com.cryallen.commonlib.okhttp.internal.cookie;

import com.cryallen.commonlib.okhttp.internal.cookie.cache.CookieCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * MemeryCookieJar
 */
public class MemoryCookieJar implements ClearableCookieJar {

    private CookieCache cache;

    public MemoryCookieJar(CookieCache cache) {
        this.cache = cache;
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cache.addAll(cookies);
    }

    @Override
    synchronized public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> validCookies = new ArrayList<>();

        for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();

            if (isCookieExpired(currentCookie)) {
                it.remove();

            } else if (currentCookie.matches(url)) {
                validCookies.add(currentCookie);
            }
        }

        return  validCookies;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    @Override
    synchronized public void clear() {
        cache.clear();
    }
}
