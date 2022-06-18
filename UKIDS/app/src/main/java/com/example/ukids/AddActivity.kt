package com.example.ukids

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.ukids.databinding.ActivityAddBinding
import com.google.android.gms.maps.GoogleMap

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding

    lateinit var adbuilder: AlertDialog.Builder
    lateinit var ad: AlertDialog

    val eventHandler = object : DialogInterface.OnClickListener {

        override fun onClick(p0: DialogInterface?, p1: Int) {
            if(p1==DialogInterface.BUTTON_POSITIVE) {
                ad.dismiss()
            }
            else if (p1==DialogInterface.BUTTON_NEGATIVE) {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        adbuilder = AlertDialog.Builder(this)

        adbuilder.setTitle("저장하지 않고 종료")
        adbuilder.setIcon(android.R.drawable.ic_dialog_info)
        adbuilder.setMessage("작성을 취소하시겠습니까?")
        adbuilder.setPositiveButton("작성 취소", eventHandler)
        adbuilder.setNegativeButton("계속 작성", eventHandler)
        adbuilder.setCancelable(false) // true: 뒤로가기 버튼으로 창 사라지게 하기 허용

        ad = adbuilder.create()

        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lat = intent.getStringExtra("lat")
        val lng = intent.getStringExtra("lng")
        intent.putExtra("lat", lat)
        intent.putExtra("lng", lng)

    }

    override fun onBackPressed() {
        ad.show()
    }

    /* 저장 옵션메뉴 */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        //menu?.add(0, 11111, 0, "저장")
        menuInflater.inflate(R.menu.add_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_add_save -> {

                var placename = binding.addPlacename.text.toString()
                intent.putExtra("placename", placename)

                var placetype: String = ""
                when (binding.addChipgroup.checkedChipId) {
                    R.id.add_park -> { placetype = "놀이터" }
                    R.id.add_lib -> { placetype = "도서관" }
                    R.id.add_kidscafe -> { placetype = "키즈카페" }
                    R.id.add_ukids -> { placetype = "유키즈존" }
                }
                intent.putExtra("placetype", placetype)

                var addr = binding.addAddr.text.toString()
                intent.putExtra("addr", addr)

                var content = binding.addContent.text.toString()
                intent.putExtra("content", content)
                setResult(RESULT_OK, intent)
                finish()
                return true
            }
        }
        return false
    }

}