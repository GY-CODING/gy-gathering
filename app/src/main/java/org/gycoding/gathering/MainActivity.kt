package org.gycoding.gathering

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import org.gycoding.gathering.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth0: Auth0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Correctly initialize the Auth0 instance
        auth0 = Auth0.getInstance(
            clientId = "gI4jJD3x917rYmrY7PKIlQAdi0I14YN9", // Your Auth0 Client ID
            domain = "gycoding.eu.auth0.com"     // Your Auth0 Domain
        )

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure the login button
        binding.loginButton.setOnClickListener {
            loginWithAuth0()
        }

        // Configure the logout button
        binding.logoutButton.setOnClickListener {
            logoutFromAuth0()
        }
    }

    private fun loginWithAuth0() {
        WebAuthProvider.login(auth0)
            .withScheme("https")
            .withAudience("https://${auth0.domain}/userinfo")
            .start(this, object : Callback<Credentials, AuthenticationException> {
                override fun onSuccess(result: Credentials) {
                    // Log successful login
                    Log.d("Auth0", "Login Successful: ${result.accessToken}")
                    Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(error: AuthenticationException) {
                    // Log failed login
                    Log.e("Auth0", "Login Failed: ${error.message}")
                    Toast.makeText(this@MainActivity, "Login Failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun logoutFromAuth0() {
        WebAuthProvider.logout(auth0)
            .withScheme("https") // Matches the intent filter in your AndroidManifest.xml
            .start(this, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(result: Void?) {
                    Toast.makeText(this@MainActivity, "Logout Successful", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(error: AuthenticationException) {
                    Toast.makeText(this@MainActivity, "Logout Failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
