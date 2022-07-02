package com.liskovsoft.youtubeapi.chat

class LiveChatService private constructor() {
    companion object {
        private var sInstance: LiveChatService? = null
        fun instance(): LiveChatService? {
            if (sInstance == null) {
                sInstance = LiveChatService()
            }
            return sInstance
        }

        fun unhold() {
            sInstance = null
        }
    }
}