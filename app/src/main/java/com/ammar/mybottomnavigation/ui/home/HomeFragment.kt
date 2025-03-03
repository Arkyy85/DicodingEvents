package com.ammar.mybottomnavigation.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ammar.core.data.Resource
import com.ammar.mybottomnavigation.databinding.FragmentHomeBinding
import com.ammar.core.ui.EventsHorizAdapter
import com.ammar.mybottomnavigation.R
import com.ammar.mybottomnavigation.ui.detail.DetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var upcomingAdapter: EventsHorizAdapter
    private lateinit var finishedAdapter: EventsHorizAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            upcomingAdapter = EventsHorizAdapter()
            finishedAdapter = EventsHorizAdapter()

            upcomingAdapter.onItemClick = { selectedEvent ->
                val intent = Intent(activity, DetailsActivity::class.java).apply {
                    putExtra("EXTRA_EVENT", selectedEvent)
                }
                startActivity(intent)
            }

            finishedAdapter.onItemClick = { selectedEvent ->
                val intent = Intent(activity, DetailsActivity::class.java).apply {
                    putExtra("EXTRA_EVENT", selectedEvent)
                }
                startActivity(intent)
            }

            homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        showLoading(false)
                        val events = resource.data
                        if (events.isNullOrEmpty()) {
                            binding.tvNoUpcoming.visibility = View.VISIBLE
                        } else {
                            binding.rvUpcoming.visibility = View.VISIBLE
                            binding.tvNoUpcoming.visibility = View.GONE
                            upcomingAdapter.submitList(events)
                        }
                    }
                    is Resource.Error -> {
                        binding.tvNoUpcoming.visibility = View.VISIBLE
                        binding.tvNoUpcoming.text = "Error mengambil data"
                        showLoading(false)
                    }
                }
            }

            homeViewModel.finishedEvents.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        showLoading(false)
                        finishedAdapter.submitList(resource.data)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showError(resource.message)
                    }
                }
            }

            with(binding.rvUpcoming) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = upcomingAdapter
            }

            with(binding.rvFinished) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = finishedAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(message: String?) {
        binding.viewError.visibility = View.VISIBLE
        binding.tvError.text = message ?: getString(R.string.something_wrong)
    }
}
