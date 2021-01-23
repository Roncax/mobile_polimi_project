package com.example.iadvice.ranking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.R
import com.example.iadvice.databinding.RankingFragmentBinding
import com.google.firebase.storage.StorageReference

class RankingFragment : Fragment() {

    companion object {
        const val TAG = "RANKING_FRAGMENT"
    }

    private lateinit var binding: RankingFragmentBinding


    private val imageListObserver =
        Observer <MutableMap<String, StorageReference>> { _ ->
            Log.d(TAG, "new category updated: '' ")
            binding.rankingList.layoutManager = LinearLayoutManager(context)
            val adapter = CustomAdapter(requireContext(), PersistenceUtils.userListRank)
            binding.rankingList.adapter = adapter
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // bind the login_fragment layout with the binding variable
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.ranking_fragment, container, false
        )

        PersistenceUtils.retrieveSortedUserList()

        binding.rankingList.layoutManager = LinearLayoutManager(context)
        val adapter = CustomAdapter(requireContext(), PersistenceUtils.userListRank)
        binding.rankingList.adapter = adapter

        PersistenceUtils.allUserImagesLiveData.observe(viewLifecycleOwner, imageListObserver)

        return binding.root
    }

}