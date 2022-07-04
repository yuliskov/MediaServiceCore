package com.liskovsoft.youtubeapi.chat

class LiveChatService private constructor() {
    companion object {
        private var sInstance: LiveChatService? = null
        @JvmStatic
        fun instance(): LiveChatService? {
            if (sInstance == null) {
                sInstance = LiveChatService()
            }
            return sInstance
        }

        @JvmStatic
        fun unhold() {
            sInstance = null
        }
    }
}