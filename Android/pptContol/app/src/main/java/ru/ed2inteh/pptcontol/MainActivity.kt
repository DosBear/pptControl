package ru.ed2inteh.pptcontol

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast

import java.net.URL
import android.os.StrictMode
import android.util.Log
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        btnLeft.setOnClickListener{
            sendCommand("left")

        }
        btnStart.setOnClickListener{
            sendCommand("start")

        }
        btnRight.setOnClickListener{
            sendCommand("right")
        }

        btnStop.setOnClickListener{
            sendCommand("stop")
        }

        btnEmpty.setOnClickListener{
            sendCommand("empty")
        }
        btnFindIp.setOnClickListener{
           val list = readArpCache()
            for (item in list)
            {
                val localURL = "http://$item:5000/"
                try {

                    if(URL(localURL.toString()).readText() == "pptContol")
                    {
                        editText.setText(item)
                        return@setOnClickListener
                    }
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun readArpCache(): ArrayList<String> {
        val ipList = ArrayList<String>()
        var br: BufferedReader? = null
        try {
            br = BufferedReader(FileReader("/proc/net/arp"), 1024)
            var line: String
            line = ""
            while (line != null) {
                line = br!!.readLine()
                Log.d("testttt", line)

                val tokens = line.split(" +".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (tokens != null && tokens.size >= 4) {
                    // verify format of MAC address
                    val macAddress = tokens[3]
                    if (macAddress.matches("..:..:..:..:..:..".toRegex())) {
                        //Log.i(TAG, "MAC=" + macAddress + " IP=" + tokens[0] + " HW=" + tokens[1]);

                        // Ignore the entries with MAC-address "00:00:00:00:00:00"
                        if (macAddress != "00:00:00:00:00:00") {

                            val ipAddress = tokens[0]
                            ipList.add(ipAddress)

                            Log.i("testttt", "$macAddress; $ipAddress")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                br!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return ipList
    }

    fun sendCommand( command: String)
    {
        try {
            val localURL = "http://"+ editText.text.toString()+ ":5000/$command"
            URL(localURL.toString()).readText()
        } catch (t: Throwable)
        {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show()
        }

    }
}

