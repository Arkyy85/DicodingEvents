package com.ammar.mybottomnavigation.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat.getParcelableExtra
import com.ammar.mybottomnavigation.R
import com.ammar.core.domain.model.Events
import com.ammar.mybottomnavigation.databinding.ActivityDetailsBinding
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val detailsViewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Detail Events"
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val event = getParcelableExtra(intent, EXTRA_EVENT, Events::class.java)
        showDetailTourism(event)
    }

    private fun showDetailTourism(detailEvents: Events?) {
        detailEvents?.let {
            Glide.with(this)
                .load(detailEvents.mediaCover)
                .into(binding.mediaCover)
            binding.eventName.text = detailEvents.name
            binding.ownerName.text = detailEvents.ownerName
            "Waktu Pelaksanaan : ${detailEvents.beginTime}".also { binding.eventTime.text = it }
            "Sisa Quota : ${detailEvents.registrants?.let { detailEvents.quota?.minus(it) }}".also { binding.eventQuota.text = it }
            binding.eventDescription.text = Html.fromHtml(detailEvents.description, Html.FROM_HTML_MODE_COMPACT)

            var statusFavorite = detailEvents.isFav
            val eventType = detailEvents.eventType
            setStatusFavorite(statusFavorite)
            binding.btnFav.setOnClickListener {
                detailsViewModel.setFavoriteTourism(detailEvents, !statusFavorite, eventType)
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
            }
            binding.btnOpenLink.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(detailEvents.link))
            startActivity(intent)
        }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.btnFav.text = getString(R.string.detail_delete_favorite)
        } else {
            binding.btnFav.text = getString(R.string.detail_add_to_favorite)
        }
    }


//    private fun showLoading(isLoading: Boolean) {
//        if (isLoading) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }

//    private fun showToast(context: Context, message: String) {
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
//    }

//    private fun getViewModel(activity: AppCompatActivity): DetailsViewModel {
//        val factory = ViewModelFactory2.getInstanceViewModel(activity.application)
//        return ViewModelProvider(activity, factory)[DetailsViewModel::class.java]
//    }

    companion object {
        const val EXTRA_EVENT = "EXTRA_EVENT"
    }
}

//        Glide.with(this)
//            .load(event?.mediaCover)
//            .into(binding.mediaCover)
//        binding.eventName.text = event?.name
//        binding.ownerName.text = event?.ownerName
//        "Waktu Pelaksanaan : ${event?.beginTime}".also { binding.eventTime.text = it }
//        if (event != null) {
//            "Sisa Quota : ${event.registrants?.let { event.quota?.minus(it) }}".also { binding.eventQuota.text = it }
//        }
//        binding.eventDescription.text = Html.fromHtml(event?.description, Html.FROM_HTML_MODE_COMPACT)
//        binding.btnOpenLink.setOnClickListener{
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event?.link))
//            startActivity(intent)
//        }
//        showLoading(false)
//
//        detailsViewModel.apply {
//            getFavUser(event?.name.toString()).observe(this@DetailsActivity) { favUserEntity ->
//                if (favUserEntity != null) {
//                    binding.btnFav.text = getString(R.string.detail_delete_favorite)
//                    binding.btnFav.setOnClickListener{
//                        detailsViewModel.deleteFavUser(favUserEntity)
//                        showToast(
//                            this@DetailsActivity,
//                            getString(R.string.removed_from_favorite)
//                        )
//                        binding.btnFav.text = getString(R.string.detail_add_to_favorite)
//                    }
//                } else {
//                    binding.btnFav.setOnClickListener{
//                        binding.btnFav.text = getString(R.string.detail_add_to_favorite)
//                        val favUser = EventsEntity(
//                            id = event?.id,
//                            name = event?.name,
//                            mediaCover = event?.mediaCover,
//                            beginTime = event?.beginTime,
//                            category = event?.category,
//                            cityName = event?.cityName,
//                            description = event?.description,
//                            endTime = event?.endTime,
//                            imageLogo = event?.imageLogo,
//                            link = event?.link,
//                            ownerName = event?.ownerName,
//                            quota = event?.quota,
//                            registrants = event?.registrants,
//                            summary = event?.summary
//                        )
//                        detailsViewModel.addFavUser(favUser)
//                        showToast(this@DetailsActivity, getString(R.string.added_to_favorite))
//                    }
//                }
//            }
//        }
