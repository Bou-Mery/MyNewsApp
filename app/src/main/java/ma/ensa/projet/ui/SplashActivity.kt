package ma.ensa.projet.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.newsprojectpractice.R
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logoImage = findViewById<ImageView>(R.id.imageView)


        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        logoImage.startAnimation(rotateAnimation)


        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
        finish()
    }
}
