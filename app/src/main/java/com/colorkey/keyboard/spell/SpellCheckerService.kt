package com.colorkey.keyboard.spell

import android.service.textservice.SpellCheckerService
import android.view.textservice.SuggestionsInfo
import android.view.textservice.SentenceSuggestionsInfo
import android.view.textservice.TextInfo

class SpellCheckerService : SpellCheckerService() {
    override fun createSession(): Session {
        return object : Session() {
            override fun onCreate() {}
            override fun onGetSuggestions(textInfo: TextInfo?, suggestionsLimit: Int): SuggestionsInfo {
                return SuggestionsInfo(SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO, arrayOf())
            }
            override fun onGetSentenceSuggestionsMultiple(textInfos: Array<out TextInfo>?, suggestionsLimit: Int): Array<SentenceSuggestionsInfo> {
                return arrayOf()
            }
        }
    }
}
