package idn.fahru.aplikasitodogambar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import idn.fahru.aplikasitodogambar.databinding.ActivityMainBinding
import idn.fahru.aplikasitodogambar.model.ModelData
import idn.fahru.aplikasitodogambar.recyclerview.adapter.ItemDataAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // buat variabel adapter untuk recyclerview
    private lateinit var adapterMain: ItemDataAdapter

    private lateinit var databaseUser : DatabaseReference

    private lateinit var valueEventListener: ValueEventListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.extendedFab.setOnClickListener {
            val intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
        }

        adapterMain = ItemDataAdapter()

        // setting RecyclerView
        binding.rvMain.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterMain
            setHasFixedSize(true)
        }



        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.childrenCount > 0) {
                    val daftarUser = arrayListOf<ModelData>()
                    for(dataUser in snapshot.children) {
                        val data = dataUser.getValue(ModelData::class.java) as ModelData
                        daftarUser.add(data)
                    }
                    adapterMain.addData(daftarUser)
                    adapterMain.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        databaseUser = FirebaseDatabase.getInstance().reference.child("users")
        databaseUser.addValueEventListener(valueEventListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        // ini jangan dihapus.. setiap kali kita menambahkan eventlistener
        // maka perlu dihapus dengan cara removeEventListener
        // jika penambahan terjadi di oncreate
        // maka hapusnya itu ada di onDestroy seperti kode di bawah ini
        databaseUser.removeEventListener(valueEventListener)
    }
}