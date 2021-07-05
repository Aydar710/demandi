package com.aydar.demandi.featurestudentroom.domain

import com.aydar.demandi.data.model.Answer

class AnswerLikeCountComparator : Comparator<Answer> {

    override fun compare(o1: Answer?, o2: Answer?): Int {
        return if (o1 != null && o2 != null) {
            o2.likes.size - o1.likes.size
        } else {
            0
        }
    }
}