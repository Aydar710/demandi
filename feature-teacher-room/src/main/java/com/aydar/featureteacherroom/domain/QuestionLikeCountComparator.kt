package com.aydar.featureteacherroom.domain

import com.aydar.demandi.data.model.Question

class QuestionLikeCountComparator : Comparator<Question> {

    override fun compare(o1: Question?, o2: Question?): Int {
        return if (o1 != null && o2 != null) {
            o2.likes.size - o1.likes.size
        } else {
            0
        }
    }
}