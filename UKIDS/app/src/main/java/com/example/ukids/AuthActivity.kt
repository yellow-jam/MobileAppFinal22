package com.example.ukids

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.ukids.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_auth)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeVisibility(intent.getStringExtra("data").toString())
        binding.goSignInBtn.setOnClickListener {
            changeVisibility("signin")
        }

        /* 회원 가입 기능 */
        binding.signBtn.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if(task.isSuccessful){  // 회원가입 성공
                        // 유효한 이메일인가
                        MyApplication.auth.currentUser?.sendEmailVerification()  // 인증메일 보내기
                            ?.addOnCompleteListener{ sendTask ->
                                if(sendTask.isSuccessful){  // 메일 발송이 정상적
                                    Toast.makeText(baseContext, "회원가입 성공!!.. 메일을 확인해주세요", Toast.LENGTH_SHORT)  // 액티비티에 있는 코드지만 리스너에 붙어있으므로 바로 this 사용 불가
                                        .show()
                                    changeVisibility("logout")
                                }
                                else{ // 메일발송 실패
                                    Toast.makeText(baseContext, "메일발송 실패", Toast.LENGTH_SHORT)  // 액티비티에 있는 코드지만 리스너에 붙어있으므로 바로 this 사용 불가
                                        .show()
                                    changeVisibility("logout")
                                }
                            }
                    } else {  // 회원가입 실패
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT)  // 액티비티에 있는 코드지만 리스너에 붙어있으므로 바로 this 사용 불가
                            .show()
                        changeVisibility("logout")
                    }
                }
        }

        /* 로그인 기능 */
        binding.loginBtn.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if(task.isSuccessful){
                        // 사용자가 이메일 인증을 했는지 확인 -> MyApplication에서 담당
                        if(MyApplication.checkAuth()){
                            MyApplication.email = email
                            finish()  // 메인액티비티로 돌아감
                        }
                        else {
                            Toast.makeText(baseContext, "이메일 인증이 되지 않았습니다.", Toast.LENGTH_SHORT)  // 액티비티에 있는 코드지만 리스너에 붙어있으므로 바로 this 사용 불가
                                .show()
                        }
                    }
                    else{
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT)  // 액티비티에 있는 코드지만 리스너에 붙어있으므로 바로 this 사용 불가
                            .show()
                    }
                }
        }

        /* 로그아웃 기능 */
        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            MyApplication.email = null
            finish()  // 메인액티비티로 돌아감
        }

    }

    // 모드에 따라 화면이 다르게 보이도록 하는 함수
    fun changeVisibility(mode: String){
        if(mode.equals("login")){
            binding.run{
                authMainTextView.text = "정말 로그아웃하시겠습니까?"
                authMainTextView.visibility = View.VISIBLE
                logoutBtn.visibility = View.VISIBLE  // 로그아웃 시 필요한 뷰만 VISIBLE, 나머지 GONE
                goSignInBtn.visibility = View.GONE
                authEmailEditView.visibility = View.GONE
                authPasswordEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.GONE
            }
        } else if(mode.equals("logout")){
            binding.run{
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                authMainTextView.visibility = View.VISIBLE
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
            }
        } else if(mode.equals("signin")){
            binding.run{
                authMainTextView.visibility = View.GONE
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
            }
        }
    }
}