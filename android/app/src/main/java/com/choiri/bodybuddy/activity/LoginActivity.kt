package com.choiri.bodybuddy.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.choiri.appcerita.viewmodel.MainViewModelFactory
import com.choiri.bodybuddy.R
import com.choiri.bodybuddy.UserPreferences
import com.choiri.bodybuddy.databinding.ActivityLoginBinding
import com.choiri.bodybuddy.dataclass.LoginDataAccount
import com.choiri.bodybuddy.viewmodel.DataStoreViewModel
import com.choiri.bodybuddy.viewmodel.MainViewModel
import com.choiri.bodybuddy.viewmodel.ViewModelFactory



val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ifClicked()

        val preferences = UserPreferences.getInstance(dataStore)
        val dataStoreViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[DataStoreViewModel::class.java]

        dataStoreViewModel.getLoginSession().observe(this) { sessionTrue ->
            if (sessionTrue) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        loginViewModel.message.observe(this) { message ->
            responseLogin(
                message,
                dataStoreViewModel
            )
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }
    private fun responseLogin(message: String, dataStoreViewModel: DataStoreViewModel) {
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

    private fun ifClicked() {
        binding.btnLogin.setOnClickListener {
            binding.username.clearFocus()
            binding.password.clearFocus()

            if (isDataValid()) {
                val requestLogin = LoginDataAccount(
                    binding.username.text.toString().trim(),
                    binding.password.text.toString().trim()
                )
                loginViewModel.login(requestLogin)
            } else {
                if (!binding.username.isNameValid) binding.username.error =
                    getString(R.string.nameNone)
                if (!binding.password.isPasswordValid) binding.password.error =
                    getString(R.string.halah)

                Toast.makeText(this, R.string.invalidLogin, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnregister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.seePassword.setOnCheckedChangeListener { _, isChecked ->
            binding.password.transformationMethod = if (isChecked) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            // Set selection to end of text
            binding.password.text?.let { binding.password.setSelection(it.length) }
        }
    }

    private fun isDataValid(): Boolean {
        return binding.username.isNameValid && binding.password.isPasswordValid
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}

