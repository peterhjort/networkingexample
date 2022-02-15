package com.example.networkingexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.networkingexample.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.button.setOnClickListener {
            viewModel.readPlayers()
        }

        viewModel.players.observe(this) {
            println("players changed!!!")
            binding.textView.text = it.joinToString(", ")
        }
    }
}

class MainActivityViewModel: ViewModel() {
    var players: MutableLiveData<List<Player>> = MutableLiveData()

    fun readPlayers() {
        viewModelScope.launch {
            try {
                players.value = PlayerApi.retrofitService.getPlayerList()
                println("Read players from NW with great success.")
            } catch (e: Exception) {
                println("No luck in reading players from NW: ${e}")
            }

        }
    }
}