package com.mostafa.countdown

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var btn_start :Button
    lateinit var tv_title : TextView
    lateinit var tv_timer: TextView
    lateinit var tv_rest :TextView
    lateinit var pb : ProgressBar
    var isTimerRunning : Boolean = false
    val REMAIN_TIME_KEY ="RemianTime"
    val STARTED_TIME_IN_MILESEC :Long = 25*60*1000
    var REMAING_TIME :Long= STARTED_TIME_IN_MILESEC
    var my_timer : CountDownTimer? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intailzeviews()

        btn_start.setOnClickListener {
            if (!isTimerRunning) {
                startTimer(STARTED_TIME_IN_MILESEC)
                tv_title.text = resources.getText(R.string.keep_going)
            }
        }

        tv_rest.setOnClickListener {
        restTime()

        }



    }

    private fun startTimer(starttime :Long) {
         my_timer = object : CountDownTimer(starttime, 1000) {
            override fun onTick(p0: Long) {
                REMAING_TIME=p0
                formatedtime()
                pb.progress=REMAING_TIME.div(STARTED_TIME_IN_MILESEC.toDouble()).times(100).toInt()
            }

            override fun onFinish() {
                isTimerRunning=false
                Snackbar.make(tv_timer,"Timer Finished !",Snackbar.LENGTH_SHORT).show()
            }

        }.start()
        isTimerRunning = true
    }
    private fun restTime(){
        my_timer?.cancel()
        REMAING_TIME=STARTED_TIME_IN_MILESEC
        formatedtime()
        tv_title.text=resources.getText(R.string.take_pomodoro)
        isTimerRunning=false
        pb.progress=100

    }
    private fun intailzeviews(){
        btn_start = findViewById(R.id.btn_start)
        tv_title =findViewById(R.id.tv_title)
        tv_rest=findViewById(R.id.tv_rest)
        tv_timer =findViewById(R.id.timer_tv)
        pb = findViewById(R.id.progressBar)
    }
    private fun formatedtime(){
        val minute = REMAING_TIME.div(1000).div(60)
        val secend = REMAING_TIME.div(1000)%60
        val formatedTime = String.format("%02d:%02d",minute,secend)
        tv_timer.text =formatedTime
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(REMAIN_TIME_KEY,REMAING_TIME)

        Log.d("MY_TAG", "onSaveInstanceState: $REMAING_TIME ")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedTime= savedInstanceState.getLong(REMAIN_TIME_KEY)
        if (savedTime != STARTED_TIME_IN_MILESEC) {
            startTimer(savedTime)
        }
    }


}