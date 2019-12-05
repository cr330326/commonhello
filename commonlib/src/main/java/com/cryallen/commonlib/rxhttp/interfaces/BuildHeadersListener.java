package com.cryallen.commonlib.rxhttp.interfaces;

import java.util.Map;

/**
 * <pre>
 *      @author : Allen
 *      date    : 2019/03/03
 *      desc    : 请求头interface
 * </pre>
 */
public interface BuildHeadersListener {
    Map<String, String> buildHeaders();
}
