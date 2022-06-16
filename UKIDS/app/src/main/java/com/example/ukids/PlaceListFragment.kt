package com.example.ukids

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ukids.databinding.FragmentPlaceListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlaceListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaceListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentPlaceListBinding.inflate(inflater, container, false)

        //val returnType = arguments?.getString("returnType")

        // XML 데이터 가져오기
        val call: Call<responseInfo> = MyApplication.networkServiceXml.getXmlList(
            "pYVi5WRhkWtEwEK/I4kgN7b4rNT0ilJBAD0ZrcvBAAFFgV3kqfOSQ9cQn5eEczFo+9O1Q1g5b0UiGp10XfJcOA==",
            "xml",
            1,
            30
        )

        call?.enqueue(object : Callback<responseInfo> {
            override fun onResponse(call: Call<responseInfo>, response: Response<responseInfo>) {
                if(response.isSuccessful){
                    Log.d("mobileApp", "$response")
                    binding.PlaceListRecyclerView.layoutManager = LinearLayoutManager(activity)
                    binding.PlaceListRecyclerView.adapter = XmlAdapter(activity as Context, response.body()!!.row)
                }
            }

            override fun onFailure(call: Call<responseInfo>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlaceListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlaceListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}