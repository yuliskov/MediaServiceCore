package com.liskovsoft.youtubeapi.app.potoken

internal const val RESULT_DELIM = "%RESULT_DELIM%"
internal const val DOM_WRAPPER =
"""
    const base64urlCharRegex = /[-_.]/g;
    const base64urlToBase64Map = {
        '-': '+',
        _: '/',
        '.': '='
    };
    function base64ToU8(base64) {
        let base64Mod;
        if (base64urlCharRegex.test(base64)) {
            base64Mod = base64.replace(base64urlCharRegex, function (match) {
                return base64urlToBase64Map[match];
            });
        }
        else {
            base64Mod = base64;
        }
        base64Mod = atob(base64Mod);
        const result = new Uint8Array([...base64Mod].map((char) => char.charCodeAt(0)));
        return result;
    }
    function u8ToBase64(u8, base64url = false) {
        const result = btoa(String.fromCharCode(...u8));
        if (base64url) {
            return result
                .replace(/\+/g, '-')
                .replace(/\//g, '_');
        }
        return result;
    }
    function btoa(input) {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
        let str = String(input);
        let output = '';
        let i = 0;
        
        while (i < str.length) {
            const block = (str.charCodeAt(i++) << 16) |
                          (str.charCodeAt(i++) << 8) |
                          (str.charCodeAt(i++) || 0);
            
            output += chars[(block >> 18) & 0x3F] +
                      chars[(block >> 12) & 0x3F] +
                      (i > str.length + 1 ? '=' : chars[(block >> 6) & 0x3F]) +
                      (i > str.length ? '=' : chars[block & 0x3F]);
        }
        
        return output;
    }
    var events = {};
    function addEventListener(event, handler) {
        if (!events[event]) {
          events[event] = [];
        }
        events[event].push(handler);
    }
    function removeEventListener(event, handler) {
        if (!events[event]) return;
        events[event] = events[event].filter(h => h !== handler);
    }
    function dispatchEvent(event) {
        if (!events[event.type]) return;
        events[event.type].forEach(handler => handler(event));
    }
    window = {}; document = {}; window.document = document;
    window.performance = { now: () => 261855.69999999553 };
    window.btoa = btoa;
    document.hidden = false;
    document.readyState = 'complete';
    document.addEventListener = addEventListener;
    document.removeEventListener = removeEventListener;
    document.dispatchEvent = dispatchEvent;
"""