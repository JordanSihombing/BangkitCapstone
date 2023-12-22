package com.choiri.bodybuddy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.choiri.appcerita.viewmodel.MainViewModelFactory
import com.choiri.bodybuddy.R
import com.choiri.bodybuddy.UserPreferences
import com.choiri.bodybuddy.databinding.ActivityRegisterBinding
import com.choiri.bodybuddy.dataclass.LoginDataAccount
import com.choiri.bodybuddy.dataclass.RegisterDataAccount
import com.choiri.bodybuddy.viewmodel.DataStoreViewModel
import com.choiri.bodybuddy.viewmodel.MainViewModel
import com.choiri.bodybuddy.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    private val loginViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.createaccount)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ifClicked()

        val pref = UserPreferences.getInstance(dataStore)
        val dataStoreViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[DataStoreViewModel::class.java]
        dataStoreViewModel.getLoginSession().observe(this) { sessionTrue ->
            if (sessionTrue) {
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        registerViewModel.message.observe(this) { messageRegist ->
            responseRegister(
                messageRegist
            )
        }
        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        loginViewModel.message.observe(this) { messageLogin ->
            responseLogin(
                messageLogin,
                dataStoreViewModel
            )
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnlogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }

    private fun responseLogin(
        message: String,
        dataStoreViewModel: DataStoreViewModel
    ) {
        if (message.contains("Hello")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val user = loginViewModel.userlogin.value
            dataStoreViewModel.saveLoginSession(true)
            dataStoreViewModel.saveToken(user?.token!!)
            dataStoreViewModel.saveName(user.token)
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun responseRegister(
        message: String,
    ) {
        if (message == "Account Success Created") {
            val userLogin = LoginDataAccount(
                binding.username.text.toString(),
                binding.password.text.toString()
            )
            loginViewModel.login(userLogin)


        } else {
            if (message.contains("Username Has Been Taken")) {
                binding.username.setErrorMessage(
                    resources.getString(R.string.emailTaken),
                    binding.password.text.toString()
                )
                Toast.makeText(this, resources.getString(R.string.emailTaken), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun ifClicked() {
        binding.seeRegistPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.RetypePassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.RetypePassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }

            // Set selection to end of text
            binding.password.text?.let { binding.password.setSelection(it.length) }
            binding.RetypePassword.text?.let { binding.RetypePassword.setSelection(it.length) }
        }

        binding.btnRegister.setOnClickListener {
            binding.apply {
                username.clearFocus()
                password.clearFocus()
                RetypePassword.clearFocus()
            }

            if (binding.username.isNameValid && binding.password.isPasswordValid && binding.RetypePassword.isPasswordValid) {
                val dataRegisterAccount = RegisterDataAccount(
                    username = binding.username.text.toString().trim(),
                    password = binding.password.text.toString().trim()
                )
                registerViewModel.register(dataRegisterAccount)

            } else {
                if (!binding.username.isNameValid) binding.username.error =
                    resources.getString(R.string.nameNone)
                if (!binding.password.isPasswordValid) binding.password.error =
                    resources.getString(R.string.halah)
                if (!binding.RetypePassword.isPasswordValid) binding.RetypePassword.error =
                    resources.getString(R.string.passwordConfirm2)

                Toast.makeText(this, R.string.invalidLogin, Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }


}



