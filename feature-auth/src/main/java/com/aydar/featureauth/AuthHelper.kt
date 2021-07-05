package com.aydar.featureauth

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.common.getTag
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class AuthHelper(private val activity: AppCompatActivity) {

    lateinit var onCodeSent: () -> Unit
    lateinit var onVerificationFailed: () -> Unit
    lateinit var onVerificationCompleted: (String) -> Unit
    lateinit var onSignedIn: (FirebaseUser) -> Unit

    private lateinit var verificationId: String
    private val auth = FirebaseAuth.getInstance()

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            credential.smsCode?.let {
                onVerificationCompleted.invoke(it)
            }
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.e(getTag(), "onVerificationFailed: ", e)
            onVerificationFailed.invoke()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            this@AuthHelper.verificationId = verificationId
            onCodeSent.invoke()
        }
    }

    fun checkIsUserSignedIn(): Boolean {
        val user = auth.currentUser
        return user != null
    }

    fun verifyPhoneNumber(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            activity,
            callbacks
        )
    }

    fun signInWithCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    user?.let { onSignedIn.invoke(it) }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(activity, "Неверный код", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}