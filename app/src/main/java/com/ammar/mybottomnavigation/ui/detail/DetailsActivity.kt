package com.ammar.mybottomnavigation.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ammar.core.domain.model.Events
import com.ammar.mybottomnavigation.R
import com.ammar.mybottomnavigation.databinding.ActivityDetailsBinding
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {
    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private val detailsViewModel: DetailsViewModel by viewModel()
    private var isFavorite: Boolean = false
    private lateinit var menuItemFavorite: MenuItem
    private var currentEvent: Events? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Detail Events"

        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.search_hint)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.details_act)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val event = getParcelableExtra(intent, EXTRA_EVENT, Events::class.java)
        showDetailTourism(event)
    }

    private fun showDetailTourism(detailEvents: Events?) {
        detailEvents?.let { event ->
            binding.apply {
                currentEvent = event
                isFavorite = event.isFav

                Glide.with(this@DetailsActivity)
                    .load(event.mediaCover)
                    .into(mediaCover)
                eventName.text = event.name
                ownerName.text = event.ownerName
                supportActionBar?.title = event.ownerName
                "Waktu Pelaksanaan : ${detailEvents.beginTime}".also { eventTime.text = it }
                "Sisa Quota : ${detailEvents.registrants?.let { detailEvents.quota?.minus(it) }}".also { eventQuota.text = it }
                eventDescription.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_COMPACT)
                btnOpenLink.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                    startActivity(intent)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details_act, menu)
        menuItemFavorite = menu?.findItem(R.id.action_details_fav)!!
        updateFavoriteIcon()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_details_fav -> {
                toggleFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleFavorite() {
        currentEvent?.let { event ->
            val eventType = event.eventType
            detailsViewModel.setFavoriteTourism(event, !isFavorite, eventType)
            isFavorite = !isFavorite
            updateFavoriteIcon()

            val message = if (isFavorite) {
                getString(R.string.toast_added_to_favorite)
            } else {
                getString(R.string.toast_removed_from_favorite)
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFavoriteIcon() {
        if (::menuItemFavorite.isInitialized) {
            menuItemFavorite.setIcon(
                if (isFavorite) R.drawable.action_bar_favorite_selected_selector else R.drawable.action_bar_favorite_selector
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_EVENT = "EXTRA_EVENT"
    }
}
