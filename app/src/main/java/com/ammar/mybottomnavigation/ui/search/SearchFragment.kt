package com.ammar.mybottomnavigation.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ammar.core.data.Resource
import com.ammar.core.ui.EventsAdapter
import com.ammar.mybottomnavigation.R
import com.ammar.mybottomnavigation.databinding.ActivitySearchBinding
import com.ammar.mybottomnavigation.ui.detail.DetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var adapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivitySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EventsAdapter()
        adapter.onItemClick = { selectedEvent ->
            val intent = Intent(requireContext(), DetailsActivity::class.java).apply {
                putExtra("EXTRA_EVENT", selectedEvent)
            }
            startActivity(intent)
        }

        with(binding.recyclerViewSearchResults) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@SearchFragment.adapter
        }

        searchViewModel.searchEvents("").observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    showLoading(false)
                    showError(resource.message)
                }
            }
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchViewModel.searchEvents(s.toString()).observe(viewLifecycleOwner) { resource ->
                    when (resource) {
                        is Resource.Loading -> showLoading(true)
                        is Resource.Success -> {
                            showLoading(false)
                            adapter.submitList(resource.data){
                                binding.recyclerViewSearchResults.scrollToPosition(0)
                            }
                        }
                        is Resource.Error -> {
                            showLoading(false)
                            showError(resource.message)
                        }
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
                searchViewModel.searchEvents(s.toString()).observe(viewLifecycleOwner) { resource ->
                    when (resource) {
                        is Resource.Loading -> showLoading(true)
                        is Resource.Success -> {
                            showLoading(false)
                            adapter.submitList(resource.data) {
                                binding.recyclerViewSearchResults.scrollToPosition(0)
                            }
                        }
                        is Resource.Error -> {
                            showLoading(false)
                            showError(resource.message)
                        }
                    }
                }
            }
        })

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String?) {
        binding.viewError.visibility = View.VISIBLE
        binding.tvError.text = message ?: getString(R.string.something_wrong)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
