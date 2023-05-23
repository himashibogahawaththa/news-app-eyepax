package com.example.newsapp.view.auth

import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignupActivityTest{
    lateinit var sign : SignupActivity

    @Before
    fun setup(){
        sign = SignupActivity()
    }

    @Test
    fun emptyFirstNameFalse() {
        val result = sign.performAuth(
            "",
            "a",
            "a",
            "a",
            "a",
            "a"
        )
        assertTrue(result)
    }
}