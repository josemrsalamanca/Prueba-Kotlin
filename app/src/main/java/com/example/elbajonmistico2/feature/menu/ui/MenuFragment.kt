package com.example.elbajonmistico2.feature.menu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elbajonmistico2.databinding.FragmentMenuBinding
import com.example.elbajonmistico2.feature.menu.data.MenuRepository
import com.example.elbajonmistico2.feature.menu.data.remote.MenuResponse
import com.example.elbajonmistico2.feature.menu.data.remote.RetrofitClient
import com.example.elbajonmistico2.feature.menu.presentation.MenuViewModel
import com.example.elbajonmistico2.feature.menu.presentation.MenuViewModelFactory

class MenuFragment:Fragment() {

    private val menuApi = RetrofitClient.getRetrofit()
    private val menuRepository = MenuRepository(menuApi)
    private val menuViewModelFactory = MenuViewModelFactory(menuRepository)
    private val menuViewModel: MenuViewModel by activityViewModels(){
        menuViewModelFactory
    }
    private var rawBinding: FragmentMenuBinding ?= null
    private val binding get() = rawBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rawBinding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupViewModel() {
        menuViewModel.state().observe(this){
            it?.let{safeMenuResponse->
                handleUI(safeMenuResponse)                
            }
        }
        menuViewModel.getMenu()
    }

    private fun handleUI(safeMenuResponse: MenuResponse) {
        val adapter= MenuAdapter(safeMenuResponse.products)
        binding.rvMenu.adapter = adapter
    }

    private fun setupRecyclerView() {
        binding.rvMenu.layoutManager = LinearLayoutManager(context)
    }
}