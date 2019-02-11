package com.app.redes.gravadordevoz
import android.Manifest
import android.content.pm.PackageManager
import kotlinx.android.synthetic.main.activity_main.*

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaRecorder
import android.widget.Toast
import android.media.MediaPlayer
import android.os.Environment
import com.app.redes.gravadordevoz.R.id.play
import com.app.redes.gravadordevoz.R.id.stop
import com.app.redes.gravadordevoz.R.id.record
import android.os.Environment.getExternalStorageDirectory
import android.support.v4.app.ActivityCompat
import android.view.View
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private var myAudioRecorder: MediaRecorder? = null
    private var outputFile: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stop.setEnabled(false)
        play.setEnabled(false)



}

    override fun onResume() {
        super.onResume()
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp"
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO),
                    10)

        }  else {
            myAudioRecorder = MediaRecorder()
            myAudioRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            myAudioRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            myAudioRecorder!!.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            myAudioRecorder!!.setOutputFile(outputFile)
        }
        record.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                try {
                    myAudioRecorder!!.prepare()
                    myAudioRecorder!!.start()
                } catch (ise: IllegalStateException) {
                    // make something ...
                } catch (ioe: IOException) {
                    // make something
                }

                record.isEnabled = false
                stop.isEnabled = true
                Toast.makeText(applicationContext, "Recording started", Toast.LENGTH_LONG).show()
            }
        })

        stop.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (myAudioRecorder != null) {
                    myAudioRecorder!!.stop()
                    myAudioRecorder!!.reset()
                    myAudioRecorder!!.release()
                    myAudioRecorder = null
                }
                record.isEnabled = true
                stop.isEnabled = false
                play.isEnabled = true
                Toast.makeText(applicationContext, "Audio Recorder successfully", Toast.LENGTH_LONG).show()
            }
        })

        play.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val mediaPlayer = MediaPlayer()
                try {
                    mediaPlayer.setDataSource(outputFile)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    Toast.makeText(applicationContext, "Playing Audio", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    // make something
                }

            }
        })
    }
}
