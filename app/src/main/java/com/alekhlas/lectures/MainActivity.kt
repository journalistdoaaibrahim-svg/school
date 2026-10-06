package com.alekhlas.lectures

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

data class Lecture(val title:String,val videoId:String)

class MainActivity : Activity() {
    private val lectures = listOf(
        Lecture("محاضرة قوانين السير","rMVZwpDlARo"),
        Lecture("محاضرة الميكانيكا","sKbqPG386-c"),
        Lecture("محاضرة الإشارات","P_EMWF63d_o")
    )
    private val blue = Color.rgb(11,43,224)
    private val orange = Color.rgb(255,106,0)
    private val bg = Color.rgb(245,247,255)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showList()
    }

    private fun baseLayout() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(bg)
        layoutDirection = View.LAYOUT_DIRECTION_RTL
        textDirection = View.TEXT_DIRECTION_RTL
    }

    private fun titleBar(title:String, subtitle:String? = null): LinearLayout {
        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24,22,24,22)
            setBackgroundColor(Color.WHITE)
        }
        val t = TextView(this).apply {
            text = title
            textSize = 22f
            setTextColor(Color.rgb(14,19,48))
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }
        bar.addView(t, LinearLayout.LayoutParams(-1,-2))
        if (subtitle != null) {
            val s = TextView(this).apply {
                text = subtitle
                textSize = 14f
                setTextColor(Color.DKGRAY)
                gravity = Gravity.CENTER
            }
            bar.addView(s, LinearLayout.LayoutParams(-1,-2))
        }
        return bar
    }

    private fun showList() {
        val root = baseLayout()
        root.addView(titleBar("مدرسة الإخلاص","المحاضرات التعليمية"))
        val scroll = android.widget.ScrollView(this)
        val list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24,24,24,24)
        }
        val intro = TextView(this).apply {
            text = "أهلًا بك في تطبيق المحاضرات\nاختر المحاضرة التي تريد مشاهدتها"
            textSize = 20f
            setTextColor(Color.rgb(14,19,48))
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.RIGHT
            setPadding(0,0,0,24)
        }
        list.addView(intro)
        lectures.forEachIndexed { index, lecture ->
            val b = Button(this).apply {
                text = "▶  المحاضرة " + (index + 1) + "\n" + lecture.title
                textSize = 17f
                setTextColor(Color.WHITE)
                setBackgroundColor(if(index % 2 == 0) blue else orange)
                setPadding(20,18,20,18)
                gravity = Gravity.CENTER
                isAllCaps = false
                setOnClickListener { showPlayer(lecture) }
            }
            val lp = LinearLayout.LayoutParams(-1,130)
            lp.setMargins(0,0,0,18)
            list.addView(b,lp)
        }
        scroll.addView(list)
        root.addView(scroll,LinearLayout.LayoutParams(-1,0,1f))
        setContentView(root)
    }

    private fun showPlayer(lecture:Lecture) {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.BLACK)
            layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundColor(Color.WHITE)
            setPadding(8,8,16,8)
        }
        val back = Button(this).apply {
            text = "رجوع"
            isAllCaps = false
            setOnClickListener { showList() }
        }
        top.addView(back,LinearLayout.LayoutParams(90,60))
        val title = TextView(this).apply {
            text = lecture.title
            textSize = 18f
            setTypeface(null,Typeface.BOLD)
            setTextColor(Color.rgb(14,19,48))
            gravity = Gravity.CENTER
        }
        top.addView(title,LinearLayout.LayoutParams(0,60,1f))
        root.addView(top)
        val web = WebView(this).apply {
            setBackgroundColor(Color.BLACK)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            loadDataWithBaseURL("https://www.youtube.com", htmlFor(lecture.videoId), "text/html", "UTF-8", null)
        }
        root.addView(web,LinearLayout.LayoutParams(-1,0,1f))
        val note = TextView(this).apply {
            text = "يحتاج تشغيل المحاضرة إلى اتصال بالإنترنت."
            textSize = 14f
            setTextColor(Color.WHITE)
            setPadding(18,12,18,18)
            gravity = Gravity.CENTER
        }
        root.addView(note,LinearLayout.LayoutParams(-1,-2))
        setContentView(root)
    }

    private fun htmlFor(id:String) = """<!doctype html><html><head><meta name="viewport" content="width=device-width,initial-scale=1"><style>html,body,#p{margin:0;width:100%;height:100%;background:#000;overflow:hidden}</style></head><body><div id="p"></div><script>var s=document.createElement('script');s.src='https://www.youtube.com/iframe_api';document.head.appendChild(s);function onYouTubeIframeAPIReady(){new YT.Player('p',{videoId:'$id',playerVars:{playsinline:1,controls:1,rel:0,fs:1}});}</script></body></html>"""
}