package com.example.newsapp.view.auth

import com.google.common.truth.Truth
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class signupTest{
    lateinit var sign : signup

    @Before
    fun setup(){
        sign = signup()
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
        assertFalse(result)
    }

    @Test
    fun emptyLastNameFalse() {
        val result = sign.performAuth(
            "a",
            " ",
            "a",
            "a",
            "a",
            "a"
        )
        assertFalse(result)
    }

    @Test
    fun emptyUserNameFalse() {
        val result = sign.performAuth(
            "a",
            "a",
            "",
            "a",
            "a",
            "a"
        )
        assertFalse(result)
    }

    @Test
    fun emptyEmailFalse() {
        val result = sign.performAuth(
            "a",
            "a",
            "a",
            "",
            "a",
            "a"
        )
        assertFalse(result)
    }

    @Test
    fun emptyPasswordFalse() {
        val result = sign.performAuth(
            "a",
            "a",
            "a",
            "a",
            "a",
            "a"
        )
        assertFalse(result)
    }

    @Test
    fun emptyConfirmPasswordFalse() {
        val result = sign.performAuth(
            "a",
            "a",
            "a",
            "a",
            "a",
            ""
        )
        assertFalse(result)
    }

    @Test
    fun passwordsNotMatchFalse() {
        val result = sign.performAuth(
            "a",
            "a",
            "a",
            "a",
            "a",
            "d"
        )
        assertFalse(result)
    }

    @Test
    fun checkEmailPatternFalse() {
        val result = sign.performAuth(
            "a",
            "a",
            "a",
            "aas",
            "d",
            "d"
        )
        assertFalse(result)
    }

    @Test
    fun passwordLengthFalse() {
        val result = sign.performAuth(
            "a",
            "a",
            "a",
            "a@gmail.com",
            "d",
            "d"
        )
        assertFalse(result)
    }

    @Test
    fun successfulUserTrue() {
        val result = sign.performAuth(
            "a",
            "a",
            "a",
            "a@gmail.com",
            "qwerty",
            "qwerty"
        )
        assertTrue(result)
    }
}