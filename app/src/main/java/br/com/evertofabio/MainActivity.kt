package br.com.evertofabio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.evertofabio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.date2.mask = "yyyy-MM-dd"

        binding.button.setOnClickListener {
            validAll()
        }
    }

    private fun validAll() {
        Toast.makeText(this, "All right " +
                "${binding.date1} " +
                "${binding.date2}", Toast.LENGTH_LONG).show()
    }
}