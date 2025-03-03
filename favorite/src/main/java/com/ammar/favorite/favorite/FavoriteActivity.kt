package com.ammar.favorite.favorite

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
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
        supportActionBar?.title = "Favorite List"
        enableEdgeToEdge()

        adapter = EventsAdapter()
        adapter.onItemClick = { selectedEvent ->
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("EXTRA_EVENT", selectedEvent)
            }
            startActivity(intent)
        }

        favoriteViewModel.favoriteEvents.observe(this) { dataTourism ->
            adapter.submitList(dataTourism)
        }

        with(binding.rvUpcoming) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@FavoriteActivity.adapter
        }
    }
}

//class SearchFragment : Fragment() {
//    private var _binding: FragmentFavoriteBinding? = null
//    private val binding get() = _binding!!
//
//    private val extraEvent: String = "EXTRA_EVENT"
//    private val favoriteViewModel: FavoriteViewModel by viewModel()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        loadKoinModules(mapsModule)
//
//        if (activity != null) {
//
//            val eventsAdapter = EventsAdapter()
//            eventsAdapter.onItemClick = { selectedData ->
//                val intent = Intent(activity, DetailsActivity::class.java)
//                intent.putExtra(DetailsActivity.EXTRA_EVENT, selectedData)
//                startActivity(intent)
//            }
//
//            favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { dataTourism ->
//                eventsAdapter.submitList(dataTourism)
//            }
//
//            with(binding.rvUpcoming) {
//                layoutManager = LinearLayoutManager(context)
//                setHasFixedSize(true)
//                adapter = eventsAdapter
//            }
//        }