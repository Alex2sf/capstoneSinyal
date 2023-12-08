package com.example.sinyal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// ExploreActivity.kt
class ExploreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        val recyclerView: RecyclerView = findViewById(R.id.rv_stories)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val data = generateDummyData() // Ganti ini dengan data sebenarnya
        val adapter = ExploreAdapter(data)
        recyclerView.adapter = adapter
    }

    private fun generateDummyData(): List<YourDataModel> {
        // Buat data palsu sebagai contoh
        return listOf(
            YourDataModel("Sabtu, 01 Dec 2023", "Workshop 1"),
            YourDataModel("Minggu, 02 Dec 2023", "Workshop 2"),
            YourDataModel("Sabtu, 01 Dec 2023", "Workshop 1"),
            YourDataModel("Minggu, 02 Dec 2023", "Workshop 2"),
            YourDataModel("Sabtu, 01 Dec 2023", "Workshop 1"),
            YourDataModel("Minggu, 02 Dec 2023", "Workshop 2"),
            YourDataModel("Sabtu, 01 Dec 2023", "Workshop 1"),
            YourDataModel("Minggu, 02 Dec 2023", "Workshop 2"),
            YourDataModel("Sabtu, 01 Dec 2023", "Workshop 1"),
            YourDataModel("Minggu, 02 Dec 2023", "Workshop 2"),
            YourDataModel("Sabtu, 01 Dec 2023", "Workshop 1"),
            YourDataModel("Minggu, 02 Dec 2023", "Workshop 2"),
            YourDataModel("Sabtu, 01 Dec 2023", "Workshop 1"),
            YourDataModel("Minggu, 02 Dec 2023", "Workshop 2"),
            // Tambahkan item lainnya sesuai kebutuhan
        )
    }
}