package com.ammar.mybottomnavigation.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ammar.core.data.Resource
import com.ammar.core.ui.EventsAdapter
import com.ammar.mybottomnavigation.R
import com.ammar.mybottomnavigation.databinding.FragmentFinishUpcBinding
import com.ammar.mybottomnavigation.ui.detail.DetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentFinishUpcBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventsAdapter
    private val upcomingViewModel: UpcomingViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishUpcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            adapter = EventsAdapter()
            adapter.onItemClick = { selectedEvent ->
                val intent = Intent(activity, DetailsActivity::class.java).apply {
                    putExtra("EXTRA_EVENT", selectedEvent)
                }
                startActivity(intent)
            }

            upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) { resource ->
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
                            adapter.submitList(events)
                        }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showError()
                    }
                }
            }

            with(binding.rvUpcoming) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = this@UpcomingFragment.adapter
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvUpcoming.adapter = null
        _binding = null
    }

    private fun showError() {
        binding.tvErrorCompound.visibility = View.VISIBLE
        binding.tvErrorCompound.text = getString(R.string.something_wrong)
        binding.emptyAnimation.visibility = View.VISIBLE
    }
}
