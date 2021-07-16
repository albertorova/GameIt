package com.example.gameit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameit.R
import com.example.gameit.adapters.FindAdapter
import kotlinx.android.synthetic.main.fragment_find.*


class FindFragment : Fragment() {

    var demoNames = listOf(
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft",
        "Minecraft"
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAdapter = FindAdapter(demoNames)
        findRecyclerView.layoutManager =
            GridLayoutManager(context, 2)
        findRecyclerView.adapter = mAdapter
    }

}

