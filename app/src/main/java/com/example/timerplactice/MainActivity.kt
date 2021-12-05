package com.example.timerplactice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.example.timerplactice.databinding.ActivityMainBinding
import java.util.*

//参考元（というかコピー元）https://teratail.com/questions/93963
class MainActivity : AppCompatActivity() {
    internal var mHandler = Handler()
    internal var mCounter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.TextA)
        textView.text = ""
        // onCreateのようなライフサイクルメソッドの中で時間のかかる処理を記述すると
        // アプリが落ちるので、そうした処理は別スレッドで行う
        val thread = Thread(Runnable {
            try {
                mCounter = 0
                while (mCounter < 3) {
                    // Threadによる処理の中ではUIを操作することができないので、
                    // Handlerを用いてUIスレッドに行わせる処理を記述する
                    mHandler.post {
                        // この部分はUIスレッドで動作する
                        textView.text = (mCounter + 1).toString()
                    }
                    // ここで時間稼ぎ
                    Thread.sleep(1000)
                    mCounter++
                }
                // 繰り返しが終わったところで次のActivityに遷移する
                val intent = Intent(this@MainActivity, SubActivity::class.java)
                startActivity(intent)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })
        thread.start()
    }
}