package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmetFavoriteTracksBinding
import com.example.playlistmaker.ui.library.view_model.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private var _binding: FragmetFavoriteTracksBinding? = null
    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmetFavoriteTracksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    
    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}