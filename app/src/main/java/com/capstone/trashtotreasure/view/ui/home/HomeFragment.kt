package com.capstone.trashtotreasure.view.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.FragmentHomeBinding
import com.capstone.trashtotreasure.view.ui.adapter.ArticleAdapter
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        binding.tvName.text = "Hi! "+firebaseUser?.displayName.toString()
        Glide.with(requireContext())
            .load(firebaseUser?.photoUrl)
            .circleCrop()
            .into(binding.ivProfile)

        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }

        articleAdapter = ArticleAdapter(this@HomeFragment, emptyList()) // Initialize with empty list

        binding.loadingShimmer.visibility = View.VISIBLE
        binding.rvArtikel.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = articleAdapter
        }

        homeViewModel.getAllArticle().observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                val data = response.articles
                articleAdapter.updateData(data) // Update the data in the adapter
                binding.loadingShimmer.visibility = View.INVISIBLE
            }
            result.onFailure {
                showToast(
                    requireContext(),
                    getString(R.string.error_occurred)
                )
                binding.loadingShimmer.visibility = View.VISIBLE
            }
        }

    }


    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}