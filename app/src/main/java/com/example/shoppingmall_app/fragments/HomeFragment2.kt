package com.example.shoppingmall_app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingmall_app.R
import com.example.shoppingmall_app.contentList.BookmarkRvAdapter
import com.example.shoppingmall_app.contentList.ContentModel
import com.example.shoppingmall_app.databinding.FragmentHome2Binding
import com.example.shoppingmall_app.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeFragment2 : Fragment() {

    private  lateinit var binding: FragmentHome2Binding
    lateinit var rvAdapter: BookmarkRvAdapter
    val items = ArrayList<ContentModel>()
    val bookmarkList = mutableListOf<String>()
    val itemKeyList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home2, container, false)

        binding.tipTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment2_to_tipFragment)
        }

        binding.talkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment2_to_talkFragment)
        }

        binding.bookmarkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment2_to_bookmarkFragment)
        }

        binding.storeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment2_to_shopFragment)
        }

        rvAdapter = BookmarkRvAdapter(requireContext(), items, itemKeyList, bookmarkList)
        val rv : RecyclerView = binding.mainRV
        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(requireContext(),2)
        getCategoryData()

        return binding.root
    }
    private fun getCategoryData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    //데이터 받아 올 때
                    val item = dataModel.getValue(ContentModel::class.java)

                        items.add(item!!)
                        itemKeyList.add(dataModel.key.toString())

                }
                rvAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
         FBRef.conetent.child("categoryALl").addValueEventListener(postListener)
    }
}