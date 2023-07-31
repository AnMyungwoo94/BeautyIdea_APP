package com.example.shoppingmall_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.shoppingmall_app.R
import com.example.shoppingmall_app.databinding.FragmentShopBinding


class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false)
        val webView: WebView = binding.storeWebView
        webView.loadUrl("https://www.oliveyoung.co.kr/store/main/main.do?oy=0")

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_shopFragment_to_homeFragment2)
        }

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_shopFragment_to_tipFragment)
        }

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_shopFragment_to_bookmarkFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_shopFragment_to_talkFragment)
        }
        return binding.root
    }
}