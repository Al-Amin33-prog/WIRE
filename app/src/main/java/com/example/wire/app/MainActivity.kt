package com.example.wire.app
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Greeting("Android")
        }


    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text("Hello $name!",
        modifier = modifier)

}
@Preview(showBackground = true)
@Composable
fun GreetingPreview(){
    Greeting("Android")
}

