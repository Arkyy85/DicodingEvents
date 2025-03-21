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
import com.ammar.mybottomnavigation.databinding.FragmentSearchBinding
import com.ammar.mybottomnavigation.ui.detail.DetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var adapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
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
                    showError()
                }
            }
        }

        binding.searchTextInputLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {
                performSearch(s.toString())
            }
        })
    }

    private fun performSearch(query: String) {
        searchViewModel.searchEvents(query).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    adapter.submitList(resource.data) {
                        binding.recyclerViewSearchResults.scrollToPosition(0)
                    }
                    updateEmptyState(resource.data.isNullOrEmpty())
                }
                is Resource.Error -> {
                    showLoading(false)
                    showError()
                }
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyAnimation.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.tvErrorCompound.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewSearchResults.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError() {
        binding.tvErrorCompound.visibility = View.VISIBLE
        binding.tvErrorCompound.text = getString(R.string.something_wrong)
        binding.emptyAnimation.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewSearchResults.adapter = null
        _binding = null
    }
}
