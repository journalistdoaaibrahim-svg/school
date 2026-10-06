package com.alekhlas.lectures

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

data class Lecture(val title:String,val videoId:String)
private val lectures=listOf(
 Lecture("محاضرة قوانين السير","rMVZwpDlARo"),
 Lecture("محاضرة الميكانيكا","sKbqPG386-c"),
 Lecture("محاضرة الإشارات","P_EMWF63d_o")
)
private val Blue=Color(0xFF0B2BE0)
private val Orange=Color(0xFFFF6A00)
private val Background=Color(0xFFF5F7FF)
private val Ink=Color(0xFF0E1330)

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{App()}}
}

@Composable private fun App(){
 CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl){
  MaterialTheme(colorScheme=lightColorScheme(primary=Blue,secondary=Orange,background=Background,surface=Color.White,onBackground=Ink,onSurface=Ink)){
   var selected by remember{mutableStateOf<Lecture?>(null)}
   if(selected==null) LectureList{selected=it} else Player(selected!!){selected=null}
  }
 }
}

@Composable private fun LectureList(onOpen:(Lecture)->Unit){
 Scaffold(topBar={CenterAlignedTopAppBar(title={Column(horizontalAlignment=Alignment.CenterHorizontally){Text("مدرسة الإخلاص",fontWeight=FontWeight.ExtraBold);Text("المحاضرات التعليمية",style=MaterialTheme.typography.labelMedium)}})}){p->
  LazyColumn(Modifier.fillMaxSize().background(Background).padding(p),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
   item{Text("أهلًا بك في تطبيق المحاضرات",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold);Text("اختر المحاضرة التي تريد مشاهدتها",color=Color.Gray,modifier=Modifier.padding(vertical=8.dp))}
   items(lectures){lecture->
    Card(Modifier.fillMaxWidth().clickable{onOpen(lecture)},elevation=CardDefaults.cardElevation(4.dp)){
     Row(Modifier.fillMaxWidth().padding(18.dp),verticalAlignment=Alignment.CenterVertically){
      Icon(Icons.Filled.PlayCircle,null,tint=Blue,modifier=Modifier.size(48.dp));Spacer(Modifier.width(14.dp))
      Text(lecture.title,fontWeight=FontWeight.Bold,style=MaterialTheme.typography.titleMedium)
     }
    }
   }
  }
 }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable private fun Player(lecture:Lecture,onBack:()->Unit){
 Scaffold(topBar={TopAppBar(title={Text(lecture.title,maxLines=1)},navigationIcon={IconButton(onClick=onBack){Icon(Icons.Filled.ArrowBack,"رجوع")}})}){p->
  Column(Modifier.fillMaxSize().padding(p).background(Color.Black)){
   AndroidView(Modifier.fillMaxWidth().aspectRatio(16f/9f),factory={context->
    WebView(context).apply{
     setBackgroundColor(android.graphics.Color.BLACK)
     settings.javaScriptEnabled=true
     settings.domStorageEnabled=true
     settings.mediaPlaybackRequiresUserGesture=false
     webViewClient=WebViewClient();webChromeClient=WebChromeClient()
     loadDataWithBaseURL("https://www.youtube.com",htmlFor(lecture.videoId),"text/html","UTF-8",null)
    }
   })
   Text("يحتاج تشغيل المحاضرة إلى اتصال بالإنترنت.",color=Color.White,modifier=Modifier.padding(16.dp))
  }
 }
}

private fun htmlFor(id:String)= """<!doctype html><html><head><meta name="viewport" content="width=device-width,initial-scale=1"><style>html,body,#p{margin:0;width:100%;height:100%;background:#000;overflow:hidden}</style></head><body><div id="p"></div><script>var s=document.createElement('script');s.src='https://www.youtube.com/iframe_api';document.head.appendChild(s);function onYouTubeIframeAPIReady(){new YT.Player('p',{videoId:'$id',playerVars:{playsinline:1,controls:1,rel:0,fs:1}});}</script></body></html>""".trimIndent()
