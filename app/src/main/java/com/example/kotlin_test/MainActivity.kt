package com.example.kotlin_test

import android.app.NotificationChannel

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle


import kotlinx.android.synthetic.main.activity_main.*
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

import android.os.Build
import android.os.CountDownTimer

import java.text.SimpleDateFormat
import android.media.RingtoneManager
import android.media.Ringtone
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.tapadoo.alerter.Alerter
import com.tapadoo.alerter.OnHideAlertListener
import com.tapadoo.alerter.OnShowAlertListener
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {
    private  val CHANNEL_ID="73195"
   private val timeSetted:Long=60000
   private val words=Words.get()
   private val rand=java.util.Random()
   private var initialTime:Long=timeSetted
   private var isNotificationRunning=false
   private val timeFormat=SimpleDateFormat("mm:ss")
    private var isRunning:Boolean=false
    lateinit var timer:CountDownTimer

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MY_Channel"
            val descriptionText = "MY channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



   private fun reset() {
        if (!isRunning) {
            time_text.textSize = 80f
            initialTime = timeSetted
            time_text.text = timeFormat.format(-1800000 + initialTime).toString()
            btn_start.text = "start"

        }
    }


    private fun alertTimeUp(){
        val alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val player =MediaPlayer.create(this,alarmTone)
        Alerter.create(this)
            .setTitle("Time Up")
            .setText("Your time is up")
            .setBackgroundColorRes(R.color.btn_start)
            .setDismissable(true)
            .setDuration(10000)
            .setOnShowListener(OnShowAlertListener {
                isNotificationRunning=true
                player.isLooping=true
                player.start()
            })
            .setOnHideListener(OnHideAlertListener {
                isNotificationRunning=false
                isRunning=false
                player.stop()
                reset()
            })
            .show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        time_text.text=timeFormat.format(-1800000+initialTime).toString()

        word_text.text=words[rand.nextInt(words.size)]
        btn_plus.setOnClickListener {

            if(!isRunning){

                word_text.text=words[rand.nextInt(words.size)]
                reset()
            }


        }


        btn_minus.setOnClickListener {
           reset()

        }


        btn_start.setOnClickListener {
            if(!isNotificationRunning){
            time_text.textSize=80f
            if(!isRunning){
                time_text.text=" "
                timer=object:CountDownTimer(initialTime,1000){

                    override fun onFinish() {
                        btn_start.text="start"
                        initialTime=timeSetted
                        alertTimeUp()
                        btn_start.setBackgroundResource(R.drawable.btn_start)

                    }

                    override fun onTick(millisUntilFinished: Long) {
                            time_text.text=timeFormat.format(-1800000+millisUntilFinished).toString()
                            isRunning=true
                            initialTime=millisUntilFinished
                            btn_start.text = "pause"
                            btn_start.setBackgroundResource(R.drawable.btn_pause)

                    }


                }
            timer.start()}else{
                timer.cancel()
                isRunning=false
                btn_start.text="Start"
                btn_start.setBackgroundResource(R.drawable.btn_start)
            }
        }



    }}


}