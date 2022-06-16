package com.example.ukids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.example.ukids.databinding.ActivitySearchBinding
import com.google.android.gms.maps.GoogleMap

class SearchActivity : AppCompatActivity() {
    lateinit var resultFragment: PlaceListFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultFragment = PlaceListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_content, resultFragment)  // (어디에, 어떤 프래그먼트를)
            .commit()

        /*
        val bundle = Bundle()  // 프래그먼트에 값 전달 - 번들 객체 이용
        binding.searchBtn.setOnClickListener {

            when (binding.rGroup.checkedRadioButtonId) {
                R.id.json -> bundle.putString("returnType", "json")
                R.id.xml -> bundle.putString("returnType", "xml")
                else -> bundle.putString("returnType", "json")
            }
            fragment.arguments = bundle  // 프래그먼트에 값 전달

            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_content, fragment)  // (어디에, 어떤 프래그먼트를)
                .commit()
        }
        */

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.search_menu, menu) // 두 번째 - 어디에 적용할 것인지: 옵션메뉴에 적용하겠다 menu

        // 액션 뷰 이용: SearchView - search_menu.xml
        val menuSearch = menu?.findItem(R.id.menu_search_search)
        val searchView = menuSearch?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_content, resultFragment)  // (어디에, 어떤 프래그먼트를)
                    .commit()
                return true
            }
        })

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0);
    }

}