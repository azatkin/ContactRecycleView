package com.example.customview2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var customViewIndicator: CustomViewIndicator

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            loadContacts()
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                Toast.makeText(this, "Для использования функции разрешите доступ в настройках", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Доступ к контактам отклонён", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customViewIndicator = findViewById(R.id.indicator)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        contactAdapter = ContactAdapter(emptyList())
        recyclerView.adapter = contactAdapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = layoutManager.findFirstVisibleItemPosition()
                customViewIndicator.setCurrentPage(position)
            }
        })

        // Проверка разрешений
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                showPermission()
            }else{
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        } else {
            loadContacts()
        }
    }

    private fun showPermission(){
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Доступ к контактам")
            .setMessage("Приложению нужно разрешение на чтение ваших контактов для отображения списка контактов.")
            .setPositiveButton("Разрешить") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun loadContacts() {
        val contacts = mutableListOf<Contact>() // Список для хранения контактов

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                contacts.add(Contact(name, number))
            }
        }

        // Обновляем данные в адаптере
        contactAdapter.updateContacts(contacts)

        customViewIndicator.setup(contacts.size)

    }

}