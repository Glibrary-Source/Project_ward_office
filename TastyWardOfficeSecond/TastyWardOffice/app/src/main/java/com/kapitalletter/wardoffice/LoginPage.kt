package com.kapitalletter.wardoffice

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginPage : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private val signInContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    // Google Sign-in was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                    Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()
                } catch (e: ApiException) {
                    // Google Sign-in failed, handle the error
                    Log.w(TAG, "Google Sign-in failed: ${e.message}")
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_login_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure Google Sign-in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Set up the sign-in button
        val signInButton = view.findViewById<Button>(R.id.btn_google_Signin)
        signInButton.setOnClickListener {
            signIn()
        }

        val signOutButton = view.findViewById<Button>(R.id.btn_logout)
        signOutButton.setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut()
            Toast.makeText(requireContext(), "로그아웃 성공", Toast.LENGTH_SHORT).show()
        }

    }

    private fun signIn() {
        // Start the Google Sign-in intent
        try{
            val signInIntent = googleSignInClient.signInIntent
            signInContracts.launch(signInIntent)
        } catch (e: Exception) {
            Log.d(TAG, "error sign in")
        }
    }

    companion object {
        private const val TAG = "MainFragment"
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if(task.isSuccessful) {
                    Log.d(TAG, "Firebase 로그인 성공: ${auth.currentUser?.email}")
                } else {
                    Log.w(TAG, "Firebase 로그인 실패: ${task.exception?.message}")
                }
            }
    }

}