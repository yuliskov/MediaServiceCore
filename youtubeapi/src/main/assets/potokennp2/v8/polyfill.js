const __intervalCallbacks = new Map();

globalThis.setInterval = function(fn, delay = 0, ...args) {

    const id = __nativeSetInterval(delay);

    __intervalCallbacks.set(id, () => {
        fn(...args);
    });

    return id;
};

globalThis.clearInterval = function(id) {
    __intervalCallbacks.delete(id);
    __nativeClearInterval(id);
};

globalThis.__runInterval = function(id) {
    __intervalCallbacks.get(id)?.();
};