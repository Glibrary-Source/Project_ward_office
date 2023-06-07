package com.kapitalletter.wardoffice

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.kapitalletter.wardoffice.databinding.FragmentLoginPageBinding
import com.kapitalletter.wardoffice.overview.OverviewViewModel
import com.kapitalletter.wardoffice.preferences.MyApplication
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginPage : Fragment() {

    private val TAG = "LoginFragment"

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginPageBinding
    private lateinit var overViewModel: OverviewViewModel

    //googleLogin 인텐트
    private val signInContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    // Google Sign-in was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                    MyApplication.preferences.setString("checkLogin", "1")
                    MyApplication.preferences.setString("GUserEmail", account.email.toString())
                    MyApplication.preferences.setString("GUserName", account.displayName.toString())

                    //로그인 성공시 프로필 페이지로 이동
                    val action = LoginPageDirections.actionLoginPageToMyProfile()
                    findNavController().navigate(action)
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
        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]


        //naver login proguard 설정하자
        //sdk 초기화
        NaverIdLoginSDK.initialize(
            requireContext(),
            getString(R.string.social_login_info_naver_client_id),
            getString(R.string.social_login_info_naver_client_secret),
            getString(R.string.social_login_info_naver_client_name)
        )

        if(MyApplication.preferences.getString("checkLogin", "") == "1") {
            val action = LoginPageDirections.actionLoginPageToMyProfile()
            findNavController().navigate(action)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for the fragment
        binding = FragmentLoginPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure Google Sign-in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //구글 로그인 버튼
        binding.btnGoogleSignin.setOnClickListener {
            signIn()
        }

        //네이버 로그인 버튼
        binding.btnNaverSignin.setOnClickListener {
            startNaverLogin()
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut()
            startNaverLogout()
            startNaverDeleteToken()
            Toast.makeText(requireContext(), "로그아웃 성공", Toast.LENGTH_SHORT).show()
            //네이버 토큰 삭제 원할때
//            startNaverDeleteToken()
        }

        //email 생성 및 로그인 버튼
//        binding.fragmentLoginBtnLogin.setOnClickListener {
//            try {
//                val email = binding.editEmail.text.toString()
//                val password = binding.editPassword.text.toString()
//                performLogin(email, password)
//            } catch (e: Exception) {
//
//            }
//        }
//
//        binding.fragmentLoginBtnCreate.setOnClickListener {
//            Log.d(TAG, auth.currentUser?.email.toString())
//        }

        //naver token , logout 버튼
//        binding.btnNaverLogout.setOnClickListener {
//            startNaverLogout()
//        }
//        binding.btnNaverDeletetoken.setOnClickListener {
//            startNaverDeleteToken()
//        }

    }

    //-----------------------------------------------naver login----------------------------------
    private fun startNaverLogin() {
        var naverToken: String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(result: NidProfileResponse) {
                //로그인 확인
                MyApplication.preferences.setString("checkLogin", "1")

                val userId = result.profile?.id
                Log.d(TAG, "id: $userId \ntoken: $naverToken \n${result.profile?.email}")

                MyApplication.preferences.setString("NProfileName", result.profile!!.name.toString())
                MyApplication.preferences.setString("NProfileEmail", result.profile!!.email.toString())

                //여기까지함
                Toast.makeText(requireContext(), "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()




                val action = LoginPageDirections.actionLoginPageToMyProfile()
                findNavController().navigate(action)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    requireContext(), "errorCode: ${errorCode}\n" +
                            "errorDescription: $errorDescription", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                naverToken = NaverIdLoginSDK.getAccessToken()

//                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
//                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
//                var naverTokenType = NaverIdLoginSDK.getTokenType()
//                var naverState = NaverIdLoginSDK.getState().toString()

                NidOAuthLogin().callProfileApi(profileCallback)

            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    requireContext(), "errorCode: ${errorCode}\n" +
                            "errorDescription: $errorDescription", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)
    }

    private fun startNaverLogout() {
        NaverIdLoginSDK.logout()
    }

    private fun startNaverDeleteToken() {
        NidOAuthLogin().callDeleteTokenApi(requireContext(), object : OAuthLoginCallback {
            override fun onSuccess() {
                Toast.makeText(requireContext(), "네이버 아이디 토큰삭제 성공!", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.d(TAG, "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d(TAG, "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }

    //-----------------------------------------naver login-----------------------------------------


    //-----------------------------------------google login-----------------------------------------

    //google login 로그인
    private fun signIn() {
        // Start the Google Sign-in intent
        try {
            val signInIntent = googleSignInClient.signInIntent
            signInContracts.launch(signInIntent)
        } catch (e: Exception) {
            Log.d(TAG, "로그인 실패")
        }
    }

    //firebase google login 로그인한 user 데이터 보존용
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Firebase 로그인 성공: ${auth.currentUser?.email}")
                } else {
                    Log.w(TAG, "Firebase 로그인 실패: ${task.exception?.message}")
                }
            }
    }

    //-----------------------------------------google login-----------------------------------------


    //-----------------------------------------email login-----------------------------------------

    private fun performLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    if (task.exception is FirebaseAuthInvalidUserException) {
                        showRegistrationDialog(email, password)
                    } else {
                        Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    private fun showRegistrationDialog(email: String, password: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("회원가입")
        builder.setMessage("Email 계정이 없습니다.\n계정을 생성하시겠습니까?")
        builder.setPositiveButton("등록") { dialog, _ ->
            registerUser(email, password)
            dialog.dismiss()
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "회원가입 성공!\n다시로그인해주세요", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "회원가입 취소", Toast.LENGTH_SHORT).show()
                }
            }
    }


    //-----------------------------------------email login-----------------------------------------

    //동적링크 이메일 로그인 코드
//    private fun signInWithEmailLink(email: String) {
//        val actionCodeSettings = ActionCodeSettings.newBuilder()
//            .setUrl("https://tastywardoffice.page.link/certainlog")
//            .setHandleCodeInApp(true)
//            .setAndroidPackageName(
//                "com.kapitalletter.wardoffice",
//                true,
//                null
//            )
//            .build()
//
//        auth.sendSignInLinkToEmail(email, actionCodeSettings)
//            .addOnCompleteListener { task ->
//                if(task.isSuccessful) {
//                    // 이메일 링크 전송 성공
//                    // 사용자에게 이메일을 확인하라는 안내 메시지를 표시할 수 있습니다.
//                    // 이메일을 확인한 후에는 signInWithEmailLink 메서드를 호출하여 로그인을 완료합니다.
//                    Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
//                } else {
//                    // 이메일 링크 전송 실패
//                    // 실패 원인을 처리하는 로직을 추가할 수 있습니다.
//                    Log.d(TAG, task.exception?.message.toString())
//                }
//            }
//    }
//
//    private fun completeSignInWithEmailLink(email:String, emailLink:String) {
//        auth.signInWithEmailLink(email, emailLink)
//            .addOnCompleteListener { task ->
//                if(task.isSuccessful) {
//                    val user = auth.currentUser
//                } else {
//
//                }
//            }
//    }

}




























































