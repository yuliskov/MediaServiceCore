package com.liskovsoft.youtubeapi.chat

class LiveChatServiceInt private constructor() {
    companion object {
        private var sInstance: LiveChatServiceInt? = null
        @JvmStatic
        fun instance(): LiveChatServiceInt? {
            if (sInstance == null) {
                sInstance = LiveChatServiceInt()
            }
            return sInstance
        }

        @JvmStatic
        fun unhold() {
            sInstance = null
        }
    }
}