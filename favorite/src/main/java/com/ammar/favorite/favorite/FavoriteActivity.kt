package com.ammar.favorite.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ammar.core.di.databaseModule
import com.ammar.core.di.repositoryModule
import com.ammar.core.ui.EventsAdapter
import com.ammar.favorite.databinding.FragmentFavoriteBinding
import com.ammar.mybottomnavigation.ui.detail.DetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: EventsAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFavoriteBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loadKoinModules(mapsModule)
        loadKoinModules(databaseModule)
        loadKoinModules(repositoryModule)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Favorite List"
            setDisplayHomeAsUpEnabled(true) // Tombol Back
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        adapter = EventsAdapter()
        adapter.onItemClick = { selectedEvent ->
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("EXTRA_EVENT", selectedEvent)
            }
            startActivity(intent)
        }

        favoriteViewModel.favoriteEvents.observe(this) { dataEvents ->
            adapter.submitList(dataEvents)
            updateEmptyState(dataEvents.isEmpty())
        }

        with(binding.rvUpcoming) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@FavoriteActivity.adapter
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvUpcoming.visibility = View.GONE
            binding.emptyAnimation.apply {
                visibility = View.VISIBLE
                playAnimation()
            }
        } else {
            binding.rvUpcoming.visibility = View.VISIBLE
            binding.emptyAnimation.apply {
                cancelAnimation()
                visibility = View.GONE
            }
        }
    }
}
